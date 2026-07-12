package com.crystal.datagen;

import com.crystal.CrystalMod;
import com.crystal.item.FluidTankItemTintSource;
import com.crystal.item.ModItems;
import com.crystal.item.juice.JuiceItemTintSource;
import com.crystal.renderer.FluidTankItemRenderer;
import com.crystal.util.FluidTankTier;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ModModelDataGenerator extends FabricModelProvider {

    public ModModelDataGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockModelGenerators model) {
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators model) {
        model.generateFlatItem(ModItems.JUICE_BOTTLE, ModelTemplates.FLAT_ITEM);

        createFluidTankModel(model, ModItems.BASIC_FLUID_TANK, FluidTankTier.BASIC.getColor());
        createFluidTankModel(model, ModItems.ADVANCED_FLUID_TANK, FluidTankTier.ADVANCED.getColor());
        createFluidTankModel(model, ModItems.ELITE_FLUID_TANK, FluidTankTier.ELITE.getColor());
        createFluidTankModel(model, ModItems.ULTIMATE_FLUID_TANK, FluidTankTier.ULTIMATE.getColor());
        createFluidTankModel(model, ModItems.CREATIVE_FLUID_TANK, FluidTankTier.CREATIVE.getColor());

        createJuiceItemModel(model);
    }

    private static void createJuiceItemModel(ItemModelGenerators model) {
        Item item = ModItems.JUICE;
        Identifier overlayModel = model.generateLayeredItem(item, new Material(CrystalMod.of("item/juice_overlay")), TextureMapping.getItemTexture(item));
        model.itemModelOutput.accept(item, ItemModelUtils.tintedModel(overlayModel, new JuiceItemTintSource()));
    }

    private static void createFluidTankModel(ItemModelGenerators model, Item item, int color) {
        FluidTankItemRenderer.Unbaked unbaked = new FluidTankItemRenderer.Unbaked();
        ItemModel.Unbaked tintedModel = ItemModelUtils.tintedModel(ModelLocationUtils.getModelLocation(item), new FluidTankItemTintSource(color));
        ItemModel.Unbaked fluidTankUnbaked = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(item), unbaked);
        model.itemModelOutput.accept(item, ItemModelUtils.composite(tintedModel, fluidTankUnbaked));
    }
}
