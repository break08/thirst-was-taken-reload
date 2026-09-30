package com.thirsty.item;

import com.thirsty.ThirstWasTaken;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemInit {


    public static final Item CLAY_BOWL = register(
            // Ignore the food component for now, we'll cover it later in the food section.
            new Item(new Item.Properties().stacksTo(64)),
            "clay_bowl"
    );

    public static final Item TERRACOTTA_BOWL = register(
            // Ignore the food component for now, we'll cover it later in the food section.
            new Item(new Item.Properties().stacksTo(64)),
            "terracotta_bowl"
    );

    public static final Item TERRACOTTA_WATER_BOWL = register(
            // Ignore the food component for now, we'll cover it later in the food section.
            new DrinkableItem().setContainer(TERRACOTTA_BOWL),
            "terracotta_water_bowl"
    );

    public static Item register(Item item, String id) {

        ResourceLocation itemID = new ResourceLocation(ThirstWasTaken.MOD_ID, id);

        return Registry.register(BuiltInRegistries.ITEM, itemID, item);
    }

    public static void initialize(){

    }
}
