package com.thirsty.mixin;

import com.thirsty.config.CommonConfig;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractCookingRecipe.class)
public class MixinAbstractCookingRecipe {
    @ModifyArg(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z"))
    public ItemStack matches(ItemStack itemStack){
        if(WaterPurity.isWaterFilledContainer(itemStack) && !itemStack.getTag().contains("Purity")){
            ItemStack matched = itemStack.copy();
            CompoundTag tag = matched.getOrCreateTag();
            tag.putInt("Purity", AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY);
            return matched;
        }
        return itemStack;
    }
}
