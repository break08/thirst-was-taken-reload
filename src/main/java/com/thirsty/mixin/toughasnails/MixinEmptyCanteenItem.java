package com.thirsty.mixin.toughasnails;

import com.thirsty.misc.MathHelper;
import com.thirsty.purity.WaterPurity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.item.EmptyCanteenItem;

@Mixin(value = EmptyCanteenItem.class,remap = false)
public abstract class MixinEmptyCanteenItem {

    @Shadow(remap = false)
    protected abstract ItemStack replaceCanteen(ItemStack stack, Player player, ItemStack filledItem);

    @Shadow public abstract Item getPurifiedWaterCanteen();

    @Shadow public abstract Item getWaterCanteen();

    @Shadow public abstract Item getDirtyWaterCanteen();

    @Inject(method = "use",at =@At("HEAD"), cancellable = true)
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        ItemStack stack = player.getItemInHand(hand);
        Level player_level = player.level();

        BlockPos blockPos = MathHelper.getPlayerPOVHitResult(player_level, player, ClipContext.Fluid.ANY).getBlockPos();
        BlockState state = level.getBlockState(blockPos);

        if (!level.mayInteract(player, blockPos)){
            cir.setReturnValue(InteractionResult.PASS);
        }

        if(player_level.getFluidState(blockPos).is(FluidTags.WATER))
        {
            SoundEvent sound= SoundEvents.BOTTLE_FILL;
            ItemStack filledItem;

            player_level.playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
            player_level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);

            int purity= WaterPurity.getBlockPurity(player_level, blockPos);
            if(purity==3){
                filledItem = getPurifiedWaterCanteen().getDefaultInstance();
            } else if (purity==2) {
                filledItem = getWaterCanteen().getDefaultInstance();
            }else {
                filledItem = getDirtyWaterCanteen().getDefaultInstance();
            }

            ItemStack result = ItemUtils.createFilledResult(stack, player, filledItem);

            cir.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(replaceCanteen(stack, player, result)));
        }
        else if (state.getBlock() instanceof RainCollectorBlock) {
            int waterLevel = state.getValue(RainCollectorBlock.LEVEL);
            if (waterLevel > 0 && !level.isClientSide()) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                ((RainCollectorBlock) TANBlocks.RAIN_COLLECTOR).setWaterLevel(level, blockPos, state, waterLevel - 1);
                cir.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(replaceCanteen(stack, player, getPurifiedWaterCanteen().getDefaultInstance())));
            }
        }
    }
}