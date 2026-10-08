package com.thirsty.mixin.create;

import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= GenericItemFilling.class,remap = false)
public class MixinGenericItemFilling {

    @Inject(method = "fillItem",at= @At("RETURN"), cancellable = true)
    private static void fillItem(Level world, long requiredAmount, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack output = cir.getReturnValue();
        if(CreateWaterPurity.hasPurity(availableFluid) && WaterPurity.isWaterFilledContainer(output)){
            WaterPurity.addPurity(output, CreateWaterPurity.getPurity(availableFluid));
            cir.setReturnValue(output);
        }
    }
}