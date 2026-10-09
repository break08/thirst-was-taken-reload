package com.thirsty.api.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class ContainerConfig{
    public static List<String> CONTAINER = Arrays.asList(
            "collectorsreap:pomegranate_black_tea",
            "collectorsreap:lime_green_tea",
            "create:builders_tea"
    );

    public static Screen containerConfig(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("thirst.container_config.title"));

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("thirst.container_config.general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startStrList(Component.translatable("thirst.container_config.container"), CONTAINER)
                .setDefaultValue(CONTAINER) // Recommended: Used when user click "Reset"
                .setTooltip(Component.literal("Items that are considered as water container")) // Optional: Shown when the user hover over this option
                .setSaveConsumer(newValue -> CONTAINER = newValue) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        builder.setSavingRunnable(() -> {
            // Serialise the config into the config file. This will be called last after all variables are updated.
        });

        return builder.build();
    }
}
