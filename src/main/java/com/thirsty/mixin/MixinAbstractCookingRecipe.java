package com.thirsty.mixin;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.misc.NbtHelper;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractCookingRecipe.class)
public class MixinAbstractCookingRecipe {
    @ModifyArg(method = "matches(Lnet/minecraft/world/item/crafting/SingleRecipeInput;Lnet/minecraft/world/level/Level;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z"))
    public ItemStack matches(ItemStack itemStack){
        if(WaterPurity.isWaterFilledContainer(itemStack) && !WaterPurity.hasPurity(itemStack)){
            ItemStack matched = itemStack.copy();
            NbtHelper.edit(matched, tag -> tag.putInt("Purity", AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY));
            return matched;
        }
        return itemStack;
    }
}