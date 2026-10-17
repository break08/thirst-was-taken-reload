package com.thirsty.api.jei;

import com.thirsty.api.thirst_api.jei_emi_compat.RecipePurityHelper;
import com.thirsty.item.ItemInit;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.thirsty.ThirstWasTaken.MOD_ID;

public class ThirstJEI implements IModPlugin {
    /**
     * The unique ID for this mod plugin.
     * The namespace should be your mod's modId.
     */
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("thirst", "jei_plugin");
    }

    @SuppressWarnings("unchecked")
    private static <T extends Recipe<?>> RecipeHolder<T> getHolder(Level level, ResourceLocation id) {
        return (RecipeHolder<T>) level.getRecipeManager().byKey(id).orElse(null);
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IRecipeManager manager = jeiRuntime.getRecipeManager();
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        List<RecipeHolder<SmeltingRecipe>> hideS = Stream.of(
                        "water_bucket_from_smelting",
                        "water_bottle_from_smelting",
                        "terracotta_water_bowl_from_smelting")
                .map(p -> ThirstJEI.<SmeltingRecipe>getHolder(level, ResourceLocation.fromNamespaceAndPath("thirst", p)))
                .filter(Objects::nonNull)
                .toList();

        List<RecipeHolder<CampfireCookingRecipe>> hideC = Stream.of(
                        "water_bucket_from_campfire_cooking",
                        "water_bottle_from_campfire_cooking",
                        "terracotta_water_bowl_from_campfire_cooking")
                .map(p -> ThirstJEI.<CampfireCookingRecipe>getHolder(level, ResourceLocation.fromNamespaceAndPath("thirst", p)))
                .filter(Objects::nonNull)
                .toList();

        manager.hideRecipes(RecipeTypes.SMELTING, hideS);
        manager.hideRecipes(RecipeTypes.CAMPFIRE_COOKING, hideC);
    }

    @Nullable
    static <T extends Recipe<?>> T getRecipe(Level level, ResourceLocation id, Class<T> type) {
        return level.getRecipeManager().byKey(id)
                .filter(type::isInstance)
                .map(type::cast)
                .orElse(null);
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg){
        ItemStack water_bottle = new ItemStack(Items.POTION);
        water_bottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        ItemStack water_bucket = new ItemStack(Items.WATER_BUCKET);

        ItemStack terra = new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL);

        // Water bottle - SM
        SmeltingRecipe bottle_dirty2accept = RecipePurityHelper.puritySmelt(
                "waterbottle_dirty2accept",
                RecipePurityHelper.stackWithPurity(0, water_bottle),
                RecipePurityHelper.stackWithPurity(2, water_bottle),
                0.35f,
                200
        );

        SmeltingRecipe bottle_accept2pur = RecipePurityHelper.puritySmelt(
                "waterbottle_accept2pur",
                RecipePurityHelper.stackWithPurity(2, water_bottle),
                RecipePurityHelper.stackWithPurity(3, water_bottle),
                0.35f,
                200
        );

        SmeltingRecipe bottle_sightly2pur = RecipePurityHelper.puritySmelt(
                "waterbottle_sightly2pur",
                RecipePurityHelper.stackWithPurity(1, water_bottle),
                RecipePurityHelper.stackWithPurity(3, water_bottle),
                0.35f,
                200
        );

        // Water Bucket - SM
        SmeltingRecipe bucket_dirty2accept = RecipePurityHelper.puritySmelt(
                "bucket_dirty2accept",
                RecipePurityHelper.stackWithPurity(0, water_bucket),
                RecipePurityHelper.stackWithPurity(2, water_bucket),
                0.35f,
                200
        );

        SmeltingRecipe bucket_accept2pur = RecipePurityHelper.puritySmelt(
                "bucket_accept2pur",
                RecipePurityHelper.stackWithPurity(2, water_bucket),
                RecipePurityHelper.stackWithPurity(3, water_bucket),
                0.35f,
                200
        );

        SmeltingRecipe bucket_sightly2pur = RecipePurityHelper.puritySmelt(
                "bucket_sightly2pur",
                RecipePurityHelper.stackWithPurity(1, water_bucket),
                RecipePurityHelper.stackWithPurity(3, water_bucket),
                0.35f,
                200
        );

        // Terracotta Bowl - SM
        SmeltingRecipe terra_dirty2accept = RecipePurityHelper.puritySmelt(
                "terra_dirty2accept",
                RecipePurityHelper.stackWithPurity(0, terra),
                RecipePurityHelper.stackWithPurity(2, terra),
                0.35f,
                200
        );

        SmeltingRecipe terra_accept2pur = RecipePurityHelper.puritySmelt(
                "terra_accept2pur",
                RecipePurityHelper.stackWithPurity(2, terra),
                RecipePurityHelper.stackWithPurity(3, terra),
                0.35f,
                200
        );

        SmeltingRecipe terra_sightly2pur = RecipePurityHelper.puritySmelt(
                "terra_sightly2pur",
                RecipePurityHelper.stackWithPurity(1, terra),
                RecipePurityHelper.stackWithPurity(3, terra),
                0.35f,
                200
        );

        // Water Bottle - CC
        CampfireCookingRecipe bottle_dirty2sightlyc = RecipePurityHelper.purityCampfireCook(
                "bottle_dirty2sightlyc",
                RecipePurityHelper.stackWithPurity(0, water_bottle),
                RecipePurityHelper.stackWithPurity(1, water_bottle),
                0.35f,
                300
        );

        CampfireCookingRecipe bottle_sightly2accpetc = RecipePurityHelper.purityCampfireCook(
                "bottle_sightly2accpetc",
                RecipePurityHelper.stackWithPurity(1, water_bottle),
                RecipePurityHelper.stackWithPurity(2, water_bottle),
                0.35f,
                300
        );

        CampfireCookingRecipe bottle_accept2purc = RecipePurityHelper.purityCampfireCook(
                "bottle_accept2purc",
                RecipePurityHelper.stackWithPurity(2, water_bottle),
                RecipePurityHelper.stackWithPurity(3, water_bottle),
                0.35f,
                300
        );

        // Water Bucket - CC
        CampfireCookingRecipe bucket_dirty2sightlyc = RecipePurityHelper.purityCampfireCook(
                "bucket_dirty2sightlyc",
                RecipePurityHelper.stackWithPurity(0, water_bucket),
                RecipePurityHelper.stackWithPurity(1, water_bucket),
                0.35f,
                300
        );

        CampfireCookingRecipe bucket_sightly2acceptc = RecipePurityHelper.purityCampfireCook(
                "bucket_sightly2acceptc",
                RecipePurityHelper.stackWithPurity(1, water_bucket),
                RecipePurityHelper.stackWithPurity(2, water_bucket),
                0.35f,
                300
        );

        CampfireCookingRecipe bucket_accept2purc = RecipePurityHelper.purityCampfireCook(
                "bucket_accept2purc",
                RecipePurityHelper.stackWithPurity(2, water_bucket),
                RecipePurityHelper.stackWithPurity(3, water_bucket),
                0.35f,
                300
        );

        // Terracotta - CC
        CampfireCookingRecipe terra_dirty2sightlyc = RecipePurityHelper.purityCampfireCook(
                "terra_dirty2sightlyc",
                RecipePurityHelper.stackWithPurity(0, terra),
                RecipePurityHelper.stackWithPurity(1, terra),
                0.35f,
                300
        );

        CampfireCookingRecipe terra_sightly2acceptc = RecipePurityHelper.purityCampfireCook(
                "terra_sightly2acceptc",
                RecipePurityHelper.stackWithPurity(1, terra),
                RecipePurityHelper.stackWithPurity(2, terra),
                0.35f,
                300
        );

        CampfireCookingRecipe terra_accept2purc = RecipePurityHelper.purityCampfireCook(
                "terra_accept2purc",
                RecipePurityHelper.stackWithPurity(2, terra),
                RecipePurityHelper.stackWithPurity(3, terra),
                0.35f,
                300
        );

        // List
        List<RecipeHolder<SmeltingRecipe>> smelting = List.of(
                holder("waterbottle_accept2pur", bottle_accept2pur),
                holder("waterbottle_dirty2accept", bottle_dirty2accept),
                holder("waterbottle_sightly2pur", bottle_sightly2pur),
                holder("bucket_accept2pur", bucket_accept2pur),
                holder("bucket_sightly2pur", bucket_sightly2pur),
                holder("bucket_dirty2accept", bucket_dirty2accept),
                holder("terra_sightly2pur", terra_sightly2pur),
                holder("terra_accept2pur", terra_accept2pur),
                holder("terra_dirty2accept", terra_dirty2accept)
        );

        List<RecipeHolder<CampfireCookingRecipe>> cookingCamp = List.of(
                holder("bottle_sightly2accpetc", bottle_sightly2accpetc),
                holder("bottle_dirty2sightlyc", bottle_dirty2sightlyc),
                holder("bottle_accept2purc", bottle_accept2purc),
                holder("bucket_sightly2acceptc", bucket_sightly2acceptc),
                holder("bucket_accept2purc", bucket_accept2purc),
                holder("bucket_dirty2sightlyc", bucket_dirty2sightlyc),
                holder("terra_sightly2acceptc", terra_sightly2acceptc),
                holder("terra_accept2purc", terra_accept2purc),
                holder("terra_dirty2sightlyc", terra_dirty2sightlyc)
        );

        reg.addRecipes(RecipeTypes.SMELTING, smelting);
        reg.addRecipes(RecipeTypes.CAMPFIRE_COOKING, cookingCamp);
    }

    private static <T extends Recipe<?>> RecipeHolder<T> holder(String path, T recipe) {
        return new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, path), recipe);
    }
}
