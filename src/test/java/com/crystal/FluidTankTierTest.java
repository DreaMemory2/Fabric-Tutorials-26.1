package com.crystal;

import com.crystal.block.FluidTankBlock;
import com.crystal.block.ModBlocks;
import com.crystal.util.FluidTankTier;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FluidTankTierTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    public void run() {
        for (FluidTankTier tier : FluidTankTier.values()) {
            CrystalMod.LOGGER.info("等级为：{}", tier.getName());
        }
        FluidTankBlock block = (FluidTankBlock) ModBlocks.BASIC_FLUID_TANK;
        CrystalMod.LOGGER.info("方块等级为：{}", block.getTier());

        // 获取等级颜色
        CrystalMod.LOGGER.info("方块等级名称：{}", FluidTankTier.valueOf("basic".toUpperCase()));
    }
}
