package com.crystal.block.entity.tank;

import com.crystal.block.ModBlockEntityTypes;
import com.crystal.util.FluidTankTier;
import com.crystal.util.SimpleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeFluidTankBlockEntity extends FluidTankBlockEntity {

    public CreativeFluidTankBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.CREATIVE_FLUID_TANK, SimpleFluidStorage.setFixedCapacity(FluidTankTier.CREATIVE.getCapacity()), worldPosition, blockState);
    }
}
