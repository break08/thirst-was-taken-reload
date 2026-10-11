package com.thirsty.api.create.ponder.scene;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class SandFilterScene {
    public static void filtering(SceneBuilder scene, SceneBuildingUtil util)
    {
        scene.title("sand_filter", "Purifying Water with a Sand Filter");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(5);

        Selection filter = util.select().position(2, 3, 2);
        BlockPos filterPos = util.grid().at(2, 3, 2);

        scene.world().showSection(filter, Direction.DOWN);
        scene.idle(5);

        Vec3 filterSide = util.vector().blockSurface(filterPos, Direction.WEST);
        scene.overlay().showText(80)
                .pointAt(filterSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Sand Filter purifies by one step water pumped through it");

        scene.idle(70);

        Selection gears = util.select().fromTo(3, 1, 2, 3, 5, 5);
        Selection bottomGear = util.select().position(3, 0, 5);
        Selection topTank = util.select().fromTo(2, 4, 2, 2, 5, 2);
        BlockPos topTankPos = util.grid().at(2, 5, 2);

        scene.world().showSection(topTank, Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(gears, Direction.WEST);
        scene.world().showSection(bottomGear, Direction.WEST);
        scene.idle(5);

        FluidStack content = new FluidStack(Fluids.WATER, 4000);
        scene.world().modifyBlockEntity(topTankPos, FluidTankBlockEntity.class, be -> {
            try(Transaction t = Transaction.openOuter()){
                fill(be.getTankInventory(), content, t);
                t.commit();
            }
        });

        Vec3 topTankSide = util.vector().blockSurface(topTankPos, Direction.EAST);
        scene.overlay().showText(60)
                .pointAt(topTankSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Dirty Water needs to be pumped in from the top...");

        scene.idle(30);

        Selection bottomTank = util.select().fromTo(2, 1, 2, 2, 2, 2);
        BlockPos bottomTankPos = util.grid().at(2, 1, 2);

        scene.world().showSection(bottomTank, Direction.DOWN);
        scene.idle(30);

        Vec3 bottomTankSide = util.vector().blockSurface(bottomTankPos, Direction.WEST);
        scene.overlay().showText(60)
                .pointAt(bottomTankSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("and Purified Water can be pumped out from the bottom");

        scene.idle(10);

        scene.world().modifyBlockEntity(topTankPos, FluidTankBlockEntity.class, be -> {
                try (Transaction t = Transaction.openOuter()){
                    drain(be.getTankInventory(), 4000, t);
                    t.commit();
                }
            }
        );

        scene.world().modifyBlockEntity(bottomTankPos, FluidTankBlockEntity.class, be -> {
            try (Transaction t = Transaction.openOuter()) {
                fill(be.getTankInventory(), content, t);
                t.commit();
            }
        });

        scene.idle(50);
    }

    public static FluidStack drain(FluidTank tank, long amount, Transaction t) {
        FluidVariant v = tank.getResource();
        if (v.isBlank()) return FluidStack.EMPTY;
        long got = tank.extract(v, amount, t);
        return got <= 0 ? FluidStack.EMPTY : new FluidStack(v, got);
    }

    public static long fill(FluidTank tank, FluidStack stack, Transaction t) {
        return tank.insert(stack.getType(), stack.getAmount(), t);
    }
}
