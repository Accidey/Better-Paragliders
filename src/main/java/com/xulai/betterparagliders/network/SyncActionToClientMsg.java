package com.xulai.betterparagliders.network;

import com.xulai.betterparagliders.BetterParaglidersMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SyncActionToClientMsg(int totalActionStaminaCost) implements CustomPacketPayload {

    public static final Type<SyncActionToClientMsg> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(BetterParaglidersMod.MOD_ID, "sync_action_stamina_cost"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncActionToClientMsg> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncActionToClientMsg::totalActionStaminaCost,
            SyncActionToClientMsg::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
