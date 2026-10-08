package com.thirsty.mixin.create;

import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OpenEndedPipe.class,remap = false)
public class MixinOpenEndedPipe
{

    @Inject(method = "removeFluidFromSpace", at = @At("HEAD"), cancellable = true, remap = false)
    private void removeFluidFromSpace(TransactionContext ctx, CallbackInfoReturnable<FluidStack> cir)
    {
        FluidStack stack = cir.getReturnValue();
        if (stack == null || stack.isEmpty() || !stack.getFluid().isSame(Fluids.WATER)) return;

        OpenEndedPipe pipe = (OpenEndedPipe) (Object) this;
        if (pipe.getWorld() == null) return;

        CreateWaterPurity.addPurity(stack,
                WaterPurity.getBlockPurity(pipe.getWorld(), pipe.getOutputPos()));
        cir.setReturnValue(stack);
    }
}
