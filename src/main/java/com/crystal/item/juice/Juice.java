package com.crystal.item.juice;

import com.crystal.register.ModRegistries;
import com.crystal.register.ModRegistryKeys;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Juice implements FeatureElement {
    public static final Codec<Holder<Juice>> CODEC = ModRegistries.JUICE.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Juice>> PACKET_CODEC = ByteBufCodecs.holderRegistry(ModRegistryKeys.JUICE);
    private final String name;
    private final int color;
    private final List<MobEffectInstance> effects;

    public Juice(String name, int color, MobEffectInstance... effects) {
        this.name = name;
        this.color = color;
        this.effects = List.of(effects);
    }

    @NotNull
    @Override
    public FeatureFlagSet requiredFeatures() {
        return FeatureFlags.DEFAULT_FLAGS;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public List<MobEffectInstance> getEffects() {
        return effects;
    }

    public MobEffectInstance getEffect() {
        return effects.getFirst();
    }
}
