package com.thirsty.mixin;

import com.thirsty.thirst.ThirstData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack
{
    @Shadow
    public abstract Item getItem();


    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void thirst$drinkOnFinish(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (level.isClientSide() || !(livingEntity instanceof Player player)) return;
        ThirstData.drink((ItemStack) (Object) this, player);
    }
}