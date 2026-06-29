package com.crystal.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public abstract class ConnectionPipeBlock extends Block {
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    public final Function<BlockState, VoxelShape> center;
    public final Function<BlockState, VoxelShape> side;

    public ConnectionPipeBlock(Properties properties, VoxelShape center, VoxelShape hSide, VoxelShape vSide) {
        super(properties);
        this.center = makeShapes(center, cube(0), cube(0));
        this.side = makeShapes(center, hSide, vSide);
    }

    @NotNull
    @Override
    protected VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.center.apply(state);
    }

    @NotNull
    @Override
    protected VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.center.apply(state);
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@Nullable BlockState state) {
        return cube(8);
    }

    protected Function<BlockState, VoxelShape> makeShapes(VoxelShape center, VoxelShape hSide, VoxelShape vSide) {
        return this.getShapeForEachState(_ -> {
            VoxelShape shape = center;

            for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
                if (isVerticalDirection(entry.getKey())) {
                    shape = Shapes.or(center, vSide);
                } else  {
                    shape = Shapes.or(center, hSide);
                }
            }

            return shape;
        });
    }

    public boolean isVerticalDirection(Direction direction) {
        if (Direction.UP.equals(direction)) return true;
        return Direction.DOWN.equals(direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, @NotNull BlockState> builder) {
        builder.add(EAST, SOUTH, WEST, NORTH, UP, DOWN);
    }
}
