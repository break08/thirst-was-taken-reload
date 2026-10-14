package com.thirsty.mixin;

import com.thirsty.gui.ThirstBarRenderer;
import com.thirsty.gui.appleskin.HUDOverlayHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public class MixinGui {
    @Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 1))
    private void render(GuiGraphics guiGraphics, CallbackInfo ci){

        if (FabricLoader.getInstance().isModLoaded("appleskin")){
            HUDOverlayHandler.onClientTick();
            HUDOverlayHandler.onRenderGuiOverlayPre(guiGraphics, 0);
        }
        ThirstBarRenderer.renderingHUD(guiGraphics, 0);
        if (FabricLoader.getInstance().isModLoaded("appleskin")){
            HUDOverlayHandler.onRenderGuiOverlayPost(guiGraphics, 0);
        }
    }

    @Inject(method = "getVisibleVehicleHeartRows", at = @At("HEAD"), cancellable = true)
    private void getHeartRowsMixin(int i, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) Math.ceil((double) i / 10.0D) + 1);
    }
}