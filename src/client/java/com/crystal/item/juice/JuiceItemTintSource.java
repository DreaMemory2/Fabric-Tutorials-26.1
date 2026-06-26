package com.crystal.item.juice;

import com.crystal.register.ModDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public record JuiceItemTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<JuiceItemTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(JuiceItemTintSource::defaultColor)
            ).apply(instance, JuiceItemTintSource::new));

    public JuiceItemTintSource() {
        this(-13083194);
    }

    @Override
    public int calculate(@NotNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        JuiceContents contents = itemStack.get(ModDataComponents.JUICE);
        return contents != null ? ARGB.opaque(contents.getColor(this.defaultColor)) : ARGB.opaque(this.defaultColor);
    }

    @NotNull
    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
