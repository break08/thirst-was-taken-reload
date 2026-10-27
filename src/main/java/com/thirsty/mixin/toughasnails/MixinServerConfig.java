package com.thirsty.mixin.toughasnails;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.config.ThirstConfig;

@Mixin(value = ThirstConfig.class,remap = false)
public class MixinServerConfig {
    @Shadow
    public boolean enableThirst;
    @Shadow
    public boolean thirstPreventHealthRegen;

    @Inject(method = "load", at = @At("TAIL"))
    private void modifyBoolean(CallbackInfo ci) {
        this.enableThirst = false;
        this.thirstPreventHealthRegen = false;
    }
}