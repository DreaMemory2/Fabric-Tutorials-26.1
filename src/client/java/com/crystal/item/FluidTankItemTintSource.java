package com.crystal.item;

import com.crystal.block.FluidTankBlock;
import com.crystal.util.FluidTankTier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * <p>渲染液体储罐中的刻度线，每个等级颜色：绿色、红色、蓝色、紫色、黑色</p>
 * <p>例如：基础液体储罐的刻度线绿色</p>
 * <pre><code>
 * {
 *   "type": "minecraft:special",
 *   "base": "crystalmod:item/basic_fluid_tank",
 *   "model": {
 *     "type": "crystalmod:fluid_tank"
 *   }
 * }
 * </code></pre>
 * @param color 刻度线的颜色
 * @see FluidTankItemTintSource#calculate(ItemStack, ClientLevel, LivingEntity)
 */
public record FluidTankItemTintSource(Integer color) implements ItemTintSource {
    public static final MapCodec<FluidTankItemTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(FluidTankItemTintSource::color)
            ).apply(instance, FluidTankItemTintSource::new)
    );

    @Override
    public int calculate(@NonNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        FluidTankBlock block = (FluidTankBlock) ((FluidTankItem) itemStack.getItem()).getBlock();
        for (FluidTankTier tier : FluidTankTier.values()) {
            if (tier.getName().equals(block.getTier())) {
                return ARGB.opaque(tier.getColor(tier.getName()));
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
