package com.thirsty.item;

import com.thirsty.ThirstWasTaken;
import com.thirsty.api.create.CreateRegistry;
import com.thirsty.purity.WaterPurity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import static com.thirsty.item.ThirstTab.THIRST_CREATIVE_TAB;
import static com.thirsty.item.ThirstTab.THIRST_CREATIVE_TAB_KEY;

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
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, THIRST_CREATIVE_TAB_KEY, THIRST_CREATIVE_TAB);
        ItemGroupEvents.modifyEntriesEvent(THIRST_CREATIVE_TAB_KEY).register(itemGroup -> {
            itemGroup.accept((WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 0)));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 1));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 2));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 3));
            itemGroup.accept(WaterPurity.addPurity(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 0));
            itemGroup.accept(WaterPurity.addPurity(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 1));
            itemGroup.accept(WaterPurity.addPurity(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 2));
            itemGroup.accept(WaterPurity.addPurity(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 3));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 0));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 1));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 2));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 3));
            itemGroup.accept(ItemInit.CLAY_BOWL.getDefaultInstance());
            itemGroup.accept(ItemInit.TERRACOTTA_BOWL.getDefaultInstance());
            if (FabricLoader.getInstance().isModLoaded("create")){
                itemGroup.accept(CreateRegistry.SAND_FILTER_BLOCK.get());
            }
        });
    }
}
