package com.thirsty;

import com.thirsty.api.config.ClientConfig;
import com.thirsty.gui.appleskin.TooltipRenderer;
import com.thirsty.network.DrinkByHand;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ThirstWasTakenClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        // Cloth Config API
        AutoConfig.register(ClientConfig.class, GsonConfigSerializer::new);

        // AppleSkin + Thirst Bar registry
        if (FabricLoader.getInstance().isModLoaded("appleskin")){
            TooltipRenderer.register();
        }

        DrinkByHand.register();
    }
}
