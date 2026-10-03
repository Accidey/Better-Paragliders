package com.xulai.betterparagliders.network;

import com.xulai.betterparagliders.BetterParaglidersMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SwingActionMsg(ResourceLocation itemId) implements CustomPacketPayload {

    public static final Type<SwingActionMsg> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(BetterParaglidersMod.MOD_ID, "swing_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SwingActionMsg> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SwingActionMsg::itemId,
            SwingActionMsg::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
