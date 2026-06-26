package com.crystal.block;

import com.crystal.api.TickableBlockEntity;
import com.crystal.block.entity.*;
import com.crystal.register.ModDataComponents;
import com.crystal.util.SimpleFluidContent;
import com.crystal.util.FluidTankTier;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    private final FluidTankTier tier;

    public FluidTankBlock(FluidTankTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
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
        String id = state.getBlock().getDescriptionId();
        if (id.equals(ModBlocks.ADVANCED_FLUID_TANK.getDescriptionId())) {
            return new AdvancedFluidTankBlockEntity(pos, state);
        } else if (id.equals(ModBlocks.ELITE_FLUID_TANK.getDescriptionId())) {
            return new EliteFluidTankBlockEntity(pos, state);
        } else if (id.equals(ModBlocks.ULTIMATE_FLUID_TANK.getDescriptionId())) {
            return new UltimateFluidTankBlockEntity(pos, state);
        } else if (id.equals(ModBlocks.CREATIVE_FLUID_TANK.getDescriptionId())) {
            return new CreativeFluidTankBlockEntity(pos, state);
        } else {
            return new BasicFluidTankBlockEntity(pos, state);
        }
    }

    /**
     * <p>当方块放置时，将数据组件中的数据添加至方块实体</p>
     * <p>实现：玩家背包中的液体储罐放置时，加载液体储罐里的液体</p>
     */
    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity by, @NotNull ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
        // 获取液体储罐实体
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (itemStack.has(ModDataComponents.STORED_FLUID)) {
            // 通过方块物品获取数据组件
            SimpleFluidContent content = itemStack.get(ModDataComponents.STORED_FLUID);
            if (content == null) return;
            // 判断获取方块实体为液体储罐实体，且判断简单液体存储是否为空
            if (blockEntity instanceof FluidTankBlockEntity tankBlockEntity && !content.isEmpty()) {
                // 获取方块实体的单一存储系统
                SingleFluidStorage fluidStorage = tankBlockEntity.getFluidTank();
                try(Transaction transaction = Transaction.openOuter()) {
                    // 确保能够顺利转换数据，需要判断单一存储系统是否已经有液体
                    if (fluidStorage.isResourceBlank() && fluidStorage.getAmount() <= 0) {
                        // 向方块实体的单一存储系统添加液体
                        fluidStorage.insert(content.getFluidStorage().getResource(), content.getFluidStorage().getAmount(), transaction);
                    }
                    transaction.commit();
                }
            }
        }
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(@NotNull  BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof FluidTankBlockEntity blockEntity) {
            player.openMenu(blockEntity);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState blockState, @NotNull BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }

    public String getTier() {
        return tier.getName();
    }
}
