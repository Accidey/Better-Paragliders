package com.xulai.betterparagliders.mixins.paragliders.client;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.plugin.BetterParaglidersMovementPlugin;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.Movement;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerAiStepMixin {

    @Inject(method = "aiStep", at = @At("HEAD"), remap = false)
    private void betterparagliders$drowningPreventInput(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) (Object) this;
        if (self.isCreative() || self.isSpectator()) return;
        if (!Movement.get(self).state().is(BetterParaglidersMovementPlugin.DROWNING)) return;

        self.setSprinting(false);
        self.setSwimming(false);
        self.xxa = 0;
        self.zza = 0;
    }

    @Inject(method = "aiStep", at = @At("TAIL"), remap = false)
    private void betterparagliders$drowningApplySink(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) (Object) this;
        if (self.isCreative() || self.isSpectator()) return;
        if (!Movement.get(self).state().is(BetterParaglidersMovementPlugin.DROWNING)) return;

        double sinkSpeed = BetterParaglidersConfig.drowningSinkSpeed();
        self.setDeltaMovement(0, -sinkSpeed, 0);
    }
}