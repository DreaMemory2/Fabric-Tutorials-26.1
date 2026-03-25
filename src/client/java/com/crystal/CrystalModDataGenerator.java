package com.crystal;

import com.crystal.datagen.ModBlockTagDataGenerator;
import com.crystal.datagen.ModLangDataGenerator;
import com.crystal.datagen.ModModelDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CrystalModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(ModModelDataGenerator::new);
		pack.addProvider(ModLangDataGenerator::new);
		pack.addProvider(ModBlockTagDataGenerator::new);
	}
}
