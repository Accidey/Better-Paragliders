package com.xulai.betterparagliders.mixins.paragliders.movement;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.plugin.BetterParaglidersMovementPlugin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.PlayerState;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.impl.movement.ServerPlayerMovement;

@Mixin(ServerPlayerMovement.class)
public abstract class ServerPlayerMovementMixin {

    @Inject(method = "updateStamina", at = @At("TAIL"), remap = false)
    private void betterparagliders$clearDepletedAsSoonAsStaminaReturns(CallbackInfo ci) {
        ServerPlayerMovement self = (ServerPlayerMovement) (Object) this;
        Stamina stamina = self.stamina();

        if (!stamina.isDepleted()) return;
        if (stamina.stamina() + stamina.extraStamina() <= 0) return;

        stamina.setDepleted(false, true);
        stamina.setDirty(true);
    }

    @Inject(method = "applyMovement", at = @At("TAIL"), remap = false)
    private void betterparagliders$applyDrowning(CallbackInfo ci) {
        ServerPlayerMovement self = (ServerPlayerMovement) (Object) this;
        PlayerState state = self.state();
        if (!state.is(BetterParaglidersMovementPlugin.DROWNING)) return;

        Player player = self.player();
        Stamina stamina = self.stamina();

        if (stamina.stamina() > 0) {
            stamina.setStamina(0, true);
            stamina.setDirty(true);
        }
        if (stamina.extraStamina() > 0) {
            stamina.setExtraStamina(0, true);
            stamina.setDirty(true);
        }
        if (!stamina.isDepleted()) {
            stamina.setDepleted(true, true);
            stamina.setDirty(true);
        }

        player.setSprinting(false);
        player.setSwimming(false);
        player.xxa = 0;
        player.zza = 0;

        double sinkSpeed = BetterParaglidersConfig.drowningSinkSpeed();
        player.setDeltaMovement(0, -sinkSpeed, 0);
    }
}