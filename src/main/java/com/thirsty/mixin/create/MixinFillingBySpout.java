package com.thirsty.mixin.create;

import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FillingBySpout.class, remap = false)
public class MixinFillingBySpout {

    @Inject(method = "fillItem", at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private static void thirst$addPurity(Level world, long requiredAmount, ItemStack stack,
                                         FluidStack availableFluid, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result == null || result.isEmpty()) return;

        FluidVariant fluid = availableFluid.getType();
        if (!WaterPurity.hasPurity(fluid)) return;

        if (WaterPurity.isWaterFilledContainer(result))
            cir.setReturnValue(WaterPurity.addPurity(result, WaterPurity.getPurity(fluid)));
    }
}
