package com.thirsty.mixin;

import com.thirsty.purity.WaterPurity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredCauldronBlock.class)
public abstract class MixinLayeredCauldronBlock
{
    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    protected void addPurityBlockState(StateDefinition.Builder<Block, BlockState> p_153549_, CallbackInfo ci)
    {
        p_153549_.add(WaterPurity.BLOCK_PURITY);
    }

    @Inject(method = "receiveStalactiteDrip", at = @At("TAIL"))
    private void markDripstoneWaterDirty(BlockState state, Level level, BlockPos pos, Fluid fluid, CallbackInfo ci)
    {
        if (fluid == Fluids.WATER
                && state.getValue(LayeredCauldronBlock.LEVEL) < LayeredCauldronBlock.MAX_FILL_LEVEL)
        {
            BlockState filledState = level.getBlockState(pos);
            level.setBlockAndUpdate(pos, filledState.setValue(WaterPurity.BLOCK_PURITY, WaterPurity.MIN_PURITY + 1));
        }
    }
}
