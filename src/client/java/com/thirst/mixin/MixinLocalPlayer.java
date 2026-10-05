package com.thirst.mixin;

import com.mojang.authlib.GameProfile;
import com.thirsty.api.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    public MixinLocalPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    /**
     * @reason prevent sprinting when thirst
     * @return food level or thirst level
     */

    @Redirect(method ="hasEnoughFoodToStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;getFoodLevel()I"))
    public int hasEnoughThirstToStartSprinting(FoodData instance){
        int Food = instance.getFoodLevel();
        if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().MOVE_SLOW_WHEN_THIRSTY) return Food;

        if(Food < 6.0F){
            return Food;
        }else {
            Food = PLAYER_THIRST.get(this).getThirst();
        }
        return Food;
    }
}