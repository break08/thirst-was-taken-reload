package com.thirsty.api.create;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.CreateLang;
import com.thirsty.purity.CreateWaterPurity;
import com.thirsty.purity.WaterPurity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import me.shedaniel.autoconfig.AutoConfig;
import net.createmod.catnip.lang.LangBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import com.thirsty.api.config.CommonConfig;

import java.util.List;

public class SandFilterTileEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public static final int TANK_SIZE = 1000;
    public SmartFluidTankBehaviour dirtyTank;
    public SmartFluidTankBehaviour purifiedTank;

    private static final long MB = 81;

    public static CommonConfig config = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();

    public SandFilterTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        dirtyTank = SmartFluidTankBehaviour.single(this, TANK_SIZE * MB);
        behaviours.add(dirtyTank);
        purifiedTank = SmartFluidTankBehaviour.single(this, TANK_SIZE * MB);
        behaviours.add(purifiedTank);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0, -2, 0);
    }


    private boolean trackFoods() {
        return getBehaviour(AdvancementBehaviour.TYPE).isOwnerPresent();
    }

    /*

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side != null && side.getAxis() == Direction.Axis.Y) {
            if (side == Direction.DOWN)
                return purifiedTank.getCapability()
                        .cast();
            else
                return dirtyTank.getCapability()
                        .cast();
        }
        return super.getCapability(cap, side);
    }

     */

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide()) return;

        SmartFluidTank dirty = dirtyTank.getPrimaryHandler();
        SmartFluidTank purified = purifiedTank.getPrimaryHandler();
        long perTick = config.SAND_FILTER_MB_PER_TICK * MB;

        if (dirty.getFluidAmount() < perTick || purified.getFluidAmount() >= TANK_SIZE * MB) return;

        try (Transaction t = Transaction.openOuter()) {
            FluidStack water = drain(dirty, perTick, t);
            if (water.isEmpty()) return;

            if (water.getFluid().isSame(Fluids.WATER))
                CreateWaterPurity.addPurity(water, Math.min(
                        CreateWaterPurity.getPurity(water) + config.SAND_FILTER_FILTRATION_AMOUNT,
                        WaterPurity.MAX_PURITY));

            if (fill(purified, water, t) == water.getAmount())
                t.commit();
        }
    }



    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        CreateLang.translate("gui.goggles.fluid_container")
                .forGoggles(tooltip);

        int dirtyWaterAmount = (int) (dirtyTank.getPrimaryHandler().getFluidAmount() / MB);
        int purifiedWaterAmount = (int) (purifiedTank.getPrimaryHandler().getFluidAmount() / MB);

        buildTooltip(tooltip, mb, dirtyWaterAmount, dirtyTank);

        buildTooltip(tooltip, mb, purifiedWaterAmount, purifiedTank);

        if (dirtyTank.isEmpty() && purifiedTank.isEmpty()) {
            CreateLang.translate("gui.goggles.fluid_container.capacity")
                    .add(CreateLang.number((double) dirtyTank.getPrimaryHandler().getCapacity() / MB)
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
        }

        return !dirtyTank.isEmpty() || !purifiedTank.isEmpty();
    }



    private void buildTooltip(List<Component> tooltip, LangBuilder mb, int purifiedWaterAmount, SmartFluidTankBehaviour purifiedTank) {
        if (!purifiedTank.isEmpty()) {
            if (WaterPurity.getPurityText(WaterPurity.getPurity(purifiedTank.getPrimaryHandler().getFluid().getType())) == null){
                return;
            }
            CreateLang.builder()
                    .text(WaterPurity.getPurityText(WaterPurity.getPurity(purifiedTank.getPrimaryHandler().getFluid().getType())))
                    .add(CreateLang.text(" "))
                    .add(CreateLang.fluidName(purifiedTank.getPrimaryHandler().getFluid()))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);

            System.out.println("[SF-client] shown=" + WaterPurity.getPurity(purifiedTank.getPrimaryHandler().getFluid().getType())
                    + " nbt=" + purifiedTank.getPrimaryHandler().getFluid().getType().getNbt());

            CreateLang.builder()
                    .add(CreateLang.number(purifiedWaterAmount)
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(CreateLang.number((double) purifiedTank.getPrimaryHandler().getCapacity() / MB)
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);
        }
    }

    private static FluidStack drain(SmartFluidTank tank, long amount, Transaction t) {
        FluidVariant v = tank.getResource();
        if (v.isBlank()) return FluidStack.EMPTY;
        long got = tank.extract(v, amount, t);
        if (got <= 0) return FluidStack.EMPTY;
        return new FluidStack(v.getFluid(), got, v.copyNbt());
    }

    private static long fill(SmartFluidTank tank, FluidStack stack, Transaction t) {
        return tank.insert(FluidVariant.of(stack.getFluid(), stack.getTag()), stack.getAmount(), t);
    }

}
