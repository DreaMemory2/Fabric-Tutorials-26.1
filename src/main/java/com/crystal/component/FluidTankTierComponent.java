package com.crystal.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FluidTankTierComponent(String name, int tier) {
    public static final FluidTankTierComponent BASIC = new FluidTankTierComponent("basic",1);
    public static final FluidTankTierComponent ADVANCED = new FluidTankTierComponent("advanced", 2);
    public static final FluidTankTierComponent ELITE = new FluidTankTierComponent("elite", 3);
    public static final FluidTankTierComponent ULTIMATE = new FluidTankTierComponent("ultimate", 4);
    public static final FluidTankTierComponent CREATIVE = new FluidTankTierComponent("creative", 5);

    public static final Codec<FluidTankTierComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(FluidTankTierComponent::name),
                    Codec.INT.fieldOf("tier").forGetter(FluidTankTierComponent::tier)
            ).apply(instance, FluidTankTierComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidTankTierComponent> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            FluidTankTierComponent::name,
            ByteBufCodecs.INT,
            FluidTankTierComponent::tier,
            FluidTankTierComponent::new
    );

}
