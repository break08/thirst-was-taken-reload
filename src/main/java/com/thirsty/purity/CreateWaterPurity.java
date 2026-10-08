package com.thirsty.purity;

import com.thirsty.api.config.CommonConfig;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.nbt.CompoundTag;

public class CreateWaterPurity {
    // Purity helper for Create mod

    public static FluidStack addPurity(FluidStack fluid, int purity)
    {
        CompoundTag tag = fluid.getTag();

        if (tag == null){
            CompoundTag newTag = fluid.getOrCreateTag();
            newTag.putInt("Purity", purity);
            return fluid;
        }

        tag.putInt("Purity", purity);

        return fluid;
    }

    public static int getPurity(FluidStack fluid)
    {
        if(fluid.getTag() == null || !fluid.getTag().contains("Purity"))
            return AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY;

        return fluid.getTag().getInt("Purity");
    }

    public static boolean hasPurity(FluidStack fluid)
    {
        if(!fluid.hasTag() || fluid.getTag() == null)
            return false;
        else
            return fluid.getTag().contains("Purity");
    }
}
