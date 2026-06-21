package com.crystal.loot;

import com.crystal.CrystalMod;
import com.crystal.util.NbtBlockLootFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;

public class ModLootContents {

    public static final MapCodec<NbtBlockLootFunction> NBT_BLOCK_LOOT_FUNCTION = register("nbt_block_loot_function", NbtBlockLootFunction.CODEC);

    public static <T extends LootItemConditionalFunction> MapCodec<T> register(String name, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, CrystalMod.of(name), codec);
    }

    public static void init() {

    }
}
