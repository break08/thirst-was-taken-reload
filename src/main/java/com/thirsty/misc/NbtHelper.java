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
    // stack.getTag() / getOrCreateTag()
    public static CompoundTag get(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void set(ItemStack stack, CompoundTag tag) {
        if (tag.isEmpty()) stack.remove(DataComponents.CUSTOM_DATA);
        else stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static void edit(ItemStack stack, Consumer<CompoundTag> action) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, action);
    }

    // hasTag()
    public static boolean has(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
    }

    public static CompoundTag get(FluidVariant variant) {
        Optional<? extends CustomData> opt =
                variant.getComponents().get(DataComponents.CUSTOM_DATA);
        return (opt != null && opt.isPresent()) ? opt.get().copyTag() : new CompoundTag();
    }

    public static boolean has(FluidVariant variant) {
        Optional<? extends CustomData> opt =
                variant.getComponents().get(DataComponents.CUSTOM_DATA);
        return opt != null && opt.isPresent();
    }

    public static FluidVariant edit(FluidVariant variant, Consumer<CompoundTag> action) {
        CompoundTag tag = get(variant);
        action.accept(tag);
        return set(variant, tag);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static FluidVariant set(FluidVariant variant, CompoundTag tag) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();

        for (var e : variant.getComponents().entrySet()) {
            if (e.getKey() == DataComponents.CUSTOM_DATA) continue;
            if (e.getValue().isPresent()) {
                builder.set((DataComponentType) e.getKey(), e.getValue().get());
            } else {
                builder.remove(e.getKey());
            }
        }

        if (!tag.isEmpty()) {
            builder.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return FluidVariant.of(variant.getFluid(), builder.build());
    }
}
