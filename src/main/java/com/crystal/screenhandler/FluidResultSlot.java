package com.crystal.screenhandler;

import com.crystal.block.entity.FluidTankBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class FluidResultSlot extends Slot {
    private FluidTankBlockEntity blockEntity;
    private int removeCount;

    public FluidResultSlot(FluidTankBlockEntity blockEntity, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.blockEntity = blockEntity;
    }

    @NotNull
    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return true;
    }
}
