package com.thirsty.api.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "thirst_client")
public class ClientConfig implements ConfigData {
    @Comment("If the purity tooltip should be shown only when the player is pressing the shift key")
    public boolean ONLY_SHOW_PURITY_WHEN_SHIFTING = false;

    @Comment("How many pixels should the thirst bar be shifted vertically from its original position")
    public int THIRST_BAR_Y_OFFSET = 0;

    @Comment("How many pixels should the thirst bar be shifted horizontally from its original position")
    public int THIRST_BAR_X_OFFSET = 0;

    @Comment("Whether players needs two hands available to drink water from source")
    public boolean DRINK_BOTH_HAND_NEEDED = true;
}