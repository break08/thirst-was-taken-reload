package com.thirsty.mixin;

import com.thirsty.api.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public interface MixinItemInstance extends TypedInstance<Item>, DataComponentGetter {
    @Inject(method="getMaxStackSize", at = @At("HEAD"), cancellable = true)
    default void changeWaterBottleStackSize(CallbackInfoReturnable<Integer> cir)
    {
        if ((Object)this instanceof ItemStack) {
            PotionContents contents = this.get(DataComponents.POTION_CONTENTS);
            if (contents == null) {
                return;
            }
            if (this.is(Items.POTION) && contents.is(Potions.WATER))
                cir.setReturnValue(AutoConfig.getConfigHolder(CommonConfig.class).getConfig().WATER_BOTTLE_STACKSIZE);
        }
    }
}
