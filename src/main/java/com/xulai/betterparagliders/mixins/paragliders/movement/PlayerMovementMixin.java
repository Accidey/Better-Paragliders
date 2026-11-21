package com.xulai.betterparagliders.mixins.paragliders.movement;

import com.xulai.betterparagliders.capabilities.StaminaOverride;
import com.xulai.betterparagliders.utils.CalculateStaminaUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.PlayerState;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.impl.movement.PlayerMovement;

@Mixin(PlayerMovement.class)
public abstract class PlayerMovementMixin {

    @Shadow
    protected abstract boolean isRemote();

    @Inject(method = "updateStamina", at = @At("HEAD"), cancellable = true, remap = false)
    private void betterparagliders$updateStamina(CallbackInfo ci) {
        PlayerMovement self = (PlayerMovement) (Object) this;
        boolean remote = this.isRemote();
        Stamina stamina = self.stamina();

        if (!stamina.updateWithDefaultLogic(remote)) {
            ci.cancel();
            return;
        }

        PlayerState state = self.state();
        int recoveryDelay = self.recoveryDelay();
        int newRecoveryDelay = recoveryDelay;
        double delta = CalculateStaminaUtils.getModifiedStaminaDelta(self);

        if (delta < 0) {
            if (!stamina.isDepleted()) stamina.takeStamina(-delta, false, false, true);
            if (CalculateStaminaUtils.staminaEmpty(stamina)) {
                newRecoveryDelay = Math.max(newRecoveryDelay, CalculateStaminaUtils.DEPLETION_RECOVERY_DELAY_TICKS);
            }
        } else {
            if (recoveryDelay > 0) newRecoveryDelay--;
            else if (delta > 0) {
                double given = stamina.giveStamina(delta, false, true);
                if (!remote) CalculateStaminaUtils.chargeRecoveryExhaustion(self.player(), given);
            }
        }

        if (stamina instanceof StaminaOverride override) {
            int pending = override.getTotalActionStaminaCost();
            if (pending > 0) override.setTotalActionStaminaCost(pending - 1);
            else if (pending < 0) override.setTotalActionStaminaCost(pending + 1);
        }

        newRecoveryDelay = Math.max(0, Math.max(newRecoveryDelay, state.recoveryDelay()));
        if (recoveryDelay != newRecoveryDelay) self.setRecoveryDelay(newRecoveryDelay);

        ci.cancel();
    }
}
