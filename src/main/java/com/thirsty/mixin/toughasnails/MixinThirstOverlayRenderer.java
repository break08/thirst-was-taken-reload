package com.thirsty.mixin.toughasnails;

import glitchcore.event.client.RenderHudEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstOverlayRenderer;

@Mixin(ThirstOverlayRenderer.class)
public class MixinThirstOverlayRenderer {
    // Destroy Thirst system of Tough as Nails
    @Inject(method = "onBeginRenderAir", at = @At("HEAD"), cancellable = true)
    private static void beginRender(RenderHudEvent.Pre event, CallbackInfo ci){
        ci.cancel();
    }

    @Inject(method = "drawThirst", at = @At("HEAD"), cancellable = true)
    private static void drawThirst(GuiGraphicsExtractor GuiGraphicsExtractor, int screenWidth, int rowTop, int thirstLevel, float thirstHydrationLevel, CallbackInfo ci){
        ci.cancel();
    }
}
