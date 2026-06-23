package com.crystal.datagen;

import com.crystal.CrystalMod;
import com.crystal.item.ModItems;
import com.crystal.renderer.FluidTankItemRenderer;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ModModelDataGenerator extends FabricModelProvider {

    public ModModelDataGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators model) {
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators model) {
        model.generateFlatItem(ModItems.RUBY, ModelTemplates.FLAT_ITEM);

        createFluidTankModel(model, ModItems.FLUID_TANK);
    }

    private static void createFluidTankModel(ItemModelGenerators model, Item item) {
        FluidTankItemRenderer.Unbaked unbaked = new FluidTankItemRenderer.Unbaked(CrystalMod.of("item/fluid_tank"));
        ItemModel.Unbaked fluidTankUnbaked = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(item), unbaked);
        model.itemModelOutput.accept(item, fluidTankUnbaked);
    }
}
