package com.thirsty.misc;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import toughasnails.api.item.TANItems;
import toughasnails.api.potion.TANEffects;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.thirsty.purity.WaterPurity.MIN_PURITY;

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

    public static boolean isToughAsNailsDirtyWater(ItemStack item)
    {
        return item.is(TANItems.DIRTY_WATER_BOTTLE) ||
                item.is(TANItems.LEATHER_DIRTY_WATER_CANTEEN) ||
                item.is(TANItems.COPPER_DIRTY_WATER_CANTEEN) ||
                item.is(TANItems.IRON_DIRTY_WATER_CANTEEN) ||
                item.is(TANItems.GOLD_DIRTY_WATER_CANTEEN) ||
                item.is(TANItems.DIAMOND_DIRTY_WATER_CANTEEN) ||
                item.is(TANItems.NETHERITE_DIRTY_WATER_CANTEEN);
    }

    private static boolean isToughAsNailsWater(ItemStack item)
    {
        return item.is(TANItems.LEATHER_WATER_CANTEEN) ||
                item.is(TANItems.COPPER_WATER_CANTEEN) ||
                item.is(TANItems.IRON_WATER_CANTEEN) ||
                item.is(TANItems.GOLD_WATER_CANTEEN) ||
                item.is(TANItems.DIAMOND_WATER_CANTEEN) ||
                item.is(TANItems.NETHERITE_WATER_CANTEEN);
    }

    private static boolean isToughAsNailsPurifiedDrink(ItemStack item)
    {
        return item.is(TANItems.PURIFIED_WATER_BOTTLE) ||
                item.is(TANItems.LEATHER_PURIFIED_WATER_CANTEEN) ||
                item.is(TANItems.COPPER_PURIFIED_WATER_CANTEEN) ||
                item.is(TANItems.IRON_PURIFIED_WATER_CANTEEN) ||
                item.is(TANItems.GOLD_PURIFIED_WATER_CANTEEN) ||
                item.is(TANItems.DIAMOND_PURIFIED_WATER_CANTEEN) ||
                item.is(TANItems.NETHERITE_PURIFIED_WATER_CANTEEN) ||
                item.is(TANItems.APPLE_JUICE) ||
                item.is(TANItems.CACTUS_JUICE) ||
                item.is(TANItems.CHORUS_FRUIT_JUICE) ||
                item.is(TANItems.GLOW_BERRY_JUICE) ||
                item.is(TANItems.MELON_JUICE) ||
                item.is(TANItems.PUMPKIN_JUICE) ||
                item.is(TANItems.SWEET_BERRY_JUICE);
    }

    public static int getToughAsNailsPurity(ItemStack item)
    {
        if(isToughAsNailsDirtyWater(item))
            return MIN_PURITY;

        if(isToughAsNailsWater(item))
            return 2;

        if(isToughAsNailsPurifiedDrink(item))
            return 3;

        return -1;
    }
}
