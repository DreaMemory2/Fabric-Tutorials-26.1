package com.crystal.util;

import com.crystal.CrystalMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * <p>创建一个类，名为方块位置的有效载荷；有效载荷是作为实际预期消息的传输数据的一部分</p>
 * @param pos 方块位置
 */
public record BlockPosPayload(BlockPos pos) implements CustomPacketPayload {
    /* ID是一个自定义有效载荷的标识符，定义为block_pos */
    public static final Type<@NotNull BlockPosPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(CrystalMod.MOD_ID, "block_pos"));
    /**
     * <p>自定义数据包编码器，泛型为字节发包和方块位置有效载荷</p>
     * <p>数据包编解码器将接收方块位置，然后用方块的位置去新建有效载荷</p>
     * <p>这个编码器允许序列化和反序列转为不同的格式，将其编码为注册字节缓冲区，用来访问注册表</p>
     * <p>需要我们将这个记录类，去导入到我们的方块实体类中，然后实现它的方法</p>
     */
    public static final StreamCodec<ByteBuf, BlockPosPayload> PACKET_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, BlockPosPayload::pos, BlockPosPayload::new);

    @NotNull
    @Override
    public Type<? extends @NotNull CustomPacketPayload> type() {
        return ID;
    }
}