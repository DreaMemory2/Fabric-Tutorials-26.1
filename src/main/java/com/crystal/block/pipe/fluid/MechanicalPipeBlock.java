package com.crystal.block.pipe.fluid;

import com.crystal.block.pipe.ConnectionPipeBlock;
import com.crystal.register.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class MechanicalPipeBlock extends ConnectionPipeBlock {
    private static final VoxelShape CENTER = box(4, 4, 4, 12, 12, 12);
    private static final VoxelShape H_SIDE = box(12, 4, 4, 16, 12, 12);
    private static final VoxelShape V_SIDE = box(4, 0, 4, 12, 16, 12);

    public MechanicalPipeBlock(Properties properties) {
        super(properties, CENTER, H_SIDE, V_SIDE);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(NORTH, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return super.getStateForPlacement(context)
                .setValue(EAST, this.connectsTo(world.getBlockState(pos.east())))
                .setValue(SOUTH, this.connectsTo(world.getBlockState(pos.south())))
                .setValue(WEST, this.connectsTo(world.getBlockState(pos.west())))
                .setValue(NORTH, this.connectsTo(world.getBlockState(pos.north())))
                .setValue(UP, this.connectsTo(world.getBlockState(pos.above())))
                .setValue(DOWN, this.connectsTo(world.getBlockState(pos.below())));
    }

    @NotNull
    @Override
    protected BlockState updateShape(BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess ticks, @NotNull BlockPos pos, @NotNull Direction directionToNeighbour, @NotNull BlockPos neighbourPos, @NotNull BlockState neighbourState, @NotNull RandomSource random) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(directionToNeighbour), this.connectsTo(neighbourState));
    }

    public boolean connectsTo(BlockState state) {
        return state.is(ModBlockTags.CONNECTION_MECHANICAL_PIPE);
    }
}
