package com.thirsty.mixin;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
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
    @Shadow @Nullable
    public abstract CompoundTag getTag();

    @Inject(method="getMaxStackSize", at = @At("HEAD"), cancellable = true)
    public void changeWaterBottleStackSize(CallbackInfoReturnable<Integer> cir)
    {
        if(getItem() == Items.POTION && PotionUtils.getPotion(getTag()) == Potions.WATER )
            cir.setReturnValue(AutoConfig.getConfigHolder(CommonConfig.class).getConfig().WATER_BOTTLE_STACKSIZE);
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void thirst$drinkOnFinish(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (level.isClientSide() || !(livingEntity instanceof Player player)) return;
        ThirstData.drink((ItemStack) (Object) this, player);
    }
}
