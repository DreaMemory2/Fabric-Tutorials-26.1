package com.crystal.screenhandler;

import com.crystal.block.entity.FluidTankBlockEntity;
import com.crystal.network.BlockPosPayload;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FluidTankScreenHandler extends AbstractContainerMenu {
    private final Container container;
    public final FluidTankBlockEntity blockEntity;

    /**
     * <p>客户端，需要把同步数据（位置数据）传入{@code getBlockEntity}方法中</p>
     */
    public FluidTankScreenHandler(int syncId, Inventory inventory, BlockPosPayload payload) {
        this(syncId, inventory, new SimpleContainer(2), (FluidTankBlockEntity) inventory.player.level().getBlockEntity(payload.pos()));
    }

    // 服务端
    public FluidTankScreenHandler(int syncId, Inventory playerInventory, Container container, FluidTankBlockEntity blockEntity) {
        super(ModScreenHandlers.FLUID_TANK, syncId);
        this.container = container;
        this.blockEntity = blockEntity;
        checkContainerSize(container, 2);
        // 添加输入槽
        this.addSlot(new Slot(container, 0, 143, 19) {
            /**
             * 调用{@code inventory}中的{@link Inventory#canPlaceItem(int slot, ItemStack stack)}方法
             */
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return container.canPlaceItem(0, stack);
            }
        });
        // 添加输出槽
        this.addSlot(new Slot(container, 1, 143, 50));
        // 物品栏
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return container.stillValid(player);
    }

    public FluidTankBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
