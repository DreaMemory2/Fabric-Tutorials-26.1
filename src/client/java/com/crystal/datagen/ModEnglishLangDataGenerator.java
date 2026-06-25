package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLangDataGenerator extends FabricLanguageProvider {

    public ModEnglishLangDataGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider provider, TranslationBuilder builder) {
        builder.add("container.crystalmod.fluid_tank", "Fluid Tank");
        builder.add("itemGroup.crystalmod.crystal", "Crystal Mod");
        builder.add("tooltip.crystalmod.fluid_empty", "Empty");
        // Block
        translateBlock(builder);
        // Item
        translateItem(builder);
    }

    private void translateBlock(TranslationBuilder builder) {
        builder.add(ModBlocks.BASIC_FLUID_TANK, "§aBasic Fluid Tank");
        builder.add(ModBlocks.ADVANCED_FLUID_TANK, "§cAdvanced Fluid Tank");
        builder.add(ModBlocks.ELITE_FLUID_TANK, "§bElite Fluid Tank");
        builder.add(ModBlocks.ULTIMATE_FLUID_TANK, "§dUltimate Fluid Tank");
        builder.add(ModBlocks.CREATIVE_FLUID_TANK, "Creative Fluid Tank");
    }

    private void translateItem(TranslationBuilder builder) {
    }
}
