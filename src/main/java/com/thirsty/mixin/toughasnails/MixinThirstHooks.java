package com.thirsty.mixin.toughasnails;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstHooks;

@Mixin(ThirstHooks.class)
public class MixinThirstHooks {
    @Inject(method = "doFoodDataTick", at = @At("HEAD"), cancellable = true)
    private static void doFoodDataTick(FoodData data, Player player, CallbackInfo ci){
        ci.cancel();
    }

    @Inject(method = "onCauseFoodExhaustion", at = @At("HEAD"), cancellable = true)
    private static void onCauseFoodExhaustion(Player player, float exhaustion, CallbackInfo ci){
        ci.cancel();
    }
}
