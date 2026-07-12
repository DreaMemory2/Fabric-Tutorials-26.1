package com.crystal.item;

import com.crystal.CrystalMod;
import com.crystal.block.ModBlocks;
import com.crystal.item.juice.JuiceContents;
import com.crystal.item.juice.JuiceItem;
import com.crystal.item.juice.Juices;
import com.crystal.register.ModDataComponents;
import com.crystal.util.FluidTankTier;
import com.crystal.util.SimpleFluidContent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumables;

import java.util.function.Function;

public class ModItems {
    public static final Item BASIC_FLUID_TANK = register("basic_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.BASIC_FLUID_TANK, FluidTankTier.BASIC, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY));
    public static final Item ADVANCED_FLUID_TANK = register("advanced_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.ADVANCED_FLUID_TANK, FluidTankTier.ADVANCED, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY));
    public static final Item ELITE_FLUID_TANK = register("elite_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.ELITE_FLUID_TANK, FluidTankTier.ELITE, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY));
    public static final Item ULTIMATE_FLUID_TANK = register("ultimate_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.ULTIMATE_FLUID_TANK, FluidTankTier.ULTIMATE, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY));
    public static final Item CREATIVE_FLUID_TANK = register("creative_fluid_tank",
            properties -> new FluidTankItem(ModBlocks.CREATIVE_FLUID_TANK, FluidTankTier.CREATIVE, properties),
            new Item.Properties().stacksTo(1)
                    .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY));
    public static final Item JUICE_BOTTLE = register("juice_bottle", Item::new, new Item.Properties());
    public static final Item JUICE = register("juice", JuiceItem::new,
            new Item.Properties().stacksTo(16)
                    .component(ModDataComponents.JUICE, new JuiceContents(Juices.WATER))
                    .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .component(DataComponents.FOOD, new FoodProperties(2, 0.2f, true))
                    .usingConvertsTo(ModItems.JUICE_BOTTLE));

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, CrystalMod.of(name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    public static void init() {

    }
}
