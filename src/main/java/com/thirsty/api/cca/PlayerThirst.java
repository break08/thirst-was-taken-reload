package com.thirsty.api.cca;

import com.thirsty.thirst.ThirstData;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class PlayerThirst {
    public static final ComponentKey<ThirstData> PLAYER_THIRST =
            ComponentRegistry.getOrCreate(
                    ResourceLocation.fromNamespaceAndPath("thirst", "player_thirst"),
                    ThirstData.class
            );
}
