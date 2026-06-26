package com.crystal;

import com.crystal.block.ModBlockEntityTypes;
import com.crystal.block.ModBlocks;
import com.crystal.block.entity.FluidTankBlockEntity;
import com.crystal.item.ModItemGroups;
import com.crystal.item.ModItems;
import com.crystal.register.ModDataComponents;
import com.crystal.register.ModLootContents;
import com.crystal.screenhandler.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrystalMod implements ModInitializer {
	public static final String MOD_ID = "crystalmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(CrystalMod.class);

    @Override
	public void onInitialize() {
		ModBlocks.init();
		ModBlockEntityTypes.init();
		ModItems.init();
		ModItemGroups.init();
		ModScreenHandlers.init();
		ModLootContents.init();
		ModDataComponents.init();

		ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> ((FluidTankBlockEntity) blockEntity).getInventoryProvider(direction),
				ModBlockEntityTypes.BASIC_FLUID_TANK,
				ModBlockEntityTypes.ADVANCED_FLUID_TANK,
				ModBlockEntityTypes.ELITE_FLUID_TANK,
				ModBlockEntityTypes.ULTIMATE_FLUID_TANK,
				ModBlockEntityTypes.CREATIVE_FLUID_TANK
		);
		FluidStorage.SIDED.registerForBlockEntities(((blockEntity, direction) -> ((FluidTankBlockEntity) blockEntity).getFluidStorage(direction)),
				ModBlockEntityTypes.BASIC_FLUID_TANK,
				ModBlockEntityTypes.ADVANCED_FLUID_TANK,
				ModBlockEntityTypes.ELITE_FLUID_TANK,
				ModBlockEntityTypes.ULTIMATE_FLUID_TANK,
				ModBlockEntityTypes.CREATIVE_FLUID_TANK
		);
	}

	public static Identifier of(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}