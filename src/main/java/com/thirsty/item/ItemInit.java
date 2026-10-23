package com.thirsty.item;

import com.thirsty.ThirstWasTaken;
import com.thirsty.purity.WaterPurity;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.function.Function;

import static com.thirsty.item.ThirstTab.THIRST_CREATIVE_TAB;
import static com.thirsty.item.ThirstTab.THIRST_CREATIVE_TAB_KEY;

public class ItemInit {

    public static final Item CLAY_BOWL = register("clay_bowl", Item::new,
            new Item.Properties().stacksTo(64));

    public static final Item TERRACOTTA_BOWL = register("terracotta_bowl", Item::new,
            new Item.Properties().stacksTo(64));

    public static final Item TERRACOTTA_WATER_BOWL = register("terracotta_water_bowl",
            props -> new DrinkableItem(props).setContainer(TERRACOTTA_BOWL),
            new Item.Properties());

    public static final Item porridge_test = register("porridge_thirst_test_item", Item::new,
            new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEdible().nutrition(1).saturationModifier(1).build()));

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        return register(create(name), factory, settings);
    }

    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, ThirstWasTaken.id(name));
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize(){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, THIRST_CREATIVE_TAB_KEY, THIRST_CREATIVE_TAB);
        CreativeModeTabEvents.modifyOutputEvent(THIRST_CREATIVE_TAB_KEY).register(itemGroup -> {
            itemGroup.accept((WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 0)));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 1));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 2));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(Items.WATER_BUCKET), 3));
            itemGroup.accept(WaterPurity.addPurity(PotionContents.createItemStack(Items.POTION, Potions.WATER), 0));
            itemGroup.accept(WaterPurity.addPurity(PotionContents.createItemStack(Items.POTION, Potions.WATER), 1));
            itemGroup.accept(WaterPurity.addPurity(PotionContents.createItemStack(Items.POTION, Potions.WATER), 2));
            itemGroup.accept(WaterPurity.addPurity(PotionContents.createItemStack(Items.POTION, Potions.WATER), 3));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 0));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 1));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 2));
            itemGroup.accept(WaterPurity.addPurity(new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL), 3));
            itemGroup.accept(ItemInit.CLAY_BOWL.getDefaultInstance());
            itemGroup.accept(ItemInit.TERRACOTTA_BOWL.getDefaultInstance());
        });
    }
}
