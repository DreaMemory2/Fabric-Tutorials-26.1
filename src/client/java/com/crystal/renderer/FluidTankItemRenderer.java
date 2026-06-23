package com.crystal.renderer;

import com.crystal.CrystalMod;
import com.crystal.component.ModDataComponents;
import com.crystal.component.SimpleFluidContent;
import com.crystal.util.RenderHelpers;
import com.crystal.util.SimpleFluidStorage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/Rearth/Oritech/blob/26.1/src/main/java/rearth/oritech/client/renderers/SmallTankItemRenderer.java">SmallTankItemRenderer</a>
 */
public class FluidTankItemRenderer implements SpecialModelRenderer<FluidTankItemRenderer.TankContents> {
    public static final Identifier ID = CrystalMod.of("fluid_tank");

    private final Identifier tankVisualModelId;

    public FluidTankItemRenderer(Identifier tankVisualModelId) {
        this.tankVisualModelId = tankVisualModelId;
    }

    @Override
    public void submit(@Nullable TankContents argument, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (argument == null) return;

        matrices.pushPose();
        matrices.translate(0, 0.25, 0);
        matrices.scale(0.84f, 0.84f ,0.84f);

        // 初始模型 raw model
        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(0.9f, 0.9f, 0.9f);

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
        ItemStackRenderState baseState = new ItemStackRenderState();
        mc.getModelManager().getItemModel(tankVisualModelId).update(baseState, stack, mc.getItemModelResolver(), ItemDisplayContext.NONE, mc.level, null, 0);

        // 获取数据组件
        SimpleFluidContent content = stack.getOrDefault(ModDataComponents.STORED_FLUID, SimpleFluidContent.EMPTY);
        // 如果液体储罐没有液体，则返回空储罐
        if (content.isEmpty()) return new TankContents(baseState, null, 0, 0);

        // 获取液体占容量百分比、液体纹理图
        SimpleFluidStorage fluidStorage = content.getFluidStorage();
        float fill = (float) fluidStorage.getAmount() / fluidStorage.getCapacity(fluidStorage.getResource());
        TextureAtlasSprite sprite = RenderHelpers.getFluidSprite(fluidStorage.getResource().getFluid());
        // 着色颜色，例如：不同群系中的水的颜色，岩浆为-1
        int tintColor = FluidVariantRendering.getColor(fluidStorage.variant, mc.level, Objects.requireNonNull(mc.player).blockPosition());

        return new TankContents(baseState, sprite, tintColor, fill);
    }

    // captured render state for the tank model and fluid overlay
    public record TankContents(ItemStackRenderState baseModelState, @Nullable TextureAtlasSprite fluidSprite, int fluidColor, float fill) {

    }

    // unbaked model holder registered to RegisterSpecialModelRendererEvent
    public record Unbaked(Identifier model) implements SpecialModelRenderer.Unbaked<TankContents> {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model)
                ).apply(instance, Unbaked::new));

        @Override
        public SpecialModelRenderer<TankContents> bake(@NotNull BakingContext context) {
            return new FluidTankItemRenderer(this.model);
        }

        @NotNull
        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<TankContents>> type() {
            return MAP_CODEC;
        }
    }
}