package com.thirsty.mixin.jade;

import com.thirsty.purity.WaterPurity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.FluidView;
import snownee.jade.util.CommonProxy;

import java.util.Objects;
import java.util.Optional;

@Mixin(value = FluidView.class,remap = false)
public class MixinFluidView {
    @Redirect(method ="readDefault(Lnet/minecraft/nbt/CompoundTag;Lcom/mojang/serialization/DynamicOps;)Lsnownee/jade/api/view/FluidView;", at = @At(value = "INVOKE",target = "Lsnownee/jade/util/CommonProxy;getFluidName(Lsnownee/jade/api/fluid/JadeFluidObject;)Lnet/minecraft/network/chat/Component;"))
    private static Component read(JadeFluidObject fluidObject){
        Optional<? extends CustomData> data = fluidObject.getComponents().get(DataComponents.CUSTOM_DATA);
        if (data == null || data.isEmpty()) return CommonProxy.getFluidName(fluidObject);

        FluidVariant variant = FluidVariant.of(fluidObject.getType(),
                DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, data.get()).build());

        if(variant.isBlank()) return CommonProxy.getFluidName(fluidObject);

        if((WaterPurity.hasPurity(variant) || variant.getFluid().equals(Fluids.WATER)) && WaterPurity.getPurity(variant)!=-1){
            return Component.literal(Objects.requireNonNull(
                            WaterPurity.getPurityText(WaterPurity.getPurity(variant))))
                    .append(" ")
                    .append(CommonProxy.getFluidName(fluidObject));

        }

        return CommonProxy.getFluidName(fluidObject);
    }

}
