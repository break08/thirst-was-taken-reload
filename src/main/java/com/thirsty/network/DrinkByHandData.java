package com.thirsty.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class DrinkByHandData {
    public static void sendC2SPacket(BlockPos blockPos) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(blockPos);

        ClientPlayNetworking.send(ThirstNetwork.MOD_PACKET_ID, buf);
    }
}
