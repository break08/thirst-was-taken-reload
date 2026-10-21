package com.thirsty.misc;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Optional;
import java.util.function.Consumer;

public class NbtHelper {
    private NbtHelper() {}

    public static CompoundTag get(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void set(ItemStack stack, CompoundTag tag) {
        if (tag.isEmpty()) stack.remove(DataComponents.CUSTOM_DATA);
        else stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }


    // ---------- FluidVariant ----------
    private static Optional<? extends CustomData> customData(FluidVariant v) {
        Optional<? extends CustomData> opt = v.getComponents().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().read();
        return opt == null ? Optional.empty() : opt;
    }


    public static CompoundTag get(FluidVariant variant) {
        return customData(variant).map(CustomData::copyTag).orElseGet(CompoundTag::new);
    }


}
