package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModChineseDataGenerator extends FabricLanguageProvider {

    public ModChineseDataGenerator(FabricPackOutput output, CompletableFuture<Provider> registryLookup) {
        super(output, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(@NotNull Provider registryLookup, TranslationBuilder builder) {
        builder.add("container.crystalmod.fluid_tank", "液体储罐");
        builder.add("itemGroup.crystalmod.crystal", "水晶模组");
        builder.add("tooltip.crystalmod.fluid_empty", "空");
        // 方块
        translateBlock(builder);
        // 物品
        translateItem(builder);
    }

    private void translateBlock(TranslationBuilder builder) {
        builder.add(ModBlocks.BASIC_FLUID_TANK, "§a基础 液体储罐");
        builder.add(ModBlocks.ADVANCED_FLUID_TANK, "§c高级 液体储罐");
        builder.add(ModBlocks.ELITE_FLUID_TANK, "§b精英 液体储罐");
        builder.add(ModBlocks.ULTIMATE_FLUID_TANK, "§d终极 液体储罐");
        builder.add(ModBlocks.CREATIVE_FLUID_TANK, "创造 液体储罐");
    }

    private void translateItem(TranslationBuilder builder) {
    }
}
