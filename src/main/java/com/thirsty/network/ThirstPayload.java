package com.thirsty.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ThirstPayload(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ThirstPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("thirst", "hand-drink"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ThirstPayload> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ThirstPayload::pos,
                    ThirstPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}