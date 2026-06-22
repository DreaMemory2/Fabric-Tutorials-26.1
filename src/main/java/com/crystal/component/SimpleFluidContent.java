package com.crystal.component;

import com.crystal.util.SimpleFluidStorage;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.FluidState;

public class SimpleFluidContent {
    public static final SimpleFluidContent EMPTY = new SimpleFluidContent(SimpleFluidStorage.EMPTY);
    public static final Codec<SimpleFluidContent> CODEC = SimpleFluidStorage.CODEC
            .xmap(SimpleFluidContent::new, content -> content.fluidStorage);
    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleFluidContent> PACKET_CODEC = SimpleFluidStorage.PACKET_CODEC
            .map(SimpleFluidContent::new, content -> content.fluidStorage);

    private final SimpleFluidStorage fluidStorage;

    private SimpleFluidContent(SimpleFluidStorage fluidStorage) {
        this.fluidStorage = fluidStorage;
    }

    public static SimpleFluidContent copyOf(SingleFluidStorage fluidStorage) {
        long amount = fluidStorage.getAmount();
        long capacity = fluidStorage.getCapacity();
        FluidState fluidState = fluidStorage.getResource().getFluid().defaultFluidState();
        return new SimpleFluidContent(new SimpleFluidStorage(fluidState, amount, capacity));
    }

    public boolean isEmpty() {
        return this.fluidStorage.isEmpty();
    }

    public SimpleFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    public Component getFluidName() {
        return this.fluidStorage.getFluidName();
    }
}
