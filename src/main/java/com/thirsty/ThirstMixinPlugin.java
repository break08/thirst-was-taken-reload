package com.thirsty;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

public class ThirstMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains(".toughasnails.")){
            return FabricLoader.getInstance().isModLoaded("toughasnails");
        } else if (mixinClassName.contains(".jade.")){
            return FabricLoader.getInstance().isModLoaded("jade");
        } else if (mixinClassName.contains(".brewinandchewin.")){
            return FabricLoader.getInstance().isModLoaded("brewinandchewin");
        }
        return true;
    }
}