package com.crystal.datagen.provider;

import com.crystal.util.NbtBlockLootFunction;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public abstract class BlockLootTableProvider extends FabricBlockLootSubProvider {

    protected BlockLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    public LootTable.Builder createFluidTankBoxDrop(Block fluidTank) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(fluidTank, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .setBonusRolls(ConstantValue.exactly(0.0F))
                .add(LootItem.lootTableItem(fluidTank)
                        .apply(NbtBlockLootFunction.builder()))
                )
        );
    }
}
