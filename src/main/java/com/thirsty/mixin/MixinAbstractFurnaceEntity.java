package com.thirsty.mixin;

import com.thirsty.purity.WaterPurity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceEntity {
    @Inject(method = "canBurn",at= @At("RETURN"), cancellable = true)
    private static void canBurn(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> nonNullList, int i, CallbackInfoReturnable<Boolean> cir){
        ItemStack itemStack = nonNullList.get(0);
        if (!WaterPurity.isWaterFilledContainer(itemStack) || !cir.getReturnValueZ() || PotionUtils.getPotion(itemStack) != Potions.WATER){return;}

        if (!WaterPurity.hasPurity(itemStack) || WaterPurity.getPurity(itemStack) == 3){
            cir.setReturnValue(false);
        }
        ItemStack out = nonNullList.get(2);
        if (!out.isEmpty()) {
            ItemStack expected = itemStack.copy();
            expected.setCount(1);
            WaterPurity.addPurity(expected, Math.min(3, WaterPurity.getPurity(itemStack) + 2));
            if (!ItemStack.isSameItemSameTags(out, expected)) {
                cir.setReturnValue(false);
            }
        }
    }

    @ModifyVariable(
            method = "burn",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    private static ItemStack overrideItemStack2(ItemStack itemStack2, RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> nonNullList, int i){
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