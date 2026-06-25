package com.crystal.block;

import com.crystal.CrystalMod;
import com.crystal.util.FluidTankTier;
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
    public static final Block FLUID_TANK = registerWithoutBlockItem("fluid_tank", properties -> new FluidTankBlock(FluidTankTier.BASIC, properties), Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final Block BASIC_FLUID_TANK = registerWithoutBlockItem("basic_fluid_tank", properties -> new FluidTankBlock(FluidTankTier.BASIC, properties), Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final Block ADVANCED_FLUID_TANK = registerWithoutBlockItem("advanced_fluid_tank", properties -> new FluidTankBlock(FluidTankTier.ADVANCED, properties), Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final Block ELITE_FLUID_TANK = registerWithoutBlockItem("elite_fluid_tank", properties -> new FluidTankBlock(FluidTankTier.ELITE, properties), Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final Block ULTIMATE_FLUID_TANK = registerWithoutBlockItem("ultimate_fluid_tank", properties -> new FluidTankBlock(FluidTankTier.ULTIMATE, properties), Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final Block CREATIVE_FLUID_TANK = registerWithoutBlockItem("creative_fluid_tank", properties -> new FluidTankBlock(FluidTankTier.CREATIVE, properties), Properties.ofFullCopy(Blocks.IRON_BLOCK));

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
