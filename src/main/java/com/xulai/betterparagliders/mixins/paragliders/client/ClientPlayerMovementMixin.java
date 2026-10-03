package com.xulai.betterparagliders.mixins.paragliders.client;

import com.xulai.betterparagliders.capabilities.StaminaOverride;
import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.network.ModNet;
import com.xulai.betterparagliders.plugin.BetterParaglidersMovementPlugin;
import com.xulai.betterparagliders.utils.CalculateStaminaUtils;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.PlayerState;
import tictim.paraglider.impl.movement.ClientPlayerMovement;
import tictim.paraglider.impl.movement.PlayerMovement;

@Mixin(ClientPlayerMovement.class)
public abstract class ClientPlayerMovementMixin {

    @Inject(method = "update", at = @At("HEAD"), remap = false)
    private void betterparagliders$onUpdate(CallbackInfo ci) {
        PlayerMovement self = (PlayerMovement) (Object) this;

        int pendingCost = ModNet.consumePendingActionStaminaCost();
        if (pendingCost > 0 && self.stamina() instanceof StaminaOverride override) {
            override.setTotalActionStaminaCost(pendingCost);
        }

        PlayerState state = self.state();
        if (state.is(BetterParaglidersMovementPlugin.DROWNING)) {
            Player player = self.player();
            player.setSprinting(false);
            player.setSwimming(false);

            double sinkSpeed = BetterParaglidersConfig.drowningSinkSpeed();
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, -sinkSpeed, motion.z);
            return;
        }

        if (!BetterParaglidersConfig.blockSwingWithoutStamina()) return;

        Player player = self.player();
        if (player.isCreative() || player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        int cost = CalculateStaminaUtils.calculateMeleeStaminaCost(player, stack.getItem(),
                PlayerAttackHelper.isTwoHandedWielding(player));
        if (CalculateStaminaUtils.hasEnoughStamina(player, cost)) return;

        if (Minecraft.getInstance() instanceof MinecraftClient_BetterCombat betterCombat) {
            betterCombat.cancelUpswing();
        }
    }
}