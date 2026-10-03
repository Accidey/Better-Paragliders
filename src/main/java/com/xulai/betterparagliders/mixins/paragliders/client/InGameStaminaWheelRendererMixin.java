package com.xulai.betterparagliders.mixins.paragliders.client;

import com.xulai.betterparagliders.capabilities.StaminaOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.client.render.InGameStaminaWheelRenderer;

@Mixin(InGameStaminaWheelRenderer.class)
public abstract class InGameStaminaWheelRendererMixin {

    @Redirect(method = "makeWheel", at = @At(value = "INVOKE",
            target = "Ltictim/paraglider/api/movement/Movement;staminaDelta()D"), remap = false)
    private double betterparagliders$includeActionStaminaCost(Movement movement) {
        double delta = movement.staminaDelta();

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return delta;

        if (Stamina.get(player) instanceof StaminaOverride override) {
            int cost = override.getTotalActionStaminaCost();
            if (cost > 0) return delta - cost;
        }
        return delta;
    }
}
