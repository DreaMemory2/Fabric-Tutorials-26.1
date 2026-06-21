package com.crystal.network;

import com.crystal.CrystalMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record BlockPosPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<@NotNull BlockPosPayload> ID = new Type<>(CrystalMod.of("block_pos"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockPosPayload> PACKET_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, BlockPosPayload::pos, BlockPosPayload::new);

    @NotNull
    @Override
    public Type<? extends @NotNull CustomPacketPayload> type() {
        return ID;
    }
}
