package com.crystal;

import com.crystal.block.ModBlockEntityTypes;
import com.crystal.renderer.FluidTankBlockRenderer;
import com.crystal.screen.FluidTankScreen;
import com.crystal.screenhandler.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrystalModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(CrystalModClient.class);

	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModScreenHandlers.FLUID_TANK, FluidTankScreen::new);
		BlockEntityRenderers.register(ModBlockEntityTypes.FLUID_TANK, FluidTankBlockRenderer::new);
	}
}