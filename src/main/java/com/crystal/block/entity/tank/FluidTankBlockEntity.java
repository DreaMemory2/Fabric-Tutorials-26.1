package com.crystal.block.entity.tank;

import com.crystal.api.TickableBlockEntity;
import com.crystal.network.BlockPosPayload;
import com.crystal.screenhandler.FluidTankScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class FluidTankBlockEntity extends BaseContainerBlockEntity implements TickableBlockEntity, ExtendedMenuProvider<BlockPosPayload> {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final ContainerStorage inventoryStorage = ContainerStorage.of(this, null);
    /**
     * <p>单一流体存储</p>
     * <p>long capacity: 固有液体容量，为14桶</p>
     * <p>Runnable onChange: 当容器液体变化时，则通过markDirty()方法更新客户端并渲染液体</p>
     */
    private final SingleFluidStorage fluidStorage;

    public FluidTankBlockEntity(BlockEntityType<? extends BlockEntity> type, SingleFluidStorage fluidStorage, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
        this.fluidStorage = fluidStorage;
    }

    @Override
    public void tick() {
        // 只在服务端时触发
        if (level == null || level.isClientSide()) return;

        Storage<FluidVariant> input = ContainerItemContext.ofSingleSlot(inventoryStorage.getSlot(0)).find(FluidStorage.ITEM);
        Storage<FluidVariant> output = ContainerItemContext.ofSingleSlot(inventoryStorage.getSlot(1)).find(FluidStorage.ITEM);
        ContainerItemContext context = ContainerItemContext.ofSingleSlot(inventoryStorage.getSlot(1));

        /*--- 输入液体 ---*/
        if (input != null) inputFluid(input);

        /*--- 输出液体 ---*/
        if (output != null) outputFluid(output);
    }

    private void inputFluid(Storage<FluidVariant> itemFluidStorage) {
        for (StorageView<FluidVariant> view : itemFluidStorage.nonEmptyViews()) {
            // 判断输入的液体是否与容器内液体相同
            if (!this.fluidStorage.isResourceBlank()
                    && !view.getResource().isOf(this.fluidStorage.getResource().getFluid())) return;

            try(Transaction transaction = Transaction.openOuter()) {
                FluidVariant variant = view.getResource();
                long extract = itemFluidStorage.extract(variant, FluidConstants.BUCKET, transaction);
                this.fluidStorage.insert(variant, extract, transaction);

                transaction.commit();
            }
        }
    }

    private void outputFluid(Storage<FluidVariant> itemFluidStorage) {
        // 判断输入物品是否含有可提取的液体
        for (StorageView<FluidVariant> view : itemFluidStorage)
            if (!view.isResourceBlank()) return;
        // 是否输入两个或两个以上的物品
        if (this.items.get(1).getCount() >= 2) return;

        for (StorageView<FluidVariant> view : fluidStorage.nonEmptyViews()) {
            try(Transaction transaction = Transaction.openOuter()) {
                FluidVariant resource = view.getResource();
                long extract = this.fluidStorage.extract(resource, FluidConstants.BUCKET, transaction);
                itemFluidStorage.insert(resource, extract, transaction);

                transaction.commit();
            }
        }
    }

    /**
     * @param slot 槽位下标（设置输入槽的下标为0）
     * @param stack 物品
     * @return 检查槽位中物品是否合法物品（例如：岩浆桶，水桶等）
     */
    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        // 完全支持液体提取的检查
        // 例如：检查输入槽是否为空桶或输入槽为空槽
        if (stack.isEmpty()) return true;
        // 除了输入槽（下标为0）之外均无无效
        if (slot == 0) return true;
        if (slot == 1) return true;
        // 对液体存储物品访问，也就是说，从输入槽中物品提取液体，液体按照滴为单位存储
        // 例如：输出槽位中有水桶，从水桶中提取液体，能提取81000水滴
        Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
        return storage != null;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    /* -- NBT数据 -- */
    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        // 加载液体存储数据
        this.fluidStorage.readValue(input);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        // 保存液体存储数据
        this.fluidStorage.writeValue(output);
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
       this.items = items;
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new FluidTankScreenHandler(containerId, inventory, this, this);
    }

    /* 数据同步 */

    @Override
    public Packet<@NotNull ClientGamePacketListener> getUpdatePacket() {
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
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @NotNull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
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

    @Override
    public int getContainerSize() {
        return 2;
    }

    /**
     * @param player the player that is opening the screen
     * @return 方块位置的数据同步，通过网络发包形式传输
     */
    @NotNull
    @Override
    public BlockPosPayload getScreenOpeningData(@NotNull ServerPlayer player) {
        return new BlockPosPayload(this.getBlockPos());
    }
}
