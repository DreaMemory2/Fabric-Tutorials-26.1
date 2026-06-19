package com.crystal.block.entity;

import com.crystal.api.TickableBlockEntity;
import com.crystal.block.ModBlockEntityTypes;
import com.crystal.screenhandler.FluidTankScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class FluidTankBlockEntity extends BaseContainerBlockEntity implements TickableBlockEntity {
    private final SimpleContainer inventory = new SimpleContainer(2) {
        /**
         * <p>映射表</p>
         * <p>Yarn: void markDirty()</p>
         */
        @Override
        public void setChanged() {
            super.setChanged();
            update();
        }

        /**
         * 调用{@link FluidTankBlockEntity#isValid(ItemStack itemStack, int slot)}方法
         */
        @Override
        public boolean canPlaceItem(int slot, @NotNull ItemStack itemStack) {
            return FluidTankBlockEntity.this.isValid(itemStack, slot);
        }
    };
    private final ContainerStorage inventoryStorage = ContainerStorage.of(inventory, null);
    /**
     * <p>单一流体存储</p>
     * <p>long capacity: 固有液体容量，为14桶</p>
     * <p>Runnable onChange: 当容器液体变化时，则通过markDirty()方法更新客户端并渲染液体</p>
     */
    private final SingleFluidStorage fluidStorage = SingleFluidStorage.withFixedCapacity(FluidConstants.BUCKET * 14, this::update);
    /**
     * <p>提供单一液体槽位，防止容器为空时，输入槽转换成其他物品</p>
     */
    private final ContainerItemContext fluidItemContext = ContainerItemContext.ofSingleSlot(inventoryStorage.getSlot(0));

    public FluidTankBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.FLUID_TANK, worldPosition, blockState);
    }

    @Override
    public void tick() {
        // 只在服务端时触发
        if (level == null || level.isClientSide()) return;
        // 首先：检查容器内输入槽是否为空，槽位中的物品是否有效
        if(inventory.isEmpty() || !isValid(inventory.getItem(0), 0)) return;
        // 然后，获取液体存储，注意：液体存储的容量为动态（可能为空可能已存液体），所以提供单一输出槽，不能转换物品
        // 如果通过物品提取液体，导致输入物品转换其他物品，例如：输入岩浆桶且容器存取水，则转变为水桶再存储液体
        Storage<FluidVariant> fluidStorage = this.fluidItemContext.find(FluidStorage.ITEM);
        // 确保液体存储不为空
        if (fluidStorage == null) return;

        // 遍历查找所以物品，找到这个物品可以提取液体（事务模拟）
        FluidVariant match = null;
        for (StorageView<FluidVariant> storageView : fluidStorage.nonEmptyViews()) {
            // 如果存储节点为空，跳出循环
            if (storageView.isResourceBlank()) continue;
            try(Transaction transaction = Transaction.openOuter()) {
                // 假如输入是桶物品时，则获取桶式存储节点
                if (this.fluidStorage.insert(storageView.getResource(), FluidConstants.BUCKET, transaction) > 0) {
                    match = storageView.getResource();
                    break;
                }
            }
        }

        if (match == null || match.isBlank()) return;

        // 如果成功查找物品（桶），则提取桶中的液体
        try(Transaction transaction = Transaction.openOuter()) {
            long inserted = this.fluidStorage.insert(match, FluidConstants.BUCKET, transaction);
            long extracted = fluidStorage.extract(match, inserted, transaction);
            if (extracted < FluidConstants.BUCKET) {
                long extra = FluidConstants.BUCKET - extracted;
                // 移除多余的液体
                this.fluidStorage.extract(match, extra, transaction);
            }

            transaction.commit();
        }

    }

    /**
     * @param stack 物品
     * @param slot 槽位下标（设置输入槽的下标为0）
     * @return 检查槽位中物品是否合法物品（例如：岩浆桶，水桶等）
     */
    public boolean isValid(ItemStack stack, int slot) {
        // 完全支持液体提取的检查
        // 例如：检查输入槽是否为空桶或输入槽为空槽
        if (stack.isEmpty()) return true;
        // 除了输入槽（下标为0）之外均无无效
        if (slot != 0) return false;
        // 对液体存储物品访问，也就是说，从输入槽中物品提取液体，液体按照滴为单位存储
        // 例如：输出槽位中有水桶，从水桶中提取液体，能提取81000水滴
        Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
        return storage != null;
    }

    private void update() {
        setChanged();
        if(level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    /* -- NBT数据 -- */
    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.inventory.items);
        // 加载液体存储数据
        this.fluidStorage.readValue(input);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.inventory.items);
        // 保存液体存储数据
        this.fluidStorage.writeValue(output);
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new FluidTankScreenHandler(containerId, inventory, this.inventory);
    }

    @Override
    public @Nullable Packet<@NotNull ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        return saveWithFullMetadata(registries);
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.crystalmod.fluid_tank");
    }

    @NotNull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory.getItems();
    }

    public ContainerStorage getInventoryProvider(Direction direction) {
        return this.inventoryStorage;
    }

    public SingleFluidStorage getFluidStorage(Direction direction) {
        return this.fluidStorage;
    }

    public SingleFluidStorage getFluidTank() {
        return this.fluidStorage;
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
