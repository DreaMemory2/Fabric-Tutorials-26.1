package com.crystal.component;

import com.crystal.util.SimpleFluidStorage;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SimpleFluidContent {
    public static final Codec<SimpleFluidContent> CODEC = SimpleFluidStorage.CODEC
            .xmap(SimpleFluidContent::new, content -> (SimpleFluidStorage) content.fluidStorage);
    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleFluidContent> PACKET_CODEC = SimpleFluidStorage.PACKET_CODEC
            .map(SimpleFluidContent::new, content -> (SimpleFluidStorage) content.fluidStorage);

    private final SingleFluidStorage fluidStorage;

    private SimpleFluidContent(SingleFluidStorage fluidStorage) {
        this.fluidStorage = fluidStorage;
    }

    public static SimpleFluidContent copyOf(SingleFluidStorage fluidStorage) {
        return new SimpleFluidContent(fluidStorage);
    }
}
