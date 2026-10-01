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

    public boolean QUENCH_THIRST_WHEN_DEBUFFED = true;

    public boolean MOVE_SLOW_WHEN_THIRSTY = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    public int WATER_BOTTLE_STACKSIZE = 64;

    public boolean DEHYDRATION_HALTS_HEALTH_REGEN = true;

}
