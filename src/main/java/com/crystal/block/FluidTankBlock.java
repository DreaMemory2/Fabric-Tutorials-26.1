package com.crystal.block;

import com.crystal.api.TickableBlockEntity;
import com.crystal.block.entity.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class FluidTankBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public FluidTankBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    protected VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    /**
     * <p>当方块被破坏时，则方块内容器会掉落物品</p>
     * <p>映射图</p>
     * <p>void onStateReplaced(Block state, World world, BlockPos pos, BlockState newState, boolean moved)</p>
     */
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, boolean moved) {
        if (state.getBlock() != this) {
            if(world.getBlockEntity(pos) instanceof FluidTankBlockEntity blockEntity) {
                Containers.dropContents(world, pos, blockEntity.getInventory().items);
                // 比较器更新: https://hotpad100c.github.io/posts/ComparatorUpdate/
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FluidTankBlockEntity(pos, state);
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(@NotNull  BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof  FluidTankBlockEntity blockEntity) {
            player.openMenu(blockEntity);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState blockState, @NotNull BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
