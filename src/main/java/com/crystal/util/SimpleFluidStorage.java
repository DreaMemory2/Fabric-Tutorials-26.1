package com.crystal.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

public class SimpleFluidStorage extends SingleFluidStorage {
    public static final SimpleFluidStorage EMPTY = new SimpleFluidStorage(Fluid.FLUID_STATE_REGISTRY.byId(0), 0, 0);
    public static final Codec<SimpleFluidStorage> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    FluidState.CODEC.fieldOf("fluid").forGetter(storage -> storage.fluidState),
                    ExtraCodecs.POSITIVE_LONG.fieldOf("amount").forGetter(storage -> storage.amount),
                    ExtraCodecs.POSITIVE_LONG.fieldOf("capacity").forGetter(storage -> storage.capacity)
            ).apply(instance, SimpleFluidStorage::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleFluidStorage> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(FluidState.CODEC),
            storage -> storage.fluidState,
            ByteBufCodecs.LONG,
            storage -> storage.amount,
            ByteBufCodecs.LONG,
            storage -> storage.capacity,
            SimpleFluidStorage::new
    );
    private final FluidState fluidState;
    private final long amount;
    private final long capacity;

    public SimpleFluidStorage(FluidState fluidState, long amount, long capacity) {
        this.fluidState = fluidState;
        this.amount = amount;
        this.capacity = capacity;
    }

    @Override
    public long getCapacity(FluidVariant variant) {
        return capacity;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @NotNull
    @Override
    public FluidVariant getResource() {
        return FluidVariant.of(fluidState.getType());
    }

    public boolean isEmpty() {
        return this == EMPTY || fluidState.isEmpty() || this.amount <= 0;
    }

    public Component getFluidName() {
        return Component.translatable(this.getResource().getFluid().defaultFluidState().createLegacyBlock().getBlock().getDescriptionId());
    }

    public static long getMB(long amount) {
        return (long) (float) (amount / FluidConstants.BUCKET) * 1000L;
    }
}
