package com.crystal.item;

import com.crystal.CrystalMod;
import com.crystal.block.ModBlocks;
import com.crystal.component.FluidTankTierComponent;
import com.crystal.component.ModDataComponents;
import com.crystal.component.SimpleFluidContent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item BASIC_FLUID_TANK = register("basic_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.BASIC_FLUID_TANK, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY)
                    .component(ModDataComponents.FLUID_TANK_TIER, FluidTankTierComponent.BASIC));
    public static final Item ADVANCED_FLUID_TANK = register("advanced_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.ADVANCED_FLUID_TANK, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY)
                    .component(ModDataComponents.FLUID_TANK_TIER, FluidTankTierComponent.ADVANCED));
    public static final Item ELITE_FLUID_TANK = register("elite_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.ELITE_FLUID_TANK, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY)
                    .component(ModDataComponents.FLUID_TANK_TIER, FluidTankTierComponent.ELITE));
    public static final Item ULTIMATE_FLUID_TANK = register("ultimate_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.ULTIMATE_FLUID_TANK, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY)
                    .component(ModDataComponents.FLUID_TANK_TIER, FluidTankTierComponent.ULTIMATE));
    public static final Item CREATIVE_FLUID_TANK = register("creative_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.CREATIVE_FLUID_TANK, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY)
                    .component(ModDataComponents.FLUID_TANK_TIER, FluidTankTierComponent.CREATIVE));

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, CrystalMod.of(name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    public static void init() {

    }
}
