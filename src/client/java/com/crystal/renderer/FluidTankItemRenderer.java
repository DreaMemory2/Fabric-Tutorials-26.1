package com.crystal.renderer;

import com.crystal.CrystalMod;
import com.crystal.register.ModDataComponents;
import com.crystal.util.RenderHelper;
import com.crystal.util.SimpleFluidContent;
import com.crystal.util.SimpleFluidStorage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * <p>通过设置特殊模型渲染，渲染方块物品中液体显示</p>
 * <p>例如：显示基础液体储罐的模型中液体渲染</p>
 * <pre><code>
 * {
 *   "type": "minecraft:special",
 *   "base": "crystalmod:item/basic_fluid_tank",
 *   "model": {
 *     "type": "crystalmod:fluid_tank"
 *   }
 * }
 * </code></pre>
 * @see <a href="https://zh.minecraft.wiki/w/物品模型映射#special">特殊模型</a>
 * @see <a href="https://github.com/Rearth/Oritech/blob/26.1/src/main/java/rearth/oritech/client/renderers/SmallTankItemRenderer.java">SmallTankItemRenderer</a>
 */
public class FluidTankItemRenderer implements SpecialModelRenderer<FluidTankItemRenderer.TankContents> {
    public static final Identifier ID = CrystalMod.of("fluid_tank");

    @Override
    public void submit(@Nullable TankContents argument, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (argument == null) return;

        matrices.pushPose();

        // 绘制基本方块物品模型
        matrices.pushPose();
        argument.baseModelState().submit(matrices, collector, light, overlay, outlineColor);
        matrices.popPose();

        // 液体内容图层 fluid content overlay
        if (argument.fluidSprite() != null) {
            FluidTankBlockRenderer.submitTankFluid(collector, matrices, argument.fluidSprite, argument.fluidColor, argument.fill, light, overlay);
        }

        matrices.popPose();
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> output) {
        // bounds for frustum culling
        output.accept(new Vector3f(0, 0, 0));
        output.accept(new Vector3f(1, 1.25f, 1));
    }

    @Override
    public @Nullable FluidTankItemRenderer.TankContents extractArgument(@NotNull ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        // 解决基本模型：Base Model
        BlockState blockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
        BlockModelRenderState renderState = new BlockModelRenderState();
        // NeoForge mc.getBlockModelResolver().update();
        new BlockModelResolver(mc.getModelManager()).update(renderState, blockState, BlockDisplayContext.create());
        // 获取数据组件
        SimpleFluidContent content = stack.getOrDefault(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY);
        // 如果液体储罐没有液体，则返回空储罐
        if (content.isEmpty()) return new TankContents(renderState, null, 0, 0);

        // 获取液体占容量百分比、液体纹理图
        SimpleFluidStorage fluidStorage = content.getFluidStorage();
        float fill = (float) fluidStorage.getAmount() / fluidStorage.getCapacity(fluidStorage.getResource());
        TextureAtlasSprite sprite = RenderHelper.getFluidSprite(fluidStorage.getResource().getFluid());
        // 着色颜色，例如：不同群系中的水的颜色，岩浆为-1
        int tintColor = FluidVariantRendering.getColor(fluidStorage.getResource(), mc.level, mc.player.getOnPos());
        return new TankContents(renderState, sprite, tintColor, fill);
    }

    // captured render state for the tank model and fluid overlay
    public record TankContents(BlockModelRenderState baseModelState, @Nullable TextureAtlasSprite fluidSprite, int fluidColor, float fill) {

    }

    // unbaked model holder registered to RegisterSpecialModelRendererEvent
    public static class Unbaked implements SpecialModelRenderer.Unbaked<TankContents> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<TankContents> bake(@NotNull BakingContext context) {
            return new FluidTankItemRenderer();
        }

        @NotNull
        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<TankContents>> type() {
            return MAP_CODEC;
        }
    }
}