package com.thirsty.item;

import com.thirsty.ThirstWasTaken;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ThirstTab {
    public static final ResourceKey<CreativeModeTab> THIRST_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "item_group"));
    public static final CreativeModeTab THIRST_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() ->
                    new ItemStack(Items.WATER_BUCKET)
            )
            .title(Component.translatable("itemGroup.thirst"))
            .build();
}
