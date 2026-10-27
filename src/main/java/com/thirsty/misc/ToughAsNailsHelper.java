package com.thirsty.misc;

import net.minecraft.world.entity.player.Player;
import toughasnails.api.potion.TANEffects;

import java.util.Objects;

public class ToughAsNailsHelper {
    private ToughAsNailsHelper() {}

    public static boolean hasTanThirstEffect(Player player){
        return player.hasEffect(TANEffects.THIRST);
    }
    public static int getTanThirstAmplifier(Player player){
        if (hasTanThirstEffect(player)){
            return Objects.requireNonNull(player.getEffect(TANEffects.THIRST)).getAmplifier();
        }
        return 0;
    }
}
