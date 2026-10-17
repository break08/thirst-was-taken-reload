package com.thirsty;

import com.thirsty.api.config.*;
import com.thirsty.item.ItemInit;
import com.thirsty.misc.ThirstHelper;
import com.thirsty.misc.TickHelper;
import com.thirsty.network.ThirstNetwork;
import com.thirsty.network.ThirstPayload;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThirstWasTaken implements ModInitializer {
	public static final String MOD_ID = "thirst";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Cloth Config API
		AutoConfig.register(CommonConfig.class, GsonConfigSerializer::new);
		AutoConfig.register(KeyWordConfig.class, GsonConfigSerializer::new);
		AutoConfig.register(ContainerConfig.class, GsonConfigSerializer::new);
		AutoConfig.register(ItemSettingsConfig.class, GsonConfigSerializer::new);

		// Item + Thirst init
		ItemInit.initialize();
		WaterPurity.init();
		TickHelper.initialize();
		WaterPurity.eventInit();
		ThirstHelper.init();
		PayloadTypeRegistry.playC2S().register(ThirstPayload.TYPE, ThirstPayload.CODEC);
		ThirstNetwork.onInitialize();
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> ThirstHelper.reload());

		// Loot init
		ThirstLootModifier.init();
	}


	public static ResourceLocation asResource(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
