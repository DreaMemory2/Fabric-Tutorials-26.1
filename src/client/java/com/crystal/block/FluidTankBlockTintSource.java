package com.crystal.block;

import com.crystal.util.FluidTankTier;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

/**
 * <p>渲染液体存储方块上刻度线的颜色</p>
 * @see com.crystal.item.FluidTankItemTintSource FluidTankItemTintSource
 */
public class FluidTankBlockTintSource implements BlockTintSource {
    @Override
    public int color(@NonNull BlockState state) {
        FluidTankBlock block = (FluidTankBlock) state.getBlock();
        for (FluidTankTier tier : FluidTankTier.values()) {
            if (tier.getName().equals(block.getTier())) {
                return ARGB.opaque(tier.getColor(tier.getName()));
            }
        }
        return -1;
    }
}
