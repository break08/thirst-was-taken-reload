package com.thirsty;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

public class ThirstLootModifier {
    public static String ID = ThirstWasTaken.MOD_ID;
    public static void init(){
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            // mineshaft
            if (id.equals(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(
                                new ResourceLocation(ID, "chests_loot_modifier/abandoned_mineshaft"))));
            }

            // Bastion
            if (id.equals(new ResourceLocation("minecraft", "chests/bastion_other"))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(
                                new ResourceLocation(ID, "chests_loot_modifier/bastion_other"))));
            }

            // nether bridge
            if (id.equals(new ResourceLocation("minecraft", "chests/nether_bridge"))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(
                                new ResourceLocation(ID, "chests_loot_modifier/nether_bridge"))));
            }

            // shipwreck
            if (id.equals(new ResourceLocation("minecraft", "chests/shipwreck_supply"))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(
                                new ResourceLocation(ID, "chests_loot_modifier/shipwreck_supply"))));
            }

            // simple dungeon
            if (id.equals(new ResourceLocation("minecraft", "chests/simple_dungeon"))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(
                                new ResourceLocation(ID, "chests_loot_modifier/simple_dungeon"))));
            }

            // farmer's delight refabricated compat
            if (FabricLoader.getInstance().isModLoaded("farmersdelight")){
                // mineshaft
                if (id.equals(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"))) {
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootTableReference.lootTableReference(
                                    new ResourceLocation(ID, "chests_loot_modifier/abandoned_mineshaft_fr"))));
                }

                // Bastion
                if (id.equals(new ResourceLocation("minecraft", "chests/bastion_other"))) {
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootTableReference.lootTableReference(
                                    new ResourceLocation(ID, "chests_loot_modifier/bastion_other_fr"))));
                }

                // nether bridge
                if (id.equals(new ResourceLocation("minecraft", "chests/nether_bridge"))) {
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootTableReference.lootTableReference(
                                    new ResourceLocation(ID, "chests_loot_modifier/nether_bridge_fr"))));
                }

                // shipwreck
                if (id.equals(new ResourceLocation("minecraft", "chests/shipwreck_supply"))) {
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootTableReference.lootTableReference(
                                    new ResourceLocation(ID, "chests_loot_modifier/shipwreck_supply_fr"))));
                }

                // simple dungeon
                if (id.equals(new ResourceLocation("minecraft", "chests/simple_dungeon"))) {
                    tableBuilder.withPool(LootPool.lootPool()
                            .add(LootTableReference.lootTableReference(
                                    new ResourceLocation(ID, "chests_loot_modifier/simple_dungeon_fr"))));
                }
            }
        });
    }
}
