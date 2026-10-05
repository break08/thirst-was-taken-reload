package com.thirst.network;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.misc.MathHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

public class DrinkByHand {
    public static int cooldown = 0;
    public static void register(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Player player = client.player;
            if (player == null){return;}
            Level level = player.level();
            BlockPos blockPos = MathHelper.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY).getBlockPos();
            boolean HandAvailable;
            if (cooldown > 0){
                cooldown--;
                return;
            }

            if (level.getFluidState(blockPos).is(FluidTags.WATER) && player.isCrouching() && !player.isInvulnerable() && client.options.keyUse.isDown()) {

                if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DRINK_BOTH_HAND_NEEDED){
                    HandAvailable = player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
                }else {
                    HandAvailable = player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty();
                }

                if(HandAvailable){
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    DrinkByHandData.sendC2SPacket(blockPos);
                }

                cooldown = 20;
            }
        });
    }
}
