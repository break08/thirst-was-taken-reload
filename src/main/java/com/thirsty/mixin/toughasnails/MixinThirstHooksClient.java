package com.thirsty.mixin.toughasnails;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstHooksClient;

@Mixin(ThirstHooksClient.class)
public class MixinThirstHooksClient {
    @Inject(method = "onAiStepSetSprinting", at = @At("HEAD"), cancellable = true)
    private static void onAiStepSetSprinting(LocalPlayer player, boolean sprinting, CallbackInfo ci){
        ci.cancel();
    }
}