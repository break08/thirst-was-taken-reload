package com.thirsty.api.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name="thirst_item")
public class ItemSettingsConfig implements ConfigData {
    public static List<String> DRINKS = List.of(
                    "minecraft:potion,6,8",
                    "thirst:terracotta_water_bowl,4,5",
                    "create:builders_tea,12,22",
                    "farmersdelight:apple_cider,8,13",
                    "farmersdelight:melon_juice,8,13",
                    "toughasnails:dirty_water_bottle,6,8",
                    "toughasnails:purified_water_bottle,8,10",
                    "toughasnails:leather_dirty_water_canteen,8,10",
                    "toughasnails:leather_water_canteen,9,11",
                    "toughasnails:leather_purified_water_canteen,10,12",
                    "toughasnails:copper_dirty_water_canteen,8,10",
                    "toughasnails:copper_water_canteen,9,11",
                    "toughasnails:copper_purified_water_canteen,10,12",
                    "toughasnails:iron_dirty_water_canteen,8,10",
                    "toughasnails:iron_water_canteen,9,11",
                    "toughasnails:iron_purified_water_canteen,10,12",
                    "toughasnails:gold_dirty_water_canteen,8,10",
                    "toughasnails:gold_water_canteen,9,11",
                    "toughasnails:gold_purified_water_canteen,10,12",
                    "toughasnails:diamond_dirty_water_canteen,8,10",
                    "toughasnails:diamond_water_canteen,9,11",
                    "toughasnails:diamond_purified_water_canteen,10,12",
                    "toughasnails:netherite_dirty_water_canteen,8,10",
                    "toughasnails:netherite_water_canteen,9,11",
                    "toughasnails:netherite_purified_water_canteen,10,12",
                    "toughasnails:melon_juice,8,13",
                    "toughasnails:apple_juice,8,13",
                    "toughasnails:cactus_juice,8,13",
                    "toughasnails:carrot_juice,8,13",
                    "toughasnails:glow_berry_juice,8,13",
                    "toughasnails:chorus_fruit_juice,8,13",
                    "toughasnails:suspicious_water_cup,8,13",
                    "toughasnails:pumpkin_juice,8,13",
                    "toughasnails:sweet_berry_juice,8,13",
                    "toughasnails:ice_cream,6,12"
    );

    public static List<String> FOODS = List.of(
                    "minecraft:apple,2,3",
                    "minecraft:golden_apple,2,3",
                    "minecraft:enchanted_golden_apple,2,3",
                    "minecraft:melon_slice,4,5",
                    "minecraft:carrot,1,2",
                    "minecraft:mushroom_stew,2,3",
                    "minecraft:rabbit_stew,2,3",
                    "minecraft:beetroot_soup,5,7",
                    "minecraft:beetroot,1,2",
                    "minecraft:sweet_berries,1,2",
                    "minecraft:glow_berries,1,2",
                    "minecraft:golden_carrot,1,2",
                    "farmersdelight:pumpkin_slice,2,1",
                    "farmersdelight:cabbage_leaf,1,2",
                    "farmersdelight:melon_popsicle,7,9",
                    "farmersdelight:fruit_salad,6,8",
                    "farmersdelight:tomato_sauce,4,5",
                    "farmersdelight:mixed_salad,4,5",
                    "farmersdelight:beef_stew,4,5",
                    "farmersdelight:chicken_soup,4,5",
                    "farmersdelight:vegetable_soup,4,5",
                    "farmersdelight:fish_stew,4,5",
                    "farmersdelight:pumpkin_soup,4,5",
                    "farmersdelight:baked_cod_stew,4,5",
                    "farmersdelight:noodle_soup,4,5"
    );

    public static List<String> ITEMS_BLACKLIST = List.of();

}
