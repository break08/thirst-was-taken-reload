package com.thirsty;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public class ThirstLootModifier {
    public static String ID = ThirstWasTaken.MOD_ID;

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            Identifier target = key.identifier();

            // mineshaft
            if (target.equals(Identifier.withDefaultNamespace("chests/abandoned_mineshaft"))) {
                addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft");

                // farmer's delight refabricated compat
                if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
                    addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft_fr");
                }
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft_bc");
                }

            }

            // bastion
            if (target.equals(Identifier.withDefaultNamespace("chests/bastion_other"))) {
                addPool(tableBuilder, "chests_loot_modifier/bastion_other");

                // b&c
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/bastion_other_bc");
                }
            }

            // nether bridge
            if (target.equals(Identifier.withDefaultNamespace("chests/nether_bridge"))) {
                addPool(tableBuilder, "chests_loot_modifier/nether_bridge");
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/nether_bridge_bc");
                }
            }

            // shipwreck
            if (target.equals(Identifier.withDefaultNamespace("chests/shipwreck_supply"))) {
                addPool(tableBuilder, "chests_loot_modifier/shipwreck_supply");
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/shipwreck_supply_bc");
                }
            }

            // simple dungeon
            if (target.equals(Identifier.withDefaultNamespace("chests/simple_dungeon"))) {
                addPool(tableBuilder, "chests_loot_modifier/simple_dungeon");
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/simple_dungeon_bc");
                }
            }
        });
    }

    private static void addPool(LootTable.Builder tableBuilder, String path) {
        ResourceKey<LootTable> ref = ResourceKey.create(Registries.LOOT_TABLE,
                Identifier.fromNamespaceAndPath(ID, path));
        tableBuilder.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(ref)));
    }
}