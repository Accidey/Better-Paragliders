package com.xulai.betterparagliders.mixins.minecraft;

import com.xulai.betterparagliders.plugin.BetterParaglidersMovementPlugin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.api.movement.Movement;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "decreaseAirSupply", at = @At("HEAD"), cancellable = true, remap = false)
    private void betterparagliders$accelerateDrowning(int airSupply, CallbackInfoReturnable<Integer> cir) {
        Object self = this;
        if (!(self instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        if (Movement.get(player).state().is(BetterParaglidersMovementPlugin.DROWNING)) {
            cir.setReturnValue(airSupply - 3);
        }
    }
}