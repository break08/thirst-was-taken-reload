package com.thirsty;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public class ThirstLootModifier {
    public static String ID = ThirstWasTaken.MOD_ID;

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            ResourceLocation target = key.location();

            // mineshaft
            if (target.equals(ResourceLocation.withDefaultNamespace("chests/abandoned_mineshaft"))) {
                addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft");

                // farmer's delight refabricated compat
                if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
                    addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft_fr");
                }
            }

            // bastion
            if (target.equals(ResourceLocation.withDefaultNamespace("chests/bastion_other"))) {
                addPool(tableBuilder, "chests_loot_modifier/bastion_other");
            }

            // nether bridge
            if (target.equals(ResourceLocation.withDefaultNamespace("chests/nether_bridge"))) {
                addPool(tableBuilder, "chests_loot_modifier/nether_bridge");
            }

            // shipwreck
            if (target.equals(ResourceLocation.withDefaultNamespace("chests/shipwreck_supply"))) {
                addPool(tableBuilder, "chests_loot_modifier/shipwreck_supply");
            }

            // simple dungeon
            if (target.equals(ResourceLocation.withDefaultNamespace("chests/simple_dungeon"))) {
                addPool(tableBuilder, "chests_loot_modifier/simple_dungeon");
            }
        });
    }

    private static void addPool(LootTable.Builder tableBuilder, String path) {
        ResourceKey<LootTable> ref = ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(ID, path));
        tableBuilder.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(ref)));
    }
}