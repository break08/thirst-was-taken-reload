package com.thirsty.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.thirsty.misc.NbtHelper;
import com.thirsty.purity.WaterPurity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
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

import java.util.Objects;

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
                    level.addParticle(SpellParticleOption.create(ParticleTypes.EFFECT, 0, 1), d0, d1, d2, 0.0, 0.001, 0.0);
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

    // go crazy because fabric does not support nbt crafting

    @Inject(method = "placeFood", at = @At("RETURN"), cancellable = true)
    private void removeInvalidPurity(ServerLevel serverLevel, LivingEntity sourceEntity, ItemStack placeItem, CallbackInfoReturnable<Boolean> cir){
        if (!WaterPurity.isWaterFilledContainer(placeItem) || !WaterPurity.hasPurity(placeItem)){
            return;
        }
        if (WaterPurity.getPurity(placeItem) == 3){
            cir.setReturnValue(false);
        }
        if (placeItem.get(DataComponents.POTION_CONTENTS) == null){
            return;
        }
        if (placeItem.is(Items.POTION) && !Objects.requireNonNull(placeItem.get(DataComponents.POTION_CONTENTS)).is(Potions.WATER)){
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(
            method = "cookTick",
            at = @At("STORE"),
            name = "result")
    private static ItemStack overrideItemStack2(ItemStack result,
                                                @Local(argsOnly = true, name = "level") ServerLevel level, BlockPos pos,
                                                BlockState state,
                                                @Local(argsOnly = true, name = "entity") CampfireBlockEntity entity,
                                                @Local(name = "slot") int slot
    ) {
        ItemStack itemStack = entity.getItems().get(slot);
        if (!WaterPurity.isWaterFilledContainer(itemStack)) return result;

        CompoundTag tag = NbtHelper.get(itemStack);

        ItemStack a = itemStack.copy();
        a.setCount(1);

        // result.setTag(tag.copy());
        NbtHelper.set(a, tag.copy());

        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
        boolean noPotion = contents == null || contents.potion().isEmpty();

        if (noPotion && !WaterPurity.isWaterFilledContainer(result)) {
            a.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        }
        WaterPurity.addPurity(a, Math.min(WaterPurity.getPurity(itemStack) + 1, 3));
        return a;
    }
}