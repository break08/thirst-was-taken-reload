package com.thirsty.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class PlayerThirstSyncMessage
{
    public int thirst;
    public int quenched;
    public float exhaustion;
    public boolean enable;

    public PlayerThirstSyncMessage(int thirst, int quenched, float exhaustion,boolean enable)
    {
        this.thirst = thirst;
        this.quenched = quenched;
        this.exhaustion = exhaustion;
        this.enable = enable;
    }

    public PlayerThirstSyncMessage(boolean enable)
    {
        this.enable = enable;
    }

    public static void encode(PlayerThirstSyncMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.thirst);
        buffer.writeInt(message.quenched);
        buffer.writeFloat(message.exhaustion);
        buffer.writeBoolean(message.enable);
    }

    public static PlayerThirstSyncMessage decode(FriendlyByteBuf buffer)
    {
        return new PlayerThirstSyncMessage(buffer.readInt(), buffer.readInt(), buffer.readFloat(),buffer.readBoolean());
    }

    public static final ResourceLocation S2C_CUSTOM_PACKET = new ResourceLocation("thirst", "client_thirst_receive");

    public static void handle(PlayerThirstSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        ClientPlayNetworking.registerGlobalReceiver(S2C_CUSTOM_PACKET, (client, handler, buf, responseSender) -> {

            if (context.getDirection().getReceptionSide().isClient()) {
                context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientThirstSyncMessage.handlePacket(message)));
            }

        });

    }
}
