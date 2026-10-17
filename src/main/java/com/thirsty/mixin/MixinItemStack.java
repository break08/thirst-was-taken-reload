package com.thirsty.mixin;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements DataComponentHolder, FabricItemStack
{
    @Shadow
    public abstract Item getItem();

    @Inject(method="getMaxStackSize", at = @At("HEAD"), cancellable = true)
    public void changeWaterBottleStackSize(CallbackInfoReturnable<Integer> cir)
    {
        PotionContents contents = this.get(DataComponents.POTION_CONTENTS);
        if (contents==null){return;}
        if(getItem() == Items.POTION && contents.is(Potions.WATER))
            cir.setReturnValue(AutoConfig.getConfigHolder(CommonConfig.class).getConfig().WATER_BOTTLE_STACKSIZE);
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void thirst$drinkOnFinish(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (level.isClientSide() || !(livingEntity instanceof Player player)) return;
        ThirstData.drink((ItemStack) (Object) this, player);
    }
}