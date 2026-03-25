package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jetbrains.annotations.NotNull;

public class ModModelDataGenerator extends FabricModelProvider {

    public ModModelDataGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators model) {
        model.createTrivialCube(ModBlocks.RED_CRYSTAL);
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators model) {

    }
}
