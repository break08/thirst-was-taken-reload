package com.thirsty.effect;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffect implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    public static final MobEffect DEHYDRATION = new DehydrationEffect(MobEffectCategory.HARMFUL, 32);
    public static final MobEffect QUENCHNESS = new QuenchnessEffect(MobEffectCategory.BENEFICIAL, 64);

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation("thirst", "thirst"), DEHYDRATION);
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation("thirst", "quenchness"), QUENCHNESS);
    }
}
