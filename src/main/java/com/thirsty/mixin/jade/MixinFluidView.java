package com.thirsty.mixin.jade;

import com.thirsty.purity.WaterPurity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.FluidView;
import snownee.jade.util.CommonProxy;

import java.util.Objects;

@Mixin(value = FluidView.class,remap = false)
public class MixinFluidView {
    @Redirect(method ="readDefault", at = @At(value = "INVOKE",target = "Lsnownee/jade/util/CommonProxy;getFluidName(Lsnownee/jade/api/fluid/JadeFluidObject;)Lnet/minecraft/network/chat/Component;"))
    private static Component read(JadeFluidObject fluidObject){
        Fluid instance = fluidObject.getType();
        CompoundTag tag = fluidObject.getTag();
        FluidVariant variant = FluidVariant.of(instance, tag);

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
