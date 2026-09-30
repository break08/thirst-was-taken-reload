package com.thirsty.mixin;

import com.mojang.serialization.Codec;
import com.thirsty.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer{

    /**
     * @reason prevent sprinting when thirst
     * @return food level or thirst level
     */

    @Redirect(method ="hasEnoughFoodToStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;getFoodLevel()I"))
    public int hasEnoughThirstToStartSprinting(FoodData instance){
        int Food = instance.getFoodLevel();
        if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().MOVE_SLOW_WHEN_THIRSTY) return Food;

        if(Food < 6.0F){
            return Food;
        }else {
            final AttachmentType<Integer> THIRST = AttachmentRegistry.createPersistent(
                    new ResourceLocation("thirst", "player_thirst"),
                    Codec.INT
            );
            Food = Minecraft.getInstance().player.getAttachedOrElse(THIRST, Food);
        }
        return Food;
    }
}