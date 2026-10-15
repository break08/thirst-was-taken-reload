package com.thirsty.api.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.Arrays;
import java.util.List;

@Config(name = "thirst_container")
public class ContainerConfig implements ConfigData {
    @Comment("Define drinks will be influenced by purity")
    public List<String> CONTAINER = Arrays.asList(
            "collectorsreap:pomegranate_black_tea",
            "collectorsreap:lime_green_tea",
            "create:builders_tea"
    );
}
