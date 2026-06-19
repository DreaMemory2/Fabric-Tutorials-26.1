package com.crystal.block;

import com.crystal.CrystalMod;
import com.crystal.block.entity.FluidTankBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class ModBlockEntityTypes {
    public static final BlockEntityType<@NotNull FluidTankBlockEntity> FLUID_TANK = register("fluid_tank", FabricBlockEntityTypeBuilder.create(FluidTankBlockEntity::new, ModBlocks.FLUID_TANK));

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, CrystalMod.of(name), builder.build());
    }

    public static void init() {

    }
}
