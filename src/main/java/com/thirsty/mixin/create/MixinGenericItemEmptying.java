package com.thirsty.mixin.create;

import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GenericItemEmptying.class, remap = false)
public class MixinGenericItemEmptying {

    @Inject(method = "emptyItem", at = @At("RETURN"), cancellable = true, remap = false)
    private static void thirst$purity(Level world, ItemStack stack, boolean simulate,
                                      CallbackInfoReturnable<Pair<FluidStack, ItemStack>> cir) {
        if (!WaterPurity.hasPurity(stack)) return;

        Pair<FluidStack, ItemStack> out = cir.getReturnValue();
        FluidStack fluid = out.getFirst();
        if (fluid.isEmpty() || !fluid.getFluid().isSame(Fluids.WATER)) return;

        cir.setReturnValue(Pair.of(
                CreateWaterPurity.addPurity(fluid, WaterPurity.getPurity(stack)),
                out.getSecond()));
    }
}
