package com.thirsty.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class MixinPlayer
{
    // attempt to fix bug: player has around x2 quench and thirst when drinking/eating
}