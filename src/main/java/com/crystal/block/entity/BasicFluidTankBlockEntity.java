package com.crystal.block.entity;

import com.crystal.block.ModBlockEntityTypes;
import com.crystal.util.FluidTankTier;
import com.crystal.util.SimpleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicFluidTankBlockEntity extends FluidTankBlockEntity {

    public BasicFluidTankBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.BASIC_FLUID_TANK, SimpleFluidStorage.setFixedCapacity(FluidTankTier.BASIC.getCapacity()), worldPosition, blockState);
    }
}
