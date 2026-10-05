package com.thirsty.mixin;

import com.thirsty.item.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.thirsty.purity.WaterPurity.*;

@Mixin(Item.class)
public class PurityTooltip {
    @Inject(method="appendHoverText", at = @At("TAIL"))
    private void addTooltip(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci){
        TooltipHelper.addPurityTooltip(itemStack, list);
    }
}