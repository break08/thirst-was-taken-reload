package com.thirsty.api.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import static com.thirsty.api.emi.ThirstEMI.SMELTING_PURIFY_CATEGORY;

public class ThirstWithEMIRecipe extends BasicEmiRecipe {
    public ThirstWithEMIRecipe(ResourceLocation id, EmiStack in, EmiStack out) {
        super(SMELTING_PURIFY_CATEGORY, id, 76, 18);
        this.inputs.add(in);
        this.outputs.add(out);
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        widgetHolder.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }
}