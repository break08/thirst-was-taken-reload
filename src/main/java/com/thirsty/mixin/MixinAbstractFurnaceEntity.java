package com.thirsty.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.thirsty.purity.WaterPurity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceEntity {
    // ok mojang make nbt harder in 1.21.1, they had better not do it
    @Inject(method = "canBurn",at= @At("RETURN"), cancellable = true)
    private static void canBurn(RegistryAccess registryAccess, RecipeHolder<?> recipeHolder, NonNullList<ItemStack> nonNullList, int i, CallbackInfoReturnable<Boolean> cir){
        ItemStack itemStack = nonNullList.get(0);

        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
        if (contents == null){return;}
        if (itemStack.is(Items.POTION) && !contents.is(Potions.WATER)){
            cir.setReturnValue(false);
        }

        if (!WaterPurity.isWaterFilledContainer(itemStack) || recipeHolder == null){return;}

        if (!WaterPurity.hasPurity(itemStack) || WaterPurity.getPurity(itemStack) == 3){
            cir.setReturnValue(false);
        }

        ItemStack out = nonNullList.get(2);

        if (out.isEmpty()) return;

        int expected = Math.min(3, WaterPurity.getPurity(itemStack) + 2);
        boolean canStack = ItemStack.isSameItem(out, itemStack)
                && WaterPurity.getPurity(out) == expected
                && out.getCount() < Math.min(i, out.getMaxStackSize());
        cir.setReturnValue(canStack);
    }

    @ModifyVariable(
            method = "burn",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    private static ItemStack overrideItemStack2(ItemStack itemStack2, @Local(argsOnly = true) NonNullList<ItemStack> nonNullList){
        ItemStack itemStack = nonNullList.get(0);
        ItemStack stack2 = itemStack.copy();
        if (WaterPurity.isWaterFilledContainer(itemStack) && WaterPurity.hasPurity(itemStack)){
            WaterPurity.addPurity(stack2, WaterPurity.getPurity(itemStack) + 2);
            if (WaterPurity.getPurity(stack2) > 3){
                WaterPurity.addPurity(stack2, 3);
            }
            stack2.setCount(1);
        } else {
            return itemStack2;
        }
        return stack2;
    }
}