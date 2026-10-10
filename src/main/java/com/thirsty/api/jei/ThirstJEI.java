package com.thirsty.api.jei;

import com.thirsty.item.ItemInit;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ThirstJEI implements IModPlugin {
    /**
     * The unique ID for this mod plugin.
     * The namespace should be your mod's modId.
     */
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation("thirst", "jei_plugin");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IRecipeManager manager = jeiRuntime.getRecipeManager();
        Level level = Minecraft.getInstance().level;

        if (level == null){
            return;
        }

        List<SmeltingRecipe> hideS = List.of(
                Objects.requireNonNull(getRecipe(level, new ResourceLocation("thirst", "water_bucket_from_smelting"), SmeltingRecipe.class)),
                Objects.requireNonNull(getRecipe(level, new ResourceLocation("thirst", "water_bottle_from_smelting"), SmeltingRecipe.class)),
                Objects.requireNonNull(getRecipe(level, new ResourceLocation("thirst", "terracotta_water_bowl_from_smelting"), SmeltingRecipe.class))
        );

        List<CampfireCookingRecipe> hideC = List.of(
                Objects.requireNonNull(getRecipe(level, new ResourceLocation("thirst", "water_bucket_from_campfire_cooking"), CampfireCookingRecipe.class)),
                Objects.requireNonNull(getRecipe(level, new ResourceLocation("thirst", "water_bottle_from_campfire_cooking"), CampfireCookingRecipe.class)),
                Objects.requireNonNull(getRecipe(level, new ResourceLocation("thirst", "terracotta_water_bowl_from_campfire_cooking"), CampfireCookingRecipe.class))
        );

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
        PotionUtils.setPotion(water_bottle, Potions.WATER);

        ItemStack water_bucket = new ItemStack(Items.BUCKET);

        ItemStack terra = new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL);

        // Water bottle - SM
        SmeltingRecipe bottle_dirty2accept = new JEIRecipePurity().puritySmelt(
                "waterbottle_dirty2accept",
                new JEIRecipePurity().stackWithPurity(0, water_bottle),
                new JEIRecipePurity().stackWithPurity(2, water_bottle),
                0.35f,
                200
        );

        SmeltingRecipe bottle_accept2pur = new JEIRecipePurity().puritySmelt(
                "waterbottle_accept2pur",
                new JEIRecipePurity().stackWithPurity(2, water_bottle),
                new JEIRecipePurity().stackWithPurity(3, water_bottle),
                0.35f,
                200
        );

        SmeltingRecipe bottle_sightly2pur = new JEIRecipePurity().puritySmelt(
                "waterbottle_sightly2pur",
                new JEIRecipePurity().stackWithPurity(1, water_bottle),
                new JEIRecipePurity().stackWithPurity(3, water_bottle),
                0.35f,
                200
        );

        // Water Bucket - SM
        SmeltingRecipe bucket_dirty2accept = new JEIRecipePurity().puritySmelt(
                "bucket_dirty2accept",
                new JEIRecipePurity().stackWithPurity(0, water_bucket),
                new JEIRecipePurity().stackWithPurity(2, water_bucket),
                0.35f,
                200
        );

        SmeltingRecipe bucket_accept2pur = new JEIRecipePurity().puritySmelt(
                "bucket_accept2pur",
                new JEIRecipePurity().stackWithPurity(2, water_bucket),
                new JEIRecipePurity().stackWithPurity(3, water_bucket),
                0.35f,
                200
        );

        SmeltingRecipe bucket_sightly2pur = new JEIRecipePurity().puritySmelt(
                "bucket_sightly2pur",
                new JEIRecipePurity().stackWithPurity(1, water_bucket),
                new JEIRecipePurity().stackWithPurity(3, water_bucket),
                0.35f,
                200
        );

        // Terracotta Bowl - SM
        SmeltingRecipe terra_dirty2accept = new JEIRecipePurity().puritySmelt(
                "terra_dirty2accept",
                new JEIRecipePurity().stackWithPurity(0, terra),
                new JEIRecipePurity().stackWithPurity(2, terra),
                0.35f,
                200
        );

        SmeltingRecipe terra_accept2pur = new JEIRecipePurity().puritySmelt(
                "terra_accept2pur",
                new JEIRecipePurity().stackWithPurity(2, terra),
                new JEIRecipePurity().stackWithPurity(3, terra),
                0.35f,
                200
        );

        SmeltingRecipe terra_sightly2pur = new JEIRecipePurity().puritySmelt(
                "terra_sightly2pur",
                new JEIRecipePurity().stackWithPurity(1, terra),
                new JEIRecipePurity().stackWithPurity(3, terra),
                0.35f,
                200
        );

        // Water Bottle - CC
        CampfireCookingRecipe bottle_dirty2acceptc = new JEIRecipePurity().purityCampfireCook(
                "waterbottle_dirty2acceptc",
                new JEIRecipePurity().stackWithPurity(0, water_bottle),
                new JEIRecipePurity().stackWithPurity(2, water_bottle),
                0.35f,
                300
        );

        CampfireCookingRecipe bottle_accept2purc = new JEIRecipePurity().purityCampfireCook(
                "waterbottle_accept2purc",
                new JEIRecipePurity().stackWithPurity(2, water_bottle),
                new JEIRecipePurity().stackWithPurity(3, water_bottle),
                0.35f,
                300
        );

        CampfireCookingRecipe bottle_sightly2purc = new JEIRecipePurity().purityCampfireCook(
                "waterbottle_sightly2purc",
                new JEIRecipePurity().stackWithPurity(1, water_bottle),
                new JEIRecipePurity().stackWithPurity(3, water_bottle),
                0.35f,
                300
        );

        // Water Bucket - CC
        CampfireCookingRecipe bucket_dirty2acceptc = new JEIRecipePurity().purityCampfireCook(
                "bucket_dirty2acceptc",
                new JEIRecipePurity().stackWithPurity(0, water_bucket),
                new JEIRecipePurity().stackWithPurity(2, water_bucket),
                0.35f,
                300
        );

        CampfireCookingRecipe bucket_accept2purc = new JEIRecipePurity().purityCampfireCook(
                "bucket_accept2purc",
                new JEIRecipePurity().stackWithPurity(2, water_bucket),
                new JEIRecipePurity().stackWithPurity(3, water_bucket),
                0.35f,
                300
        );

        CampfireCookingRecipe bucket_sightly2purc = new JEIRecipePurity().purityCampfireCook(
                "bucket_sightly2purc",
                new JEIRecipePurity().stackWithPurity(1, water_bucket),
                new JEIRecipePurity().stackWithPurity(3, water_bucket),
                0.35f,
                300
        );

        // Terracotta - CC
        CampfireCookingRecipe terra_dirty2acceptc = new JEIRecipePurity().purityCampfireCook(
                "terra_dirty2acceptc",
                new JEIRecipePurity().stackWithPurity(0, terra),
                new JEIRecipePurity().stackWithPurity(2, terra),
                0.35f,
                300
        );

        CampfireCookingRecipe terra_accept2purc = new JEIRecipePurity().purityCampfireCook(
                "terra_accept2purc",
                new JEIRecipePurity().stackWithPurity(2, terra),
                new JEIRecipePurity().stackWithPurity(3, terra),
                0.35f,
                300
        );

        CampfireCookingRecipe terra_sightly2purc = new JEIRecipePurity().purityCampfireCook(
                "terra_sightly2purc",
                new JEIRecipePurity().stackWithPurity(1, terra),
                new JEIRecipePurity().stackWithPurity(3, terra),
                0.35f,
                300
        );

        // List
        List<SmeltingRecipe> smelting = List.of(
                bottle_accept2pur,
                bottle_dirty2accept,
                bottle_sightly2pur,
                bucket_accept2pur,
                bucket_sightly2pur,
                bucket_dirty2accept,
                terra_sightly2pur,
                terra_accept2pur,
                terra_dirty2accept
        );

        List<CampfireCookingRecipe> cookingCamp = List.of(
                bottle_accept2purc,
                bottle_dirty2acceptc,
                bottle_sightly2purc,
                bucket_accept2purc,
                bucket_sightly2purc,
                bucket_dirty2acceptc,
                terra_accept2purc,
                terra_sightly2purc,
                terra_dirty2acceptc
        );

        reg.addRecipes(RecipeTypes.SMELTING, smelting);
        reg.addRecipes(RecipeTypes.CAMPFIRE_COOKING, cookingCamp);
    }
}
