package com.crystal.item;

import com.crystal.CrystalMod;
import com.crystal.block.ModBlocks;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab CRYSTAL = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, CrystalMod.of("crystal")),
            FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.BASIC_FLUID_TANK))
                    .title(Component.translatable("itemGroup.crystalmod.crystal"))
                    .displayItems(group())
                    .build());

    private static CreativeModeTab.DisplayItemsGenerator group() {
        return (context, entries) -> {
            entries.accept(ModBlocks.BASIC_FLUID_TANK);
            entries.accept(ModBlocks.ADVANCED_FLUID_TANK);
            entries.accept(ModBlocks.ELITE_FLUID_TANK);
            entries.accept(ModBlocks.ULTIMATE_FLUID_TANK);
            entries.accept(ModBlocks.CREATIVE_FLUID_TANK);
        };
    }

    public static void init() {

    }
}
