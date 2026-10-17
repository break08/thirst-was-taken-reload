package com.thirsty.api.emi;

import com.thirsty.ThirstWasTaken;
import com.thirsty.api.thirst_api.jei_emi_compat.RecipePurityHelper;
import com.thirsty.item.ItemInit;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.core.component.DataComponents;

public class ThirstEMI implements EmiPlugin {
    public static final EmiStack WORKSTATION_FURNACE = EmiStack.of(Items.FURNACE);
    public static final EmiRecipeCategory SMELTING_PURIFY_CATEGORY =
            new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "purify_smelting"), WORKSTATION_FURNACE, WORKSTATION_FURNACE);
    public static final EmiStack WORKSTATION_CAMPFIRE = EmiStack.of(Items.CAMPFIRE);
    public static final EmiRecipeCategory CAMP_PURIFY_CATEGORY =
            new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "purify_campfire_cooking"), WORKSTATION_CAMPFIRE, WORKSTATION_CAMPFIRE);

    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(SMELTING_PURIFY_CATEGORY);
        emiRegistry.addWorkstation(SMELTING_PURIFY_CATEGORY, WORKSTATION_FURNACE);
        emiRegistry.addCategory(CAMP_PURIFY_CATEGORY);
        emiRegistry.addWorkstation(CAMP_PURIFY_CATEGORY, WORKSTATION_CAMPFIRE);

        ItemStack base = new ItemStack(Items.POTION);
        base.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));

        ItemStack bucket = new ItemStack(Items.WATER_BUCKET);

        ItemStack terra = new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL);

        // Furnace
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/0_to_2_bottle"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(0, base)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, base))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/1_to_3_bottle"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, base)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, base))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/2_to_3_bottle"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, base)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, base))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/0_to_2_bucket"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(0, bucket)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, bucket))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/1_to_3_bucket"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, bucket)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, bucket))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/2_to_3_bucket"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, bucket)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, bucket))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/0_to_2_terra"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(0, terra)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, terra))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/1_to_3_terra"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, terra)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, terra))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipe(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/2_to_3_terra"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, terra)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, terra))));

        // Campfire

        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/0_to_1_bottlec"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(0, base)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, base))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/1_to_2_bottlec"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, base)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, base))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/2_to_3_bottlec"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, base)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, base))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/0_to_1_bucketc"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(0, bucket)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, bucket))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/1_to_2_bucketc"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, bucket)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, bucket))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/2_to_3_bucketc"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, bucket)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, bucket))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/0_to_1_terrac"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(0, terra)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, terra))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/1_to_2_terrac"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(1, terra)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, terra))));
        emiRegistry.addRecipe(new ThirstWithEMIRecipeCampfire(
                ResourceLocation.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "/purify/2_to_3_terrac"),
                EmiStack.of(RecipePurityHelper.stackWithPurity(2, terra)),
                EmiStack.of(RecipePurityHelper.stackWithPurity(3, terra))));
    }
}
