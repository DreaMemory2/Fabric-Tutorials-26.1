package com.crystal.register;

import com.crystal.CrystalMod;
import com.crystal.item.juice.Juice;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistryKeys {
    public static final ResourceKey<Registry<Juice>> JUICE = register("juice");

    public static <T> ResourceKey<Registry<T>> register(String name) {
        return ResourceKey.createRegistryKey(CrystalMod.of(name));
    }
}
