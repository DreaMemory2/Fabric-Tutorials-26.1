package com.crystal;

import com.crystal.datagen.ModBlockTagDataGenerator;
import com.crystal.datagen.ModDefaultLangDataGenerator;
import com.crystal.datagen.ModChineseDataGenerator;
import com.crystal.datagen.ModModelDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CrystalModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(ModModelDataGenerator::new);
		pack.addProvider(ModChineseDataGenerator::new);
		pack.addProvider(ModDefaultLangDataGenerator::new);
		pack.addProvider(ModBlockTagDataGenerator::new);
	}
}
