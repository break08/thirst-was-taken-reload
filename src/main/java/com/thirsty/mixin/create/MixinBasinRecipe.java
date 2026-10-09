package com.thirsty.mixin.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BasinRecipe.class)
public class MixinBasinRecipe {
    private static final ThreadLocal<Integer> THIRST_PURITY = ThreadLocal.withInitial(() -> -1);

    @Inject(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z",
            at = @At("HEAD"), remap = false)
    private static void thirst$capture(BasinBlockEntity basin, Recipe<?> recipe, boolean test,
                                       CallbackInfoReturnable<Boolean> cir) {
        THIRST_PURITY.set(readPurity(basin));
    }

    @ModifyArg(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z",
            index = 1, remap = false,
            at = @At(value = "INVOKE",
                    target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;acceptOutputs(Ljava/util/List;Ljava/util/List;Lnet/fabricmc/fabric/api/transfer/v1/transaction/TransactionContext;)Z"))
    private static List<FluidStack> thirst$applyPurity(List<FluidStack> fluids) {
        int purity = THIRST_PURITY.get();
        if (purity < 0) return fluids;

        for (int i = 0; i < fluids.size(); i++) {
            FluidStack f = fluids.get(i);
            if (f.getFluid().isSame(AllFluids.TEA.get()))
                fluids.set(i, CreateWaterPurity.addPurity(f, Math.min(purity, WaterPurity.MAX_PURITY)));
        }
        return fluids;
    }

    private static int readPurity(BasinBlockEntity basin) {
        Storage<FluidVariant> fluids = basin.getFluidStorage(null);
        if (fluids == null) return -1;
        for (StorageView<FluidVariant> view : fluids.nonEmptyViews()) {
            FluidVariant v = view.getResource();
            if (WaterPurity.hasPurity(v)) return WaterPurity.getPurity(v);
        }
        return -1;
    }
}