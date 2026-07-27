package com.crystal.api.neoforge;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T> {
    protected final ResourceKey<R> key;
    private Holder<R> holder = null;

    protected DeferredHolder(ResourceKey<R> key) {
        this.key = Objects.requireNonNull(key);
        this.bind(false);
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends Registry<R>> registryKey, Identifier valueName) {
        return create(ResourceKey.create(registryKey, valueName));
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<R> key) {
        return new DeferredHolder<>(key);
    }

    @Override
    public T get() {
        return this.value();
    }

    @NotNull
    @Override
    public T value() {
        bind(true);
        if (this.holder == null) {
            throw new NullPointerException("Trying to access unbound value: " + this.key);
        }
        return (T) this.holder.value();
    }

    protected final void bind(boolean throwOnMissingRegistry) {
        if (this.holder != null) return;

        Registry<R> registry = getRegistry();
        if (registry != null) {
            this.holder = registry.get(this.key).orElse(null);
        } else if (throwOnMissingRegistry) {
            throw new IllegalStateException("Registry not present for " + this + ": " + this.key.registry());
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected Registry<R> getRegistry() {
        return (Registry<R>) BuiltInRegistries.REGISTRY.getValue(this.key.registry());
    }

    @Override
    public boolean isBound() {
        bind(false);
        return this.holder != null && this.holder.isBound();
    }

    @Override
    public boolean areComponentsBound() {
        bind(false);
        return this.holder != null && this.holder.areComponentsBound();
    }

    @Override
    public boolean is(@NotNull Identifier key) {
        return key.equals(this.key.identifier());
    }

    @Override
    public boolean is(@NotNull ResourceKey<R> key) {
        return key == this.key;
    }

    @Override
    public boolean is(@NotNull Predicate<ResourceKey<R>> filter) {
        return filter.test(this.key);
    }

    @Override
    public boolean is(@NotNull TagKey<R> tag) {
        bind(false);
        return this.holder != null && this.holder.is(tag);
    }

    @Override
    public boolean is(@NotNull Holder<R> holder) {
        bind(false);
        return this.holder != null && this.holder.is(holder);
    }

    @NotNull
    @Override
    public Stream<TagKey<R>> tags() {
        bind(false);
        return this.holder != null ? this.holder.tags() : Stream.empty();
    }

    @NotNull
    @Override
    public DataComponentMap components() {
        bind(true);
        return this.holder != null ? this.holder.components() : DataComponentMap.EMPTY;
    }

    @NotNull
    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return Either.left(this.key);
    }

    @NotNull
    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return Optional.of(this.key);
    }

    @NotNull
    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(@NotNull HolderOwner<R> registry) {
        bind(false);
        return this.holder != null && this.holder.canSerializeIn(registry);
    }
}
