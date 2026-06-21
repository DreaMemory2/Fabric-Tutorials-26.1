package com.crystal.component;

import com.crystal.CrystalMod;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class ModDataComponent {
    public static final DataComponentType<SimpleFluidContent> FLUID_SOLID = register("fluid_solid", builder ->
            builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.PACKET_CODEC));

    public static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, CrystalMod.of(id), builder.apply(DataComponentType.builder()).build());
    }
}
