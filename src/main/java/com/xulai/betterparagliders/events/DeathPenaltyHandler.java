package com.xulai.betterparagliders.events;

import com.xulai.betterparagliders.BetterParaglidersMod;
import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import tictim.paraglider.api.vessel.VesselContainer;

@EventBusSubscriber(modid = BetterParaglidersMod.MOD_ID)
public final class DeathPenaltyHandler {

    private DeathPenaltyHandler() {}

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        int loss = BetterParaglidersConfig.deathHeartContainerLoss();
        if (loss <= 0) return;

        VesselContainer vessels = VesselContainer.get(player);
        int taken = vessels.takeHeartContainers(loss, false, true);
        if (taken <= 0) return;
        if (!BetterParaglidersConfig.sendDeathPenaltyMessage()) return;

        player.sendSystemMessage(Component.translatable("message.betterparagliders.death_penalty", taken)
                .withStyle(ChatFormatting.RED));
    }
}