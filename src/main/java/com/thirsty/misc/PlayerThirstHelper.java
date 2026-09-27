package com.thirsty.misc;

import com.thirsty.config.CommonConfig;
import com.thirsty.purity.WaterPurity;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class PlayerThirstHelper {
    public static void drink(ItemStack item, Player player) {
        if(ThirstHelper.itemRestoresThirst(item) && WaterPurity.givePurityEffects(player, item)) {
            drink(player, ThirstHelper.getThirst(item), ThirstHelper.getQuenched(item));
        }
    }

    public static void drink(Player player, int thirst, int quenched)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        int extra_quenched = Math.max(thirstData.getThirst() + thirst - 20, 0);
        if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().EXTRA_HYDRATION_CONVERT_TO_QUENCHED)
            extra_quenched = 0;
        thirstData.setThirst(Math.min(thirstData.getThirst() + thirst, 20));
        thirstData.setQuenched(Math.min(thirstData.getThirst() + quenched + extra_quenched, thirstData.getThirst()));
    }
}