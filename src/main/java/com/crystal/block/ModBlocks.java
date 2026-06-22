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

import java.util.function.Function;

public class ModBlocks {
    public static final Block FLUID_TANK = registerWithoutBlockItem("fluid_tank", FluidTankBlock::new, Properties.ofFullCopy(Blocks.IRON_BLOCK));

    /**
     * <p>没有方块物品注册方法，例如：下界传送门方块，作物方块等</p>
     * <p>请参见ModItem中的register方法</p>
     */
    private static Block registerWithoutBlockItem(String name, Function<Properties, Block> factory, Properties properties) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, CrystalMod.of(name));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));
    }

    private static Block register(String name, Function<Properties, Block> factory, Properties properties) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, CrystalMod.of(name));
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, CrystalMod.of(name));
        // 通过属性构建方块
        Block block = factory.apply(properties.setId(blockKey));
        // 使用方块前缀作为物品方块名称，例如：block.crystalmod.stone
        BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
        // 注册方块和方块物品
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void init() {

    }
}
