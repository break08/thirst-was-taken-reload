package com.thirsty.api.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
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
}
