package com.thirsty;

import com.thirsty.api.config.ClientConfig;
import com.thirsty.api.config.CommonConfig;
import com.thirsty.api.config.ContainerConfig;
import com.thirsty.api.config.KeyWordConfig;
import com.thirsty.api.create.CreateRegistry;
import com.thirsty.gui.appleskin.HUDOverlayHandler;
import com.thirsty.gui.appleskin.TooltipRenderer;
import com.thirsty.item.ItemInit;
import com.thirsty.misc.ThirstHelper;
import com.thirsty.misc.TickHelper;
import com.thirsty.network.DrinkByHand;
import com.thirsty.gui.ThirstBarRenderer;
import com.thirsty.network.ThirstNetwork;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Direction;
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
		AutoConfig.register(ClientConfig.class, GsonConfigSerializer::new);
		AutoConfig.register(KeyWordConfig.class, GsonConfigSerializer::new);

		ItemInit.initialize();
		try {
				WaterPurity.init();
		} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
		}
		TickHelper.initialize();
		WaterPurity.eventInit();
		ThirstNetwork.onInitialize();
		DrinkByHand.register();
		ThirstHelper.init();

		// AppleSkin + Thirst Bar registry
		if (FabricLoader.getInstance().isModLoaded("appleskin")){
			TooltipRenderer.register();
			HUDOverlayHandler.onClientTick();
			HUDOverlayHandler.onRenderGuiOverlayPre();
		}
		HudRenderCallback.EVENT.register(new ThirstBarRenderer());
		if (FabricLoader.getInstance().isModLoaded("appleskin")){
			HUDOverlayHandler.onRenderGuiOverlayPost();
		}

		// Create
		if (FabricLoader.getInstance().isModLoaded("create")){
			CreateRegistry.register();
			CreateRegistry.REGISTRATE.get().register();
			FluidStorage.SIDED.registerForBlockEntity((be, side) -> {
				if (side == null || side.getAxis() != Direction.Axis.Y) return null;
				return side == Direction.DOWN
						? be.purifiedTank.getCapability()
						: be.dirtyTank.getCapability();
			}, CreateRegistry.SAND_FILTER_TE.get());
		}
        }

	public static ResourceLocation asResource(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
