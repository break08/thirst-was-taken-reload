package com.thirsty.mixin;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

@Mixin(FoodData.class)
public abstract class MixinFoodData {
    @Shadow
    public abstract void addExhaustion(float p_38704_);

    @Shadow private float exhaustionLevel;
    @Unique
    private int dehydratedHealTimer = 0;


    @Redirect(
            method = {"tick"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;heal(F)V", ordinal = 0)
    )
    private void healWithSaturation(Player instance, float v)
    {
        FoodData foodData = instance.getFoodData();
        ThirstData thirstData =  PLAYER_THIRST.get(instance);

        float f = Math.min(foodData.getSaturationLevel(), 6.0F);

        boolean shouldHeal = !AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEHYDRATION_HALTS_HEALTH_REGEN || thirstData.getThirst() >= 20;

        if(shouldHeal)
        {
            instance.heal(f / 6.0F);
            thirstData.setJustHealed(true);
            return;
        }

        dehydratedHealTimer++;
        if(dehydratedHealTimer >= 8 && thirstData.getThirst() > 18)
        {
            instance.heal(f / 6.0F);
            thirstData.setJustHealed(true);
            dehydratedHealTimer = 0;
            return;
        }

        this.addExhaustion(-f);
    }

    @Redirect(
            method = {"tick"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;heal(F)V", ordinal = 1)
    )
    private void healWithHunger(Player player, float amount)
    {
       ThirstData thirstData =  PLAYER_THIRST.get(player);
        boolean shouldHeal = !AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEHYDRATION_HALTS_HEALTH_REGEN || thirstData.getThirst() > 18;

        if(shouldHeal)
        {
            player.heal(1.0F);
            thirstData.setJustHealed(true);
        }
        else
            this.addExhaustion(-6.0F);
    }

    @Inject(method = "tick",at = @At(value = "HEAD"))
    private void DealWithExhaustionBySaturation(Player player, CallbackInfo ci){
        if(exhaustionLevel>4.0F){
            ThirstData thirstData =  PLAYER_THIRST.get(player);
            thirstData.updateExhaustion(player);
        }
    }
}
