package com.crystal.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> CONNECTION_MECHANICAL_PIPE = register("connection_mechanical_pipe");

    public static TagKey<Block> register(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.parse(name));
    }
}
