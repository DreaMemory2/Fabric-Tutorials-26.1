package com.crystal.register;

import com.crystal.item.juice.Juice;
import com.crystal.item.juice.Juices;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.function.Supplier;

public class ModRegistries {
    private static final Map<Identifier, Supplier<?>> LOADERS = Maps.newLinkedHashMap();
    public static final Registry<Juice> JUICE = register(ModRegistryKeys.JUICE, Juices::bootstrap);

    public static <T> Registry<T> register(ResourceKey<Registry<T>> key, RegistryBootstrap<T> loader) {
        MappedRegistry<T> registry = FabricRegistryBuilder.create(key).attribute(RegistryAttribute.SYNCED).buildAndRegister();
        LOADERS.put(key.identifier(), () -> loader.run(registry));
        return registry;
    }

    @FunctionalInterface
    public interface RegistryBootstrap<T> {
        Object run(Registry<T> registry);
    }
}
