package com.thirsty.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = "thirst")
@Config.Gui.Background("minecraft:textures/block/stone")
public class CommonConfig implements ConfigData {
    @Comment("How much faster is hydration depletion when players with fire resistance(Range 0 to 100, 0 means not to depletion,100 means depletion like normal)")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int FIRE_RESISTANCE_DEHYDRATION = 50;

    @Comment("How much is hydration depletion in nether faster than overworld (Must be set smaller than 5.0 and bigger 3.0, we can not prevent you from modifying this value out of this range, so be careful, this can break gameplay of this mod)")
    public double NETHER_THIRST_DEPLETION_MODIFIER = 3.0D;

    @Comment("How much faster is hydration depletion relative to hunger (1 means they will deplete at the same speed)")
    public float THIRST_DEPLETION_MODIFIER = 1.2F;

    @Comment("Whether extra hydration will convert to quenched")
    public boolean EXTRA_HYDRATION_CONVERT_TO_QUENCHED = true;

    @Comment("Purity for drinks that normally have purity but for whatever reason don't have a value set")
    public int DEFAULT_PURITY = 2;

    @Comment("% of getting sick (hunger and nausea) after drinking dirty water")
    public int DIRTY_NAUSEA_PERCENTAGE = 100;

    @Comment ("% of getting sick (hunger and nausea) after drinking slightly dirty water")
    public int SLIGHTLY_DIRTY_NAUSEA_PERCENTAGE = 50;

    @Comment("% of getting sick (hunger and nausea) after drinking acceptable water")
    public int ACCEPTABLE_NAUSEA_PERCENTAGE = 5;

    @Comment("% of getting sick (hunger and nausea) after drinking purified water")
    public int PURIFIED_NAUSEA_PERCENTAGE = 0;

    @Comment("% of getting poisoned after drinking dirty water")
    public int DIRTY_POISON_PERCENTAGE = 30;

    @Comment("% of getting poisoned after drinking slightly dirty water")
    public int SLIGHTLY_DIRTY_POISON_PERCENTAGE = 10;

    @Comment("% of getting poisoned after drinking acceptable water")
    public int ACCEPTABLE_POISON_PERCENTAGE = 0;

    @Comment("% of getting poisoned after drinking purified water")
    public int PURIFIED_POISON_PERCENTAGE = 0;

    @Comment("Y level above which water has 1 more level of purification by default (i.e Mountains)")
    public int MOUNTAINS_Y = 100;

    @Comment("Y level below which water has 1 more level of purification by default (i.e Caves) (for aquatic biomes, this number will be decreased by 32)")
    public int CAVES_Y = 48;

    @Comment("How many levels of purification does running water have compared to still water")
    public int RUNNING_WATER_PURIFICATION_AMOUNT = 1;

    @Comment()
    public boolean QUENCH_THIRST_WHEN_DEBUFFED = true;

    public List<List<?>> DRINKS = new ArrayList<>(
            List.of(
                    Arrays.asList("minecraft:potion", 6, 8),
                    Arrays.asList("thirst:terracotta_water_bowl", 4, 5),
                    Arrays.asList("create:builders_tea", 12, 22),
                    Arrays.asList("farmersdelight:apple_cider", 8, 13),
                    Arrays.asList("farmersdelight:melon_juice", 8, 13),
                    Arrays.asList("toughasnails:dirty_water_bottle", 6, 8),
                    Arrays.asList("toughasnails:purified_water_bottle", 8, 10),
                    Arrays.asList("toughasnails:leather_dirty_water_canteen", 8, 10),
                    Arrays.asList("toughasnails:leather_water_canteen", 9, 11),
                    Arrays.asList("toughasnails:leather_purified_water_canteen", 10, 12),
                    Arrays.asList("toughasnails:copper_dirty_water_canteen", 8, 10),
                    Arrays.asList("toughasnails:copper_water_canteen", 9, 11),
                    Arrays.asList("toughasnails:copper_purified_water_canteen", 10, 12),
                    Arrays.asList("toughasnails:iron_dirty_water_canteen", 8, 10),
                    Arrays.asList("toughasnails:iron_water_canteen", 9, 11),
                    Arrays.asList("toughasnails:iron_purified_water_canteen", 10, 12),
                    Arrays.asList("toughasnails:gold_dirty_water_canteen", 8, 10),
                    Arrays.asList("toughasnails:gold_water_canteen", 9, 11),
                    Arrays.asList("toughasnails:gold_purified_water_canteen", 10, 12),
                    Arrays.asList("toughasnails:diamond_dirty_water_canteen", 8, 10),
                    Arrays.asList("toughasnails:diamond_water_canteen", 9, 11),
                    Arrays.asList("toughasnails:diamond_purified_water_canteen", 10, 12),
                    Arrays.asList("toughasnails:netherite_dirty_water_canteen", 8, 10),
                    Arrays.asList("toughasnails:netherite_water_canteen", 9, 11),
                    Arrays.asList("toughasnails:netherite_purified_water_canteen", 10, 12),
                    Arrays.asList("toughasnails:melon_juice", 8, 13),
                    Arrays.asList("toughasnails:apple_juice", 8, 13),
                    Arrays.asList("toughasnails:cactus_juice", 8, 13),
                    Arrays.asList("toughasnails:carrot_juice", 8, 13),
                    Arrays.asList("toughasnails:glow_berry_juice", 8, 13),
                    Arrays.asList("toughasnails:chorus_fruit_juice", 8, 13),
                    Arrays.asList("toughasnails:suspicious_water_cup", 8, 13),
                    Arrays.asList("toughasnails:pumpkin_juice", 8, 13),
                    Arrays.asList("toughasnails:sweet_berry_juice", 8, 13),
                    Arrays.asList("toughasnails:ice_cream", 6, 12)
            )
    );

    public List<List<?>> FOODS = new ArrayList<>(
            List.of(
                    Arrays.asList("minecraft:apple", 2, 3),
                    Arrays.asList("minecraft:golden_apple", 2, 3),
                    Arrays.asList("minecraft:enchanted_golden_apple", 2, 3),
                    Arrays.asList("minecraft:melon_slice", 4, 5),
                    Arrays.asList("minecraft:carrot", 1, 2),
                    Arrays.asList("minecraft:mushroom_stew", 2, 3),
                    Arrays.asList("minecraft:rabbit_stew", 2, 3),
                    Arrays.asList("minecraft:beetroot_soup", 5, 7),
                    Arrays.asList("minecraft:beetroot", 1, 2),
                    Arrays.asList("minecraft:sweet_berries", 1, 2),
                    Arrays.asList("minecraft:glow_berries", 1, 2),
                    Arrays.asList("minecraft:golden_carrot", 1, 2),
                    Arrays.asList("farmersdelight:pumpkin_slice", 2, 1),
                    Arrays.asList("farmersdelight:cabbage_leaf", 1, 2),
                    Arrays.asList("farmersdelight:melon_popsicle", 7, 9),
                    Arrays.asList("farmersdelight:fruit_salad", 6, 8),
                    Arrays.asList("farmersdelight:tomato_sauce", 4, 5),
                    Arrays.asList("farmersdelight:mixed_salad", 4, 5),
                    Arrays.asList("farmersdelight:beef_stew", 4, 5),
                    Arrays.asList("farmersdelight:chicken_soup", 4, 5),
                    Arrays.asList("farmersdelight:vegetable_soup", 4, 5),
                    Arrays.asList("farmersdelight:fish_stew", 4, 5),
                    Arrays.asList("farmersdelight:pumpkin_soup", 4, 5),
                    Arrays.asList("farmersdelight:baked_cod_stew", 4, 5),
                    Arrays.asList("farmersdelight:noodle_soup", 4, 5)
            )
    );
}
