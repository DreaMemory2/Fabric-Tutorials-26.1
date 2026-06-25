package com.crystal.item;

import com.crystal.component.FluidTankTierComponent;
import com.crystal.component.ModDataComponents;
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

public record FluidTankItemTintSource(Integer color) implements ItemTintSource {
    public static final MapCodec<FluidTankItemTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(fluidTank -> fluidTank.color)
            ).apply(instance, FluidTankItemTintSource::new)
    );

    @Override
    public int calculate(@NonNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        FluidTankTierComponent component = itemStack.get(ModDataComponents.FLUID_TANK_TIER);
        if (component == null) return -1;
        return switch (component.name()) {
            case "basic" -> ARGB.opaque(0x5FFFB8);
            case "advanced" -> ARGB.opaque(0xFF806A);
            case "elite" -> ARGB.opaque(0x4BF8FF);
            case "ultimate" -> ARGB.opaque(0xF787FF);
            case "creative" -> ARGB.opaque(0x585858);
            default -> -1;
        };
    }

    @NonNull
    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
