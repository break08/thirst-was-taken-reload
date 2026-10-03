package com.thirsty;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.gui.ThirstBarRenderer;
import com.thirsty.item.ItemInit;
import com.thirsty.misc.TickHelper;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThirstWasTaken implements ModInitializer {
	public static final String MOD_ID = "thirst";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HudRenderCallback.EVENT.register(new ThirstBarRenderer());
		AutoConfig.register(CommonConfig.class, GsonConfigSerializer::new);
		ItemInit.initialize();
        try {
            WaterPurity.init();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
		TickHelper.initialize();
		WaterPurity.eventInit();
    }

	public static ResourceLocation asResource(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
