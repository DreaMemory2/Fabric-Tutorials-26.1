package com.crystal;

import com.crystal.block.FluidTankBlockTintSource;
import com.crystal.block.ModBlockEntityTypes;
import com.crystal.block.ModBlocks;
import com.crystal.item.FluidTankItemTintSource;
import com.crystal.item.juice.JuiceItemTintSource;
import com.crystal.renderer.FluidTankBlockRenderer;
import com.crystal.renderer.FluidTankItemRenderer;
import com.crystal.screen.FluidTankScreen;
import com.crystal.screenhandler.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CrystalModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(CrystalModClient.class);

	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModScreenHandlers.FLUID_TANK, FluidTankScreen::new);

		BlockEntityRenderers.register(ModBlockEntityTypes.BASIC_FLUID_TANK, FluidTankBlockRenderer::new);
		BlockEntityRenderers.register(ModBlockEntityTypes.ADVANCED_FLUID_TANK, FluidTankBlockRenderer::new);
		BlockEntityRenderers.register(ModBlockEntityTypes.ELITE_FLUID_TANK, FluidTankBlockRenderer::new);
		BlockEntityRenderers.register(ModBlockEntityTypes.ULTIMATE_FLUID_TANK, FluidTankBlockRenderer::new);
		BlockEntityRenderers.register(ModBlockEntityTypes.CREATIVE_FLUID_TANK, FluidTankBlockRenderer::new);

		SpecialModelRenderers.ID_MAPPER.put(FluidTankItemRenderer.ID, FluidTankItemRenderer.Unbaked.MAP_CODEC);
		ItemTintSources.ID_MAPPER.put(CrystalMod.of("fluid_tank"), FluidTankItemTintSource.CODEC);
		ItemTintSources.ID_MAPPER.put(CrystalMod.of("juice"), JuiceItemTintSource.MAP_CODEC);

		BlockColorRegistry.register(List.of(new FluidTankBlockTintSource()),
				ModBlocks.BASIC_FLUID_TANK,
				ModBlocks.ADVANCED_FLUID_TANK,
				ModBlocks.ELITE_FLUID_TANK,
				ModBlocks.ULTIMATE_FLUID_TANK,
				ModBlocks.CREATIVE_FLUID_TANK
		);

	}
}