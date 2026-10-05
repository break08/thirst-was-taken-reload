package com.thirsty.mixin;

import com.thirsty.item.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionUtils.class)
public class MixinPotionUtils {
    @Inject(method= "addPotionTooltip(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;F)V", at = @At("TAIL"))
    private static void addPotionTooltip(ItemStack itemStack, List<Component> list, float f, CallbackInfo ci) {
        TooltipHelper.addPurityTooltip(itemStack, list);
    }
}