package com.crystal;

import com.crystal.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CrystalModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(ModModelDataGenerator::new);
		pack.addProvider(ModChineseDataGenerator::new);
		pack.addProvider(ModEnglishLangDataGenerator::new);
		pack.addProvider(ModBlockTagDataGenerator::new);
		pack.addProvider(ModRecipeDataGenerator::new);
		pack.addProvider(ModBlockLootTableDataGenerator::new);
	}
}
