package com.xulai.betterparagliders.events;

import com.xulai.betterparagliders.BetterParaglidersMod;
import com.xulai.betterparagliders.capabilities.MiningDrainTracker;
import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.utils.CalculateStaminaUtils;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = BetterParaglidersMod.MOD_ID)
public final class BetterParaglidersEventHandler {

    private static final int MINIMUM_BOW_CHARGE = 2;

    private static final Map<Player, Long> LAST_MELEE_TICK = Collections.synchronizedMap(new WeakHashMap<>());

    private BetterParaglidersEventHandler() {}

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        int cost = CalculateStaminaUtils.calculateMeleeStaminaCost(player, stack.getItem(),
                PlayerAttackHelper.isTwoHandedWielding(player));

        if (!CalculateStaminaUtils.hasEnoughStamina(player, cost)) {
            if (BetterParaglidersConfig.blockAttackWithoutStamina()) event.setCanceled(true);
            return;
        }

        if (CalculateStaminaUtils.isHandledByBetterCombat(stack)) return;

        long tick = player.level().getGameTime();
        Long lastTick = LAST_MELEE_TICK.get(player);
        if (lastTick != null && lastTick == tick) return;
        LAST_MELEE_TICK.put(player, tick);

        CalculateStaminaUtils.tryConsumeActionStamina(player, cost);
    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        ItemStack stack = event.getBow();
        if (!CalculateStaminaUtils.isCrossbow(stack)
                && (!event.hasAmmo() || event.getCharge() < MINIMUM_BOW_CHARGE)) return;

        CalculateStaminaUtils.tryConsumeActionStamina(player,
                CalculateStaminaUtils.calculateRangedStaminaCost(player, stack));
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;
        if (!event.getBlocked()) return;

        ItemStack stack = player.getUseItem();
        if (!CalculateStaminaUtils.isShield(stack)) return;

        CalculateStaminaUtils.tryConsumeActionStamina(player,
                CalculateStaminaUtils.calculateBlockStaminaCost(player, stack));
    }

    @SubscribeEvent
    public static void onUseItemTick(LivingEntityUseItemEvent.Tick event) {
        if (!BetterParaglidersConfig.blockItemUseWithoutStamina()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        ItemStack stack = event.getItem();
        if (!CalculateStaminaUtils.isRangedWeapon(stack) && !CalculateStaminaUtils.isShield(stack)) return;
        if (CalculateStaminaUtils.isCrossbow(stack) && CrossbowItem.isCharged(stack)) return;

        if (!canAfford(player, stack)) player.stopUsingItem();
    }

    @SubscribeEvent
    public static void onToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated()) return;
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        int cost = CalculateStaminaUtils.getTillStaminaCost();
        if (cost <= 0) return;

        if (!CalculateStaminaUtils.hasEnoughStamina(player, cost)) {
            if (BetterParaglidersConfig.blockMiningWithoutStamina()) event.setCanceled(true);
            return;
        }

        CalculateStaminaUtils.tryConsumeActionStamina(player, cost);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;
        if (!BetterParaglidersConfig.enableMiningStamina()) return;
        if (!(player.gameMode instanceof MiningDrainTracker tracker)) return;
        if (tracker.isDrained(event.getPos())) return;

        double cost = CalculateStaminaUtils.getBlockStaminaCost(event.getState(), player.serverLevel(),
                event.getPos(), player);
        if (cost <= 0.0D) return;

        if (CalculateStaminaUtils.drainMiningStamina(player, cost)) return;
        if (BetterParaglidersConfig.blockMiningWithoutStamina()) event.setCanceled(true);
    }

    private static boolean canAfford(Player player, ItemStack stack) {
        if (CalculateStaminaUtils.isShield(stack)) {
            return CalculateStaminaUtils.hasEnoughStamina(player,
                    CalculateStaminaUtils.calculateBlockStaminaCost(player, stack));
        }
        return CalculateStaminaUtils.hasEnoughStamina(player,
                CalculateStaminaUtils.calculateRangedStaminaCost(player, stack));
    }
}
