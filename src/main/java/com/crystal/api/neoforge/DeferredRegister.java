package com.crystal.api.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegister<T> {
    private final String namespace;
    private final Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private final ResourceKey<? extends Registry<T>> registryKey;

    protected DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    public <I extends T> DeferredHolder<T, I> register(final String name, final Supplier<? extends I> sup) {
        return this.register(name, _ -> sup.get());
    }

    public <I extends T> DeferredHolder<T, I> register(final String name, final Function<Identifier, ? extends I> func) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);

        final Identifier key = Identifier.fromNamespaceAndPath(namespace, name);
        DeferredHolder<T, I> ret = createHolder(this.registryKey, key);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    protected <I extends T> DeferredHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, Identifier key) {
        return DeferredHolder.create(registryKey, key);
    }
}
