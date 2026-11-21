package com.xulai.betterparagliders.utils;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.config.BetterParaglidersConfig.ItemCategory;
import com.xulai.betterparagliders.potion.PotionEffects;
import com.xulai.betterparagliders.attributes.BetterParaglidersAttributes;
import com.xulai.betterparagliders.capabilities.StaminaOverride;
import com.xulai.betterparagliders.network.ModNet;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.impl.movement.PlayerMovement;

public final class CalculateStaminaUtils {

    public static final int DEPLETION_RECOVERY_DELAY_TICKS = 100;

    private CalculateStaminaUtils() {}

    public static boolean isRangedWeapon(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (BetterParaglidersConfig.isOverridden(ItemCategory.RANGED, stack.getItem())) return true;
        return stack.getItem() instanceof ProjectileWeaponItem;
    }

    public static boolean isShield(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (BetterParaglidersConfig.isOverridden(ItemCategory.SHIELD, stack.getItem())) return true;
        return stack.canPerformAction(ItemAbilities.SHIELD_BLOCK);
    }

    public static boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    public static boolean isTrident(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof TridentItem;
    }

    public static boolean isChargedThrow(ItemStack stack, Player player, int remainingTicks) {
        if (!isTrident(stack)) return false;
        int usedTicks = stack.getItem().getUseDuration(stack, player) - remainingTicks;
        if (usedTicks < TridentItem.THROW_THRESHOLD_TIME) return false;
        return EnchantmentHelper.getTridentSpinAttackStrength(stack, player) <= 0.0F || player.isInWaterOrRain();
    }

    public static boolean isHandledByBetterCombat(ItemStack stack) {
        if (stack.isEmpty()) return false;
        WeaponAttributes attributes = WeaponRegistry.getAttributes(stack);
        return attributes != null && attributes.attacks() != null;
    }

    public static void chargeSwing(ServerPlayer player, ResourceLocation itemId) {
        if (player.isCreative() || player.isSpectator()) return;

        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == null) return;

        tryConsumeActionStamina(player, calculateMeleeStaminaCost(player, item,
                PlayerAttackHelper.isTwoHandedWielding(player)));
    }

    public static int calculateMeleeStaminaCost(Player player, Item item, boolean twoHanded) {
        double cost = BetterParaglidersConfig.costOf(ItemCategory.MELEE, item);
        if (twoHanded) {
            cost -= player.getAttributeValue(BetterParaglidersAttributes.TWO_HANDED_STAMINA_REDUCTION);
        } else {
            cost -= player.getAttributeValue(BetterParaglidersAttributes.ONE_HANDED_STAMINA_REDUCTION);
        }
        cost -= player.getAttributeValue(BetterParaglidersAttributes.BASE_MELEE_STAMINA_REDUCTION);
        return toCost(applyDiscount(player, cost));
    }

    public static int calculateRangedStaminaCost(Player player, ItemStack stack) {
        double cost = BetterParaglidersConfig.costOf(ItemCategory.RANGED, stack.getItem());
        cost -= player.getAttributeValue(BetterParaglidersAttributes.RANGE_STAMINA_REDUCTION);
        return toCost(cost);
    }

    public static int calculateBlockStaminaCost(Player player, ItemStack stack) {
        double cost = BetterParaglidersConfig.costOf(ItemCategory.SHIELD, stack.getItem());
        cost -= player.getAttributeValue(BetterParaglidersAttributes.BLOCK_STAMINA_REDUCTION);
        return toCost(applyDiscount(player, cost));
    }

    public static boolean tryConsumeActionStamina(ServerPlayer player, int cost) {
        if (cost <= 0) return true;

        Stamina stamina = Stamina.get(player);
        if (stamina.takeStamina(cost, true, true, true) < cost) return false;

        stamina.takeStamina(cost, false, true, false);

        if (stamina instanceof StaminaOverride override) {
            override.setTotalActionStaminaCost(cost);
            ModNet.sendActionStaminaCost(player, cost);
        }

        holdStaminaRecovery(player);
        return true;
    }

    public static boolean hasEnoughStamina(Player player, int cost) {
        if (cost <= 0) return true;
        Stamina stamina = Stamina.get(player);
        return stamina.stamina() + stamina.extraStamina() >= cost;
    }

    public static double getBlockStaminaCost(BlockState state, Level level, BlockPos pos, Player player) {
        if (!BetterParaglidersConfig.enableMiningStamina()) return 0.0D;
        float hardness = state.getDestroySpeed(level, pos);
        if (hardness <= 0.0F) return 0.0D;
        double cost = BetterParaglidersConfig.miningStaminaPerLog() * (hardness / 2.0D);
        if (usesWrongTool(state, player)) cost *= 2.0D;
        return cost;
    }

    private static boolean usesWrongTool(BlockState state, Player player) {
        if (!state.requiresCorrectToolForDrops()) return false;
        return !player.getMainHandItem().isCorrectToolForDrops(state);
    }

    public static double getBlockStaminaDrain(BlockState state, Level level, BlockPos pos, Player player) {
        double total = getBlockStaminaCost(state, level, pos, player);
        if (total <= 0.0D) return 0.0D;
        float progress = state.getDestroyProgress(player, level, pos);
        if (progress <= 0.0F) return 0.0D;
        return total * progress;
    }

    public static boolean drainMiningStamina(ServerPlayer player, double amount) {
        if (!(amount > 0.0D)) return true;
        if (!hasEnoughStamina(player, toCost(amount))) return false;

        Stamina stamina = Stamina.get(player);
        stamina.takeStamina(amount, false, true, false);
        stamina.setDirty(true);
        holdStaminaRecovery(player);
        return true;
    }

    private static void holdStaminaRecovery(ServerPlayer player) {
        int delay = BetterParaglidersConfig.actionRecoveryDelay();
        if (staminaEmpty(Stamina.get(player))) delay = DEPLETION_RECOVERY_DELAY_TICKS;
        if (delay <= 0) return;
        Movement movement = Movement.get(player);
        movement.setRecoveryDelay(Math.max(movement.recoveryDelay(), delay));
    }

    public static int getTillStaminaCost() {
        if (!BetterParaglidersConfig.enableMiningStamina()) return 0;
        return toCost(BetterParaglidersConfig.tillingStaminaCost());
    }

    public static double getModifiedStaminaDelta(PlayerMovement movement) {
        double original = movement.staminaDelta();
        Player player = movement.player();
        String statePath = movement.state().id().getPath();

        double modified = switch (statePath) {
            case "idle" -> original + attribute(player, BetterParaglidersAttributes.IDLE_STAMINA_REGEN);
            case "running" -> original + attribute(player, BetterParaglidersAttributes.SPRINTING_STAMINA_REDUCTION);
            case "swimming" -> original + attribute(player, BetterParaglidersAttributes.SWIMMING_STAMINA_REDUCTION);
            case "underwater" -> original + attribute(player, BetterParaglidersAttributes.SUBMERGED_STAMINA_REGEN);
            case "breathing_underwater" -> original + attribute(player, BetterParaglidersAttributes.WATER_BREATHING_STAMINA_REGEN);
            case "fast_running" -> original + attribute(player, BetterParaglidersAttributes.FAST_RUNNING_STAMINA_REDUCTION);
            case "fast_swimming" -> original + attribute(player, BetterParaglidersAttributes.FAST_SWIMMING_STAMINA_REDUCTION);
            case "cling_to_cliff" -> original + attribute(player, BetterParaglidersAttributes.CLING_TO_CLIFF_STAMINA_REDUCTION);
            case "horizontal_wall_run" -> original + attribute(player, BetterParaglidersAttributes.HORIZONTAL_WALL_RUN_STAMINA_REDUCTION);
            case "dodge" -> original + attribute(player, BetterParaglidersAttributes.DODGE_STAMINA_REDUCTION);
            case "roll" -> original + attribute(player, BetterParaglidersAttributes.ROLL_STAMINA_REDUCTION);
            case "breakfall" -> original + attribute(player, BetterParaglidersAttributes.BREAKFALL_STAMINA_REDUCTION);
            case "vault" -> original + attribute(player, BetterParaglidersAttributes.VAULT_STAMINA_REDUCTION);
            case "climb_up" -> original + attribute(player, BetterParaglidersAttributes.CLIMB_UP_STAMINA_REDUCTION);
            case "vertical_wall_run" -> original + attribute(player, BetterParaglidersAttributes.VERTICAL_WALL_RUN_STAMINA_REDUCTION);
            case "cat_leap" -> original + attribute(player, BetterParaglidersAttributes.CAT_LEAP_STAMINA_REDUCTION);
            case "charge_jump" -> original + attribute(player, BetterParaglidersAttributes.CHARGE_JUMP_STAMINA_REDUCTION);
            default -> original;
        };

        modified = applyMovementDiscount(player, statePath, original, modified);

        if (original >= 0) return Math.max(modified, original);
        return Math.min(0, modified);
    }

    private static double applyMovementDiscount(Player player, String statePath, double original, double modified) {
        if (original >= 0) return modified;
        if (!isDiscountableState(statePath)) return modified;
        double discount = sprintDiscountFor(player);
        if (discount <= 0.0D) return modified;
        return modified * (1.0D - discount);
    }

    private static boolean isDiscountableState(String statePath) {
        return "running".equals(statePath) || "fast_running".equals(statePath)
                || "swimming".equals(statePath) || "fast_swimming".equals(statePath);
    }

    private static double applyDiscount(Player player, double cost) {
        double discount = staminaDiscountFor(player);
        if (discount <= 0.0D) return cost;
        return cost * (1.0D - discount);
    }

    private static double staminaDiscountFor(Player player) {
        MobEffectInstance instance = player.getEffect(PotionEffects.STAMINA_EFFICIENCY);
        if (instance == null) return 0.0D;
        double discount = BetterParaglidersConfig.staminaEfficiencyDiscount(instance.getAmplifier());
        return Math.max(0.0D, Math.min(1.0D, discount));
    }

    private static double sprintDiscountFor(Player player) {
        MobEffectInstance instance = player.getEffect(PotionEffects.SPRINT_EFFICIENCY);
        if (instance == null) return 0.0D;
        double discount = BetterParaglidersConfig.sprintEfficiencyDiscount(instance.getAmplifier());
        return Math.max(0.0D, Math.min(1.0D, discount));
    }

    public static boolean staminaEmpty(Stamina stamina) {
        return stamina.stamina() <= 0 && stamina.extraStamina() <= 0;
    }

    private static double attribute(Player player, Holder<Attribute> attribute) {
        return player.getAttributeValue(attribute);
    }

    private static int toCost(double cost) {
        return cost <= 0 ? 0 : (int) Math.ceil(cost);
    }
}
