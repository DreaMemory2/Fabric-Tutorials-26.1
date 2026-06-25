package com.crystal.block;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class FluidTankBlockTintSource implements BlockTintSource {
    @Override
    public int color(@NonNull BlockState state) {
        FluidTankBlock block = (FluidTankBlock) state.getBlock();
        return switch (block.getTier().toLowerCase()) {
            case "basic" -> ARGB.opaque(0x5FFFB8);
            case "advanced" -> ARGB.opaque(0xFF806A);
            case "elite" -> ARGB.opaque(0x4BF8FF);
            case "ultimate" -> ARGB.opaque(0xF787FF);
            case "creative" -> ARGB.opaque(0x585858);
            default -> -1;
        };
    }

    @Override
    public int colorInWorld(@NonNull BlockState state, @NonNull BlockAndTintGetter level, @NonNull BlockPos pos) {
        return color(state);
    }
}
