package com.thirsty.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;

public class DrinkByHandData {
    public static void sendC2SPacket(BlockPos blockPos) {
        ClientPlayNetworking.send(new ThirstPayload(blockPos));
    }
}
