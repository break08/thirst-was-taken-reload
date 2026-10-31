package com.thirsty;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
                addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft", registries);

                // farmer's delight refabricated compat
                if (FabricLoader.getInstance().isModLoaded("farmersdelight")) {
                    addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft_fr", registries);
                }
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/abandoned_mineshaft_bc", registries);
                }

            }

            // bastion
            if (target.equals(Identifier.withDefaultNamespace("chests/bastion_other"))) {
                addPool(tableBuilder, "chests_loot_modifier/bastion_other", registries);

                // b&c
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/bastion_other_bc", registries);
                }
            }

            // nether bridge
            if (target.equals(Identifier.withDefaultNamespace("chests/nether_bridge"))) {
                addPool(tableBuilder, "chests_loot_modifier/nether_bridge", registries);
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/nether_bridge_bc", registries);
                }
            }

            // shipwreck
            if (target.equals(Identifier.withDefaultNamespace("chests/shipwreck_supply"))) {
                addPool(tableBuilder, "chests_loot_modifier/shipwreck_supply", registries);
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/shipwreck_supply_bc", registries);
                }
            }

            // simple dungeon
            if (target.equals(Identifier.withDefaultNamespace("chests/simple_dungeon"))) {
                addPool(tableBuilder, "chests_loot_modifier/simple_dungeon", registries);
                if (FabricLoader.getInstance().isModLoaded("brewinandchewin")) {
                    addPool(tableBuilder, "chests_loot_modifier/simple_dungeon_bc", registries);
                }
            }
        });
    }

    private static void addPool(LootTable.Builder tableBuilder, String path, HolderLookup.Provider registries) {
        ResourceKey<LootTable> ref = ResourceKey.create(Registries.LOOT_TABLE,
                Identifier.fromNamespaceAndPath(ID, path));
        Holder<LootTable> holder = registries.lookupOrThrow(Registries.LOOT_TABLE).getOrThrow(ref);
        tableBuilder.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(holder)));
    }
}