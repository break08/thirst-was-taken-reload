package com.thirsty.mixin.toughasnails;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstOverlayRenderer;

@Mixin(ThirstOverlayRenderer.class)
public class MixinThirstOverlayRenderer {
    @Inject(method = "drawThirst", at = @At("HEAD"), cancellable = true)
    private static void drawThirst(GuiGraphics guiGraphics, int screenWidth, int rowTop, int thirstLevel, float thirstHydrationLevel, CallbackInfo ci){
        ci.cancel();
    }
}
