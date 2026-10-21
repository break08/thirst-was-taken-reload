package com.thirsty.mixin;

import com.mojang.authlib.GameProfile;
import com.thirsty.api.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    public MixinLocalPlayer(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method ="isSprintingPossible", at = @At(value = "HEAD"), cancellable = true)
    public void hasEnoughThirstToStartSprinting(boolean allowedInShallowWater, CallbackInfoReturnable<Boolean> cir){
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (AutoConfig.getConfigHolder(CommonConfig.class).getConfig().MOVE_SLOW_WHEN_THIRSTY
                &&  PLAYER_THIRST.get(player).getThirst() < 6)
            cir.setReturnValue(false);
    }
}