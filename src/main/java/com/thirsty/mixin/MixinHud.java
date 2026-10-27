package com.thirsty.mixin;

import com.thirsty.gui.ThirstBarRenderer;
import com.thirsty.gui.appleskin.HUDOverlayHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

@Mixin(Hud.class)
public class MixinHud {
    @Inject(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 1))
    private void render(GuiGraphicsExtractor graphics, CallbackInfo ci){
        if (Minecraft.getInstance().player == null){
            return;
        }
        if (FabricLoader.getInstance().isModLoaded("appleskin")){
            HUDOverlayHandler.drawExhaustionOverlay(
                    PLAYER_THIRST.get(Minecraft.getInstance().player).getExhaustion(),
                    graphics,
                    ThirstBarRenderer.lastRight,
                    ThirstBarRenderer.lastTop);
            HUDOverlayHandler.onClientTick();
            HUDOverlayHandler.renderThirstOverlay(Minecraft.getInstance().player,graphics, Minecraft.getInstance().gui.hud.getGuiTicks());
        }
        ThirstBarRenderer.renderingHUD(graphics, 0);
        if (FabricLoader.getInstance().isModLoaded("appleskin")){
            HUDOverlayHandler.renderThirstOverlay(Minecraft.getInstance().player,graphics, Minecraft.getInstance().gui.hud.getGuiTicks());
        }
    }


    @Inject(method = "getVisibleVehicleHeartRows", at = @At("HEAD"), cancellable = true)
    private void getHeartRowsMixin(int i, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) Math.ceil((double) i / 10.0D) + 1);
    }
}