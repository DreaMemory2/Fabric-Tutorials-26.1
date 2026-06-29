package com.crystal.util;

import net.minecraft.core.Direction;

public class EnumUtils {
    public static final Direction[] DIRECTIONS = Direction.values();
    // 水平方向：东南西北
    public static final Direction[] HORIZONTAL = new Direction[] {
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST,
            Direction.NORTH
    };
}
