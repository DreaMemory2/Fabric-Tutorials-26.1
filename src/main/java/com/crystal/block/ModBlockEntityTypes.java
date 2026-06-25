package com.crystal.block;

import com.crystal.CrystalMod;
import com.crystal.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class ModBlockEntityTypes {
    public static final BlockEntityType<@NotNull FluidTankBlockEntity> BASIC_FLUID_TANK = register("basic_fluid_tank", FabricBlockEntityTypeBuilder.create(BasicFluidTankBlockEntity::new, ModBlocks.BASIC_FLUID_TANK));
    public static final BlockEntityType<@NotNull FluidTankBlockEntity> ADVANCED_FLUID_TANK = register("advanced_fluid_tank", FabricBlockEntityTypeBuilder.create(AdvancedFluidTankBlockEntity::new, ModBlocks.ADVANCED_FLUID_TANK));
    public static final BlockEntityType<@NotNull FluidTankBlockEntity> ELITE_FLUID_TANK = register("elite_fluid_tank", FabricBlockEntityTypeBuilder.create(EliteFluidTankBlockEntity::new, ModBlocks.ELITE_FLUID_TANK));
    public static final BlockEntityType<@NotNull FluidTankBlockEntity> ULTIMATE_FLUID_TANK = register("ultimate_fluid_tank", FabricBlockEntityTypeBuilder.create(UltimateFluidTankBlockEntity::new, ModBlocks.ULTIMATE_FLUID_TANK));
    public static final BlockEntityType<@NotNull FluidTankBlockEntity> CREATIVE_FLUID_TANK = register("creative_fluid_tank", FabricBlockEntityTypeBuilder.create(CreativeFluidTankBlockEntity::new, ModBlocks.CREATIVE_FLUID_TANK));

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, CrystalMod.of(name), builder.build());
    }

    public static void init() {

    }
}
