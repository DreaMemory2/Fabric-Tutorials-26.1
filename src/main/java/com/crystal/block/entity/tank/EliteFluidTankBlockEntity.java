package com.crystal.block.entity.tank;

import com.crystal.block.ModBlockEntityTypes;
import com.crystal.util.FluidTankTier;
import com.crystal.util.SimpleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EliteFluidTankBlockEntity extends FluidTankBlockEntity {

    public EliteFluidTankBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.ELITE_FLUID_TANK, SimpleFluidStorage.setFixedCapacity(FluidTankTier.ELITE.getCapacity()), worldPosition, blockState);
    }
}
