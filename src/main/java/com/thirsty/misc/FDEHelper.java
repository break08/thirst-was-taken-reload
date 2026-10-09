package com.thirsty.misc;

import net.minecraft.world.entity.player.Player;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FDEHelper {
    private FDEHelper() {}

    public static boolean hasNourishment(Player player) {
        return player.hasEffect(ModEffects.NOURISHMENT.get());
    }
}
