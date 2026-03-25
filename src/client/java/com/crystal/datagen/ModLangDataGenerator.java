package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModLangDataGenerator extends FabricLanguageProvider {

    public ModLangDataGenerator(FabricPackOutput output, CompletableFuture<Provider> registryLookup) {
        super(output, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(@NotNull Provider registryLookup, TranslationBuilder builder) {
        builder.add(ModBlocks.RED_CRYSTAL, "红色水晶块");

        builder.add("itemGroup.crystal_mod.crystal", "水晶模组");
    }
}
