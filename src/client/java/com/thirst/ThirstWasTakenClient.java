package com.thirst;

import com.thirst.gui.ThirstBarRenderer;
import com.thirst.gui.appleskin.HUDOverlayHandler;
import com.thirst.gui.appleskin.TooltipRenderer;
import com.thirst.network.DrinkByHand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import static com.thirsty.ThirstWasTaken.MOD_ID;

public class ThirstWasTakenClient implements ClientModInitializer {
    public static final ResourceLocation C2S_DRINK_PACKET_ID = new ResourceLocation(MOD_ID, "hand_drink_pack");
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ThirstBarRenderer());
        if (FabricLoader.getInstance().isModLoaded("appleskin")){
            TooltipRenderer.register();
            HUDOverlayHandler.event_reg();
        }
        DrinkByHand.register();
    }
}
