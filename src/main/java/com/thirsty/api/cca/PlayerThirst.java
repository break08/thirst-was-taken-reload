package com.thirsty.api.cca;

import com.thirsty.thirst.ThirstData;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.resources.ResourceLocation;

public class PlayerThirst {
    public static final ComponentKey<ThirstData> PLAYER_THIRST =
            ComponentRegistry.getOrCreate(
                    new ResourceLocation("thirst", "player_thirst"),
                    ThirstData.class
            );
}
