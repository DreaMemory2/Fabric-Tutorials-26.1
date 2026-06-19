package com.crystal.screenhandler;

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

    public FluidTankScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, new SimpleContainer(2));
    }

    public FluidTankScreenHandler(int syncId, Inventory playerInventory, Container container) {
        super(ModScreenHandlers.FLUID_TANK, syncId);
        this.container = container;
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
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }
}
