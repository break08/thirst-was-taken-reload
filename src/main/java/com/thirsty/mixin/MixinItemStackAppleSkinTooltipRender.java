package com.thirsty.mixin;

import com.thirsty.gui.appleskin.TooltipRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStack.class)
public class MixinItemStackAppleSkinTooltipRender {
    @Inject(method = "getTooltipImage", at = @At("RETURN"), cancellable = true)
    private void thirst$addTooltip(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (cir.getReturnValue().isPresent()) return;
        if (!FabricLoader.getInstance().isModLoaded("appleskin")) return;
        cir.setReturnValue(TooltipRenderer.createTooltip((ItemStack) (Object) this));
    }
}