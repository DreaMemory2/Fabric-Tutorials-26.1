package com.crystal.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public enum FluidTankTier {
    BASIC("basic", 0x5FFFB8, 14L * FluidConstants.BUCKET),
    ADVANCED("advanced", 0xFF806A, 28L * FluidConstants.BUCKET),
    ELITE("elite", 0x4BF8FF, 56L * FluidConstants.BUCKET),
    ULTIMATE("ultimate", 0xF787FF, 112L * FluidConstants.BUCKET),
    CREATIVE("creative", 0x585858, Long.MAX_VALUE);

    private final String name;
    private final int color;
    private final long capacity;

    FluidTankTier(String name, int color, long capacity) {
        this.name = name;
        this.color = color;
        this.capacity = capacity;
    }

    public int getColor() {
        return color;
    }

    public int getColor(String name) {
        return FluidTankTier.valueOf(name.toUpperCase()).getColor();
    }

    public long getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }
}
