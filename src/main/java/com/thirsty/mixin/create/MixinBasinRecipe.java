package com.thirsty.mixin.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(BasinRecipe.class)
public class MixinBasinRecipe {
    @Inject(
            method = {"apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z"},
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z",
                    ordinal = 0
            ),
            remap = false)
    private static void setPurity(BasinBlockEntity basin, Recipe<?> recipe, boolean test, CallbackInfoReturnable<Boolean> cir)
    {
        int purity = getWaterPurity(basin);
        NonNullList<FluidStack> outputFluids = ((BasinRecipe) recipe).getFluidResults();

        Pattern pattern = Pattern.compile("tea", Pattern.CASE_INSENSITIVE);

        outputFluids.forEach(fluid ->
        {
            Matcher matcher =  pattern.matcher(fluid.toString());
            if(matcher.find()) {
                CreateWaterPurity.addPurity(fluid, Math.min(purity, WaterPurity.MAX_PURITY));
            }
        });
    }

    private static int getWaterPurity(BasinBlockEntity basin)
    {
        Storage<FluidVariant> availableFluids =
                FluidStorage.SIDED.find(basin.getLevel(), basin.getBlockPos(), null);

        if(availableFluids == null)
            return WaterPurity.MAX_PURITY;

        for (StorageView<FluidVariant> view : availableFluids) {
            if (view.isResourceBlank()) continue;

            FluidVariant variant = view.getResource();
            if (WaterPurity.hasPurity(variant))
                return WaterPurity.getPurity(variant);
        }

        return -1;
    }
}