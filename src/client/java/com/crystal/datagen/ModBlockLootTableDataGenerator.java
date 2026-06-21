package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import com.crystal.datagen.provider.BlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableDataGenerator extends BlockLootTableProvider {

    public ModBlockLootTableDataGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        this.add(ModBlocks.FLUID_TANK, this::createFluidTankBoxDrop);
        createFluidTankBoxDrop(ModBlocks.FLUID_TANK);
    }
}
