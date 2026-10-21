package com.thirsty.api.thirst_api.jei_emi_compat;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class RecipePurityHelper {
    public static SmeltingRecipe puritySmelt(String recipe_name, ItemStack input, ItemStack output, float exp, int cook_time)
    {
        return new SmeltingRecipe(
                "",
                CookingBookCategory.MISC,
                Ingredient.of(input.getItem()), output,
                exp, cook_time
        );
    }

    public static CampfireCookingRecipe purityCampfireCook(String recipe_name, ItemStack input, ItemStack output, float exp, int cook_time)
    {
        return new CampfireCookingRecipe(
                "",
                CookingBookCategory.MISC,
                Ingredient.of(input.getItem()), output,
                exp, cook_time
        );
    }

    public static ItemStack stackWithPurity(int purity, ItemStack item){
        ItemStack newStack = item.copy();
        CustomData.update(DataComponents.CUSTOM_DATA, item, t -> t.putInt("Purity", purity));
        return newStack;
    }
}