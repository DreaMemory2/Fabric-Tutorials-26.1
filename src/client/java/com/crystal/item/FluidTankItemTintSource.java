package com.crystal.item;

import com.crystal.util.FluidTankTier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record FluidTankItemTintSource(Integer color) implements ItemTintSource {
    public static final MapCodec<FluidTankItemTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(FluidTankItemTintSource::color)
            ).apply(instance, FluidTankItemTintSource::new)
    );

    @Override
    public int calculate(@NonNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        for (FluidTankTier tier : FluidTankTier.values()) {
            if (tier.getColor(tier.getName()) == color) return tier.getColor();
        }
        return -1;
    }

    @NonNull
    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
