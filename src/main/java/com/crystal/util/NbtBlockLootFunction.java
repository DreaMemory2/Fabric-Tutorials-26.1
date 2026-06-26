package com.crystal.util;

import com.crystal.block.entity.FluidTankBlockEntity;
import com.crystal.register.ModDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <p>实现方块被挖掘后，方块物品存储液体功能</p>
 * @see NbtBlockLootFunction#run(ItemStack itemstack, LootContext context)
 */
public class NbtBlockLootFunction extends LootItemConditionalFunction {
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
        // 存储液体数据，通过数据组件保存方块中液体的数据
        // 判断方块实体存储液体是否为空
        if (blockEntity instanceof FluidTankBlockEntity tankBlockEntity && tankBlockEntity.getFluidTank().amount > 0) {
            stack.set(ModDataComponents.STORED_FLUID, SimpleFluidContent.copyOf(tankBlockEntity.getFluidTank()));
            stack.set(DataComponents.MAX_STACK_SIZE, 1);
        }

        return stack;
    }

    public static Builder<?> builder() {
        return LootItemConditionalFunction.simpleBuilder(NbtBlockLootFunction::new);
    }
}
