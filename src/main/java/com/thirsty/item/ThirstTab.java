package com.thirsty.item;

import com.thirsty.ThirstWasTaken;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ThirstTab {
    public static final ResourceKey<CreativeModeTab> THIRST_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), new ResourceLocation(ThirstWasTaken.MOD_ID, "item_group"));
    public static final CreativeModeTab THIRST_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() ->
                    new ItemStack(Items.WATER_BUCKET)
            )
            .title(Component.translatable("itemGroup.thirst"))
            .build();
}
