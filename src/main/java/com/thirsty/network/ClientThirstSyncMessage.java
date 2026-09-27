package com.thirsty.network;

import com.thirsty.thirst.ThirstData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class ClientThirstSyncMessage
{
    public static void handlePacket(PlayerThirstSyncMessage message)
    {
        Player player = Minecraft.getInstance().player;

        if (player != null)
        {
            ThirstData cap = PLAYER_THIRST.get(player);
            cap.setThirst(message.thirst);
            cap.setQuenched(message.quenched);
            cap.setExhaustion(message.exhaustion);
            cap.setShouldTickThirst(message.enable);

        }
    }
}