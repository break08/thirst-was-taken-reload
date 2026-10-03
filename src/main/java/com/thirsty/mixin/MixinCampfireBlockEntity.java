package com.thirsty.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.thirsty.purity.WaterPurity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin({CampfireBlockEntity.class})
public class MixinCampfireBlockEntity
{
    public MixinCampfireBlockEntity() { }

    @Inject(
            method = {"particleTick"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void waterVapour(Level level, BlockPos pos, BlockState blockState, CampfireBlockEntity campfire, CallbackInfo ci) {
        RandomSource random = level.getRandom();
        int l = blockState.getValue(CampfireBlock.FACING).get2DDataValue();
        boolean cancel = false;

        for(int i = 0; i < campfire.getItems().size(); ++i) {
            ItemStack itemstack = campfire.getItems().get(i);
            if (WaterPurity.isWaterFilledContainer(itemstack)) {
                cancel = true;
                if (random.nextFloat() < 0.2F) {
                    Direction direction = Direction.from2DDataValue(Math.floorMod(i + l, 4));
                    final float f = 0.3125F;
                    double d0 = (double)pos.getX() + 0.5 - (double)((float)direction.getStepX() * f) + (double)((float)direction.getClockWise().getStepX() * f);
                    double d1 = (double)pos.getY() + 0.6;
                    double d2 = (double)pos.getZ() + 0.5 - (double)((float)direction.getStepZ() * f) + (double)((float)direction.getClockWise().getStepZ() * f);
                    level.addParticle(ParticleTypes.EFFECT, d0, d1, d2, 0.0, 0.001, 0.0);
                }
            }
        }

        if (cancel) {
            if (random.nextFloat() < 0.11F) {
                for(int i = 0; i < random.nextInt(2) + 2; ++i) {
                    CampfireBlock.makeParticles(level, pos, blockState.getValue(CampfireBlock.SIGNAL_FIRE), false);
                }
            }

            ci.cancel();
        }
    }

    @Inject(method = "getCookableRecipe", at = @At("HEAD"), cancellable = true)
    private static void removeInvalidPurity(ItemStack itemStack, CallbackInfoReturnable<Optional<CampfireCookingRecipe>> cir){
        int purity = WaterPurity.getPurity(itemStack);
        if (WaterPurity.isWaterFilledContainer(itemStack) && (purity == 0 || purity == 3)){
            cir.setReturnValue(Optional.empty());
        }
    }

    @ModifyVariable(
            method = "cookTick",
            at = @At("STORE"),
            ordinal = 1
    )
    private static ItemStack overrideItemStack2(ItemStack itemStack2,
                                                Level level, BlockPos blockPos,
                                                BlockState blockState,
                                                CampfireBlockEntity campfireBlockEntity,
                                                @Local(ordinal = 0) int i
    ) {
        ItemStack itemStack = campfireBlockEntity.items.get(i);
        int original_pur = WaterPurity.getPurity(itemStack);
        if (WaterPurity.isWaterFilledContainer(itemStack) && WaterPurity.isWaterFilledContainer(itemStack2) && WaterPurity.hasPurity(itemStack2) && !(original_pur == 0 || original_pur == 3)) {
            WaterPurity.addPurity(itemStack2, original_pur + 1);
        }
        return itemStack2;
    }
}
