package com.xulai.betterparagliders.network;

import com.xulai.betterparagliders.utils.CalculateStaminaUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNet {

    public static final String NET_VERSION = "1";

    private static volatile int pendingActionStaminaCost;

    private ModNet() {}

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModNet::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NET_VERSION);
        registrar.playToClient(
                SyncActionToClientMsg.TYPE,
                SyncActionToClientMsg.CODEC,
                ModNet::handleActionStaminaCost);
        registrar.playToServer(
                SwingActionMsg.TYPE,
                SwingActionMsg.CODEC,
                ModNet::handleSwingAction);
    }

    private static void handleActionStaminaCost(SyncActionToClientMsg msg, IPayloadContext context) {
        pendingActionStaminaCost = msg.totalActionStaminaCost();
    }

    public static int consumePendingActionStaminaCost() {
        int cost = pendingActionStaminaCost;
        pendingActionStaminaCost = 0;
        return cost;
    }

    private static void handleSwingAction(SwingActionMsg msg, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            CalculateStaminaUtils.chargeSwing(player, msg.itemId());
        }
    }

    public static void sendActionStaminaCost(ServerPlayer player, int totalActionStaminaCost) {
        PacketDistributor.sendToPlayer(player, new SyncActionToClientMsg(totalActionStaminaCost));
    }

    public static void sendSwingAction(ResourceLocation itemId) {
        PacketDistributor.sendToServer(new SwingActionMsg(itemId));
    }
}
