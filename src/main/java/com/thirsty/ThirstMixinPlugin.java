package com.thirsty;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

public class ThirstMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains(".create.")) {
            return FabricLoader.getInstance().isModLoaded("create");
        } else if (mixinClassName.contains(".toughasnails.")){
            return FabricLoader.getInstance().isModLoaded(".toughasnails.");
        }
        return true;
    }
}