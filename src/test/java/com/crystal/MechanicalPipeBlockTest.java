package com.crystal;

import com.crystal.block.ModBlocks;
import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MechanicalPipeBlockTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    public void run() {
        Block block = ModBlocks.BASIC_MECHANICAL_PIPE;

        CrystalMod.LOGGER.info("Mechanical Pipe Block: {}", block);
    }

    @Test
    public void info() {
        Map<Direction, BooleanProperty> m1 = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((e) -> ((Direction)e.getKey()).getAxis().isHorizontal()).collect(Util.toMap());
        Map<Direction, BooleanProperty> m2 = PipeBlock.PROPERTY_BY_DIRECTION;

        CrystalMod.LOGGER.info(m1.toString());
        CrystalMod.LOGGER.info(m2.toString());
    }
}
