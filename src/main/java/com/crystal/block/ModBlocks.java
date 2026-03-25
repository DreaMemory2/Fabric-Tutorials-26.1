package com.crystal.block;

import com.crystal.CrystalMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModBlocks {
    public static final Block RED_CRYSTAL = register("red_crystal", Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));

    private static Block register(String name, Properties properties) {
        return register(name, Block::new, properties);
    }

    public static Block register(String name, Function<Properties, Block> factory, Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, CrystalMod.of(name));
        Block block = Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
        register(block, BlockItem::new, new Item.Properties());
        return block;
    }

    public static void register(Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, block.builtInRegistryHolder().key().identifier());
        Item item = factory.apply(block, properties.setId(key));
        ((BlockItem) item).registerBlocks(Item.BY_BLOCK, item);
        Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void init() {

    }
}
