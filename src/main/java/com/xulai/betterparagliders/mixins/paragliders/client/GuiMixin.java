package com.xulai.betterparagliders.mixins.paragliders.client;

import com.xulai.betterparagliders.plugin.BetterParaglidersMovementPlugin;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.stamina.Stamina;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "render", at = @At("TAIL"), remap = false)
    private void betterparagliders$renderDrowningOverlay(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui) return;
        Player player = minecraft.player;
        if (player == null) return;
        if (player.isCreative() || player.isSpectator()) return;

        if (!Movement.get(player).state().is(BetterParaglidersMovementPlugin.DROWNING)) return;

        Stamina stamina = Stamina.get(player);
        if (stamina.stamina() + stamina.extraStamina() > 0) return;

        if (player.getAirSupply() > 0) return;

        float maxHealth = player.getMaxHealth();
        float health = player.getHealth();
        if (maxHealth <= 0) return;

        float ratio = Math.max(0, Math.min(1, health / maxHealth));

        int alpha;
        if (ratio <= 0.1f) {
            alpha = 255;
        } else {
            alpha = (int) ((1.0f - ratio) / 0.9f * 255);
        }
        if (alpha <= 0) return;

        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();

        graphics.fill(0, 0, width, height, (alpha << 24) | 0x000000);
    }
}