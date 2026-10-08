package com.thirsty.mixin.create;

import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidDrainingBehaviour.class,remap = false)
public abstract class MixinFluidDrainingBehaviour
{
    @Inject(method = "getDrainableFluid", at = @At("RETURN"), remap = false, cancellable = true)
    public void getDrainableFluid(BlockPos rootPos, CallbackInfoReturnable<FluidStack> cir){
        FluidDrainingBehaviour behaviour = ((FluidDrainingBehaviour)(Object) this);
        FluidStack output=cir.getReturnValue();
        if (FluidHelper.isWater(output.getFluid())){
            CreateWaterPurity.addPurity(output,WaterPurity.getBlockPurity(behaviour.getWorld(), rootPos));
            cir.setReturnValue(output);
        }
    }
}
