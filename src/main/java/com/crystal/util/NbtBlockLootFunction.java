package com.crystal.util;

import com.crystal.block.entity.FluidTankBlockEntity;
import com.crystal.component.ModDataComponent;
import com.crystal.component.SimpleFluidContent;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NbtBlockLootFunction extends LootItemConditionalFunction {
    public static final String NAME = "nbt_block_loot_function";
    public static final MapCodec<NbtBlockLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> LootItemConditionalFunction.commonFields(instance).apply(instance, NbtBlockLootFunction::new));

    protected NbtBlockLootFunction(List<LootItemCondition> predicates) {
        super(predicates);
    }

    @NotNull
    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    @NotNull
    @Override
    protected ItemStack run(@NotNull ItemStack stack, LootContext context) {
        // 获取方块实体
        BlockEntity blockEntity = context.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        // 存储液体数据
        if (blockEntity instanceof FluidTankBlockEntity tankBlockEntity) {
            stack.set(ModDataComponent.FLUID_SOLID, SimpleFluidContent.copyOf(tankBlockEntity.getFluidTank()));
        }

        return stack;
    }

    public static Builder<?> builder() {
        return LootItemConditionalFunction.simpleBuilder(NbtBlockLootFunction::new);
    }
}
