package com.crystal.block.entity.tank;

import com.crystal.block.ModBlockEntityTypes;
import com.crystal.util.FluidTankTier;
import com.crystal.util.SimpleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedFluidTankBlockEntity extends FluidTankBlockEntity {

    public AdvancedFluidTankBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.ADVANCED_FLUID_TANK, SimpleFluidStorage.setFixedCapacity(FluidTankTier.ADVANCED.getCapacity()), worldPosition, blockState);
    }
}
