package com.thirsty.effect;

import com.thirsty.thirst.ThirstData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantaneousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class DehydrationEffect extends InstantaneousMobEffect {
    public DehydrationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(@NonNull ServerLevel level, @NonNull LivingEntity p_295892_, int p_296026_) {
        if (p_295892_ instanceof Player player) {
            ThirstData.drink(player, -1, -1);
        }
        return true;
    }
}