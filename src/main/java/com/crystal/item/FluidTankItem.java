package com.crystal.item;

import com.crystal.block.ModBlocks;
import com.crystal.component.ModDataComponents;
import com.crystal.component.SimpleFluidContent;
import com.crystal.util.SimpleFluidStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class FluidTankItem extends BlockItem {

    public FluidTankItem(Properties properties) {
        super(ModBlocks.FLUID_TANK, properties.useBlockDescriptionPrefix());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        SimpleFluidContent data = itemStack.getOrDefault(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY);

        if (data.isEmpty()) {
            builder.accept(Component.translatable("tooltip.crystalmod.fluid_empty"));
        } else {
            // 液体的容量占比
            builder.accept(Component.literal("%s / %s mB".formatted(SimpleFluidStorage.getMB(data.getFluidStorage().getAmount()), SimpleFluidStorage.getMB(data.getFluidStorage().getCapacity()))).withStyle(ChatFormatting.GRAY));
        }
    }

    @NotNull
    @Override
    public Component getName(ItemStack stack) {
        SimpleFluidContent content = stack.getOrDefault(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY);
        if (content.isEmpty()) {
            // 如果液体容量为空，则显示液体名称为 液体储罐
            return super.getName(stack);
        } else {
            // 液体名称 + 物品名称，例如：水 液体储罐
            return content.getFluidName().copy().append(Component.literal(" ")).append(super.getName(stack));
        }
    }
}
