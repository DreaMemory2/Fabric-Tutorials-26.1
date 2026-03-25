package com.crystal;

import com.crystal.block.ModBlocks;
import com.crystal.item.ModItemGroups;
import com.crystal.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class CrystalMod implements ModInitializer {
	public static final String MOD_ID = "crystal_mod";

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();

		ModItemGroups.init();
	}

	public static Identifier of(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}