package com.crystal.item;

import com.crystal.CrystalMod;
import com.crystal.component.ModDataComponents;
import com.crystal.component.SimpleFluidContent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

    public static final Item FLUID_TANK = register("fluid_tank", FluidTankItem::new, new Item.Properties().stacksTo(1)
            .component(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY)
    );

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, CrystalMod.of(name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    public static void init() {

    }
}
