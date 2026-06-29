package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import com.crystal.register.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagDataGenerator extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagDataGenerator(FabricPackOutput output, CompletableFuture<Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(@NotNull Provider registries) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.BASIC_FLUID_TANK)
                .add(ModBlocks.ADVANCED_FLUID_TANK)
                .add(ModBlocks.ELITE_FLUID_TANK)
                .add(ModBlocks.ULTIMATE_FLUID_TANK)
                .add(ModBlocks.CREATIVE_FLUID_TANK);

        valueLookupBuilder(ModBlockTags.CONNECTION_MECHANICAL_PIPE)
                .add(ModBlocks.BASIC_MECHANICAL_PIPE)
                .add(ModBlocks.ADVANCED_MECHANICAL_PIPE);
    }
}
