package com.crystal.item;

import com.crystal.register.ModDataComponents;
import com.crystal.util.FluidTankTier;
import com.crystal.util.SimpleFluidContent;
import com.crystal.util.SimpleFluidStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class FluidTankItem extends BlockItem {
    private final FluidTankTier tier;

    public FluidTankItem(Block block, FluidTankTier tier, Properties properties) {
        super(block, properties.useBlockDescriptionPrefix());
        this.tier = tier;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        SimpleFluidContent data = itemStack.getOrDefault(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY);

        if (data.isEmpty()) {
            // 如果液体储罐没有液体，会显示为“空”
            builder.accept(Component.translatable("tooltip.crystalmod.fluid_tank.empty").withStyle(ChatFormatting.DARK_RED));
        } else {
            // 液体的名称及容量
            builder.accept(data.getFluidStorage().getFluidName().copy().append(": ").setStyle(Style.EMPTY.withColor(0xD55ECB))
                    .append(Component.literal("%s".formatted(SimpleFluidStorage.getMB(data.getFluidStorage().getAmount()))).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                    .append(Component.literal(" mB").setStyle(Style.EMPTY.withColor(0xD55ECB))));

        }
        // 是否为创造储罐
        Component capacity = tier.getCapacity() == Long.MAX_VALUE ?
                Component.translatable("tooltip.crystalmod.fluid_tank.capacity_infinity") :
                Component.literal(String.valueOf(SimpleFluidStorage.getMB(tier.getCapacity())));
        // 存储的容量
        builder.accept(Component.translatable("tooltip.crystalmod.fluid_tank.fluid_tank_capacity").append(": ").setStyle(Style.EMPTY.withColor(0x559EFF))
                .append(capacity.copy().setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                .append(Component.literal(" mB").withStyle(Style.EMPTY.withColor(0x559EFF))));
        // 按住左Shift键查看详情
        builder.accept(Component.translatable("tooltip.crystalmod.detail.left_shift"));
        // 按住Shift+N可查看描述
        builder.accept(Component.translatable("tooltip.crystalmod.description.left_shift"));
    }

    @NotNull
    @Override
    public Component getName(ItemStack stack) {
        SimpleFluidContent content = stack.getOrDefault(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY);
        Style style = Style.EMPTY;
        if (tier != null) {
            style = style.withColor(tier.getColor());
            return super.getName(stack).copy().setStyle(style);
        }
        return super.getName(stack);
    }
}
