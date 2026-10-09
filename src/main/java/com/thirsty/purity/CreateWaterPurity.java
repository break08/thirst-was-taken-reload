package com.thirsty.purity;

import com.thirsty.api.config.CommonConfig;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.nbt.CompoundTag;

public class CreateWaterPurity {
    // Purity helper for Create mod

    public static FluidStack addPurity(FluidStack fluid, int purity) {
        if (fluid.isEmpty()) return fluid;
        return new FluidStack(WaterPurity.addPurity(fluid.getType(), purity), fluid.getAmount());
    }

    public static int getPurity(FluidStack fluid) {
        return WaterPurity.getPurity(fluid.getType());
    }

    public static boolean hasPurity(FluidStack fluid) {
        return WaterPurity.hasPurity(fluid.getType());
    }
}
