package com.crystal.util;

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

    /**
     * <p>不能通过强制类型转换为{@code SimpleFluidStorage}类，这样导致液体存储对象为null</p>
     * @param fluidStorage 单一液体存储
     * @return 将输入液体存储复制到简单液体存储中，再通过Codec存储数据
     */
    public static SimpleFluidContent copyOf(SingleFluidStorage fluidStorage) {
        long amount = fluidStorage.getAmount();
        long capacity = fluidStorage.getCapacity();
        FluidState fluidState = fluidStorage.getResource().getFluid().defaultFluidState();
        return new SimpleFluidContent(new SimpleFluidStorage(fluidState, amount, capacity));
    }

    /**
     * @return 判断液体是否为空
     * @see SimpleFluidStorage#isEmpty()
     */
    public boolean isEmpty() {
        return this.fluidStorage.isEmpty();
    }

    public SimpleFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    /**
     * @return 获取液体名称，例如：block.minecraft.water: "水"
     * @see SimpleFluidStorage#getFluidName()
     */
    public Component getFluidName() {
        return this.fluidStorage.getFluidName();
    }
}
