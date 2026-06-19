package com.crystal.screenhandler;

import com.crystal.CrystalMod;
import com.crystal.util.BlockPosPayload;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class ModScreenHandlers {

    public static final MenuType<@NotNull FluidTankScreenHandler> FLUID_TANK = register("fluid_tank", FluidTankScreenHandler::new);

    public static <T extends AbstractContainerMenu, D extends CustomPacketPayload> ExtendedMenuType<@NotNull T, D> register(String name, ExtendedMenuType.ExtendedFactory<@NotNull T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> codec) {
        return Registry.register(BuiltInRegistries.MENU, CrystalMod.of(name), new ExtendedMenuType<>(factory, codec));
    }

    public static <T extends AbstractContainerMenu> MenuType<@NotNull T> register(String name, MenuType.MenuSupplier<@NotNull T> constructor) {
        return Registry.register(BuiltInRegistries.MENU, name, new MenuType<>(constructor, FeatureFlagSet.of()));
    }

    public static void init() {

    }
}
