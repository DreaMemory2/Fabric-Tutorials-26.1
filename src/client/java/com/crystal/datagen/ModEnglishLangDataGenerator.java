package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import com.crystal.item.ModItems;
import com.crystal.item.juice.Juice;
import com.crystal.item.juice.Juices;
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
        builder.add("itemGroup.crystalmod.crystal", "Crystal Mod");
        builder.add("tooltip.crystalmod.detail.left_shift", "Press §9Left Shift§f to view details");
        builder.add("tooltip.crystalmod.description.left_shift", "Press §bShift + N§f to view description");
        builder.add("tooltip.crystalmod.fluid_tank.empty", "Empty");
        builder.add("tooltip.crystalmod.fluid_tank.fluid_tank_capacity", "Capacity");
        builder.add("tooltip.crystalmod.fluid_tank.capacity_infinity", "Infinity");
        // Block
        translateBlock(builder);
        // Item
        translateItem(builder);
        // 果汁
        createJuiceTranslate(builder, "Water", Juices.WATER.value());
        createJuiceTranslate(builder, "Carrot Juice", Juices.CARROT.value());
        createJuiceTranslate(builder, "Tea", Juices.TEA.value());
        createJuiceTranslate(builder, "Grape Juice", Juices.GRAPE.value());
        createJuiceTranslate(builder, "Apple Juice", Juices.APPLE.value());
        createJuiceTranslate(builder, "Vegetable Juice", Juices.VEGETABLE.value());
        createJuiceTranslate(builder, "Melon Juice", Juices.MELON.value());
        createJuiceTranslate(builder, "Golden Grape Juice", Juices.GOLDEN_GRAPE.value());
        createJuiceTranslate(builder, "Golden Apple Juice", Juices.GOLDEN_APPLE.value());
        createJuiceTranslate(builder, "Coke", Juices.COKE.value());
        createJuiceTranslate(builder, "Sprite", Juices.SPRITE.value());
        createJuiceTranslate(builder, "Milk Tea", Juices.MILK_TEA.value());
        createJuiceTranslate(builder, "Coffee", Juices.COFFEE.value());
        createJuiceTranslate(builder, "Chocolates Milk", Juices.CHOCOLATES_MILK.value());
        createJuiceTranslate(builder, "Chocolates Water", Juices.CHOCOLATES_WATER.value());
        createJuiceTranslate(builder, "Soy Milk", Juices.SOY_MILK.value());
        createJuiceTranslate(builder, "White Radish Juice", Juices.WHITE_RADISH.value());
        createJuiceTranslate(builder, "Tomato Juice", Juices.TOMATO.value());
        createJuiceTranslate(builder, "Corn Juice", Juices.CORN.value());
        createJuiceTranslate(builder, "Cucumber Juice", Juices.CUCUMBER.value());
        createJuiceTranslate(builder, "Pear Juice", Juices.PEAR.value());
        createJuiceTranslate(builder, "Lychee Juice", Juices.LYCHEE.value());
        createJuiceTranslate(builder, "Peach Juice", Juices.PEACH.value());
        createJuiceTranslate(builder, "Orange Juice", Juices.ORANGE.value());
        createJuiceTranslate(builder, "Loquat Juice", Juices.LOQUAT.value());
        createJuiceTranslate(builder, "Mango Juice", Juices.MANGO.value());
        createJuiceTranslate(builder, "Lemon Juice", Juices.LEMON.value());
        createJuiceTranslate(builder, "Grapefruit Juice", Juices.GRAPEFRUIT.value());
        createJuiceTranslate(builder, "Persimmon Juice", Juices.PERSIMMON.value());
        createJuiceTranslate(builder, "Papaya Juice", Juices.PAPAYA.value());
        createJuiceTranslate(builder, "Hawthorn Juice", Juices.HAWTHORN.value());
        createJuiceTranslate(builder, "Pomegranate Juice", Juices.POMEGRANATE.value());
        createJuiceTranslate(builder, "Chinese Date Juice", Juices.CHINESE_DATE.value());
        createJuiceTranslate(builder, "Strawberry Juice", Juices.STRAWBERRY.value());
        createJuiceTranslate(builder, "Coconut Juice", Juices.COCONUT.value());
        createJuiceTranslate(builder, "Chetty Juice", Juices.CHERRY.value());
        createJuiceTranslate(builder, "Banana Juice", Juices.BANANA.value());
        createJuiceTranslate(builder, "Coconut Milk", Juices.COCONUT_MILK.value());
    }

    private void createJuiceTranslate(TranslationBuilder builder, String name, Juice juice) {
        builder.add(ModItems.JUICE.asItem().getDescriptionId() + "." + juice.getName(), name);
    }

    private void translateBlock(TranslationBuilder builder) {
        builder.add(ModBlocks.BASIC_FLUID_TANK, "Basic Fluid Tank");
        builder.add(ModBlocks.ADVANCED_FLUID_TANK, "Advanced Fluid Tank");
        builder.add(ModBlocks.ELITE_FLUID_TANK, "Elite Fluid Tank");
        builder.add(ModBlocks.ULTIMATE_FLUID_TANK, "Ultimate Fluid Tank");
        builder.add(ModBlocks.CREATIVE_FLUID_TANK, "Creative Fluid Tank");
    }

    private void translateItem(TranslationBuilder builder) {
        builder.add(ModItems.JUICE_BOTTLE, "Juice Bottle");
    }
}
