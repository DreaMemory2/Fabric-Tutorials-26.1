package com.crystal.renderer;

import com.crystal.block.entity.tank.FluidTankBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

public class FluidTankBlockRenderer implements BlockEntityRenderer<@NotNull FluidTankBlockEntity, FluidTankBlockRenderer.@NotNull TankRenderState> {

    public FluidTankBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public TankRenderState createRenderState() {
        return new TankRenderState();
    }

    @Override
    public void extractRenderState(FluidTankBlockEntity entity, @NotNull TankRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        SingleFluidStorage fluidTank = entity.getFluidTank();
        // 如果液体储罐为空，不渲染液体
        if (fluidTank.isResourceBlank() || fluidTank.amount <= 0) return;

        // 液体种类
        FluidVariant fluidVariant = fluidTank.getResource();
        // 液体数量（滴为单位）
        long amount = fluidTank.getAmount();
        // 储罐容量
        long capacity = fluidTank.getCapacity();
        // 液体占容器百分比：已存液体 / 存储容量 (0 - 100%]
        float fillPercentage = (float) amount / capacity;
        // 约束（钳制）：百分比约束在0到1之间
        fillPercentage = Mth.clamp(fillPercentage, 0, 1);
        state.fill = fillPercentage;

        // 获取颜色，给流体上颜色，例如：水在特定群系中有不同颜色，沼泽水和海洋水
        state.color = FluidVariantRendering.getColor(fluidVariant, (ClientLevel) entity.getLevel(), entity.getBlockPos());
        // 获取静态精灵图（静态纹理图）
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidVariant.getFluid().defaultFluidState());
        state.sprite = fluidModel.stillMaterial().sprite();
    }

    @Override
    public void submit(TankRenderState state, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        TextureAtlasSprite sprite = state.sprite;
        if (sprite == null) return;

        submitTankFluid(collector, matrices, sprite, state.color, state.fill, state.lightCoords, OverlayTexture.NO_OVERLAY);
    }

    public static void submitTankFluid(@NotNull SubmitNodeCollector collector, @NotNull PoseStack matrices, TextureAtlasSprite sprite, int color, float fill, int light, int overlay) {
        // 储罐中总体积
        float l = 12f / 16f;
        // 纹理图的高度位置
        float minY = 2f / 16f;
        float maxY = fill * (14f / 16f);
        // 设置液体颜色着色：例如水的不同群系中颜色，其中岩浆液体颜色为-1
        QuadInstance instance = new QuadInstance();
        instance.setColor(color);

        matrices.pushPose();
        // 截取液体精灵图中的纹理（防止纹理拉深、变形）
        float v0 = sprite.getV(minY);
        float v1 = sprite.getV(maxY);
        float u0 = sprite.getU(2f / 16f);
        float u1 = sprite.getU(12f / 16f);
        // 绘制液体纹理四个面（东南西北）+顶面
        collector.submitCustomGeometry(matrices, Sheets.translucentBlockSheet(), (pose, vertex) -> {
            east(pose, vertex, u0, v0, u1, v1, sprite, instance, light, maxY);
            south(pose, vertex, u0, v0, u1, v1, sprite, instance, light, maxY);
            west(pose, vertex, u0, v0, u1, v1, sprite, instance, light, maxY);
            north(pose, vertex, u0, v0, u1, v1, sprite, instance, light, maxY);
            if (fill < 1)
                up(pose, vertex, sprite, instance, light, maxY);
        });

        matrices.popPose();
    }

    /* 上部 */
    private static void up(PoseStack.Pose pose, VertexConsumer vertex, TextureAtlasSprite sprite, QuadInstance instance, int light, float y2) {
        float v0 = sprite.getV(2f / 16f);
        float v1 = sprite.getV(12f / 16f);
        float u0 = sprite.getU(2f / 16f);
        float u1 = sprite.getU(12f / 16f);

        vertex.putBakedQuad(pose, new BakedQuad(
                new Vector3f(2f / 16f, y2, 2f / 16f),
                new Vector3f(2f / 16f, y2, 14f / 16f),
                new Vector3f(14f / 16f, y2, 14f / 16f),
                new Vector3f(14f / 16f, y2, 2f / 16f),
                UVPair.pack(u0, v0),
                UVPair.pack(u0, v1),
                UVPair.pack(u1, v1),
                UVPair.pack(u1, v0),
                Direction.UP,
                new BakedQuad.MaterialInfo(
                        sprite,
                        ChunkSectionLayer.TRANSLUCENT,
                        RenderTypes.LINES_TRANSLUCENT,
                        0,
                        false,
                        light
                )
        ), instance);
    }

    /* 南面 */
    private static void south(PoseStack.Pose pose, VertexConsumer vertex, float u0, float v0, float u1, float v1, TextureAtlasSprite sprite, QuadInstance instance, int light, float y2) {
        vertex.putBakedQuad(pose, new BakedQuad(
                new Vector3f(12f / 16f, 2f / 16f, 13.99f / 16f),
                new Vector3f(12f / 16f, y2, 13.99f / 16f),
                new Vector3f(4f / 16f, y2, 13.99f / 16f),
                new Vector3f(4f / 16f, 2f / 16f, 13.99f / 16f),
                UVPair.pack(u0, v0),
                UVPair.pack(u0, v1),
                UVPair.pack(u1, v1),
                UVPair.pack(u1, v0),
                Direction.NORTH,
                new BakedQuad.MaterialInfo(
                        sprite,
                        ChunkSectionLayer.TRANSLUCENT,
                        RenderTypes.LINES_TRANSLUCENT,
                        0,
                        false,
                        light
                )
        ), instance);
    }

    /* 东面 */
    private static void east(PoseStack.Pose pose, VertexConsumer vertex, float u0, float v0, float u1, float v1, TextureAtlasSprite sprite, QuadInstance instance, int light, float y2) {
        vertex.putBakedQuad(pose, new BakedQuad(
                new Vector3f(13.99f / 16f, 2f/ 16f, 4f / 16f),
                new Vector3f(13.99f / 16f, y2, 4f / 16f),
                new Vector3f(13.99f / 16f, y2, 12f / 16f),
                new Vector3f(13.99f / 16f, 2f / 16f, 12f / 16f),
                UVPair.pack(u0, v0),
                UVPair.pack(u0, v1),
                UVPair.pack(u1, v1),
                UVPair.pack(u1, v0),
                Direction.NORTH.getOpposite(),
                new BakedQuad.MaterialInfo(
                        sprite,
                        ChunkSectionLayer.TRANSLUCENT,
                        RenderTypes.LINES_TRANSLUCENT,
                        0,
                        false,
                        light
                )
        ), instance);
    }

    /* 北面 */
    private static void north(PoseStack.Pose pose, VertexConsumer vertex, float u0, float v0, float u1, float v1, TextureAtlasSprite sprite, QuadInstance instance, int light, float y2) {

        vertex.putBakedQuad(pose, new BakedQuad(
                new Vector3f(4f / 16f, 2f / 16f, 2.01f / 16f),
                new Vector3f(4f / 16f, y2, 2.01f / 16f),
                new Vector3f(12f / 16f, y2, 2.01f / 16f),
                new Vector3f(12f / 16f, 2f / 16f, 2.01f / 16f),
                UVPair.pack(u0, v0),
                UVPair.pack(u0, v1),
                UVPair.pack(u1, v1),
                UVPair.pack(u1, v0),
                Direction.NORTH.getOpposite(),
                new BakedQuad.MaterialInfo(
                        sprite,
                        ChunkSectionLayer.TRANSLUCENT,
                        RenderTypes.LINES_TRANSLUCENT,
                        0,
                        false,
                        light
                )
        ), instance);
    }

    /* 西面 */
    public static void west(PoseStack.Pose pose, VertexConsumer vertex, float u0, float v0, float u1, float v1, TextureAtlasSprite sprite, QuadInstance instance, int light, float y2) {
        vertex.putBakedQuad(pose, new BakedQuad(
                new Vector3f(2.01f / 16f, 2f / 16f, 12f / 16f),
                new Vector3f(2.01f / 16f, y2, 12f / 16f),
                new Vector3f(2.01f / 16f, y2, 4f / 16f),
                new Vector3f(2.01f / 16f, 2f / 16f, 4f / 16f),
                UVPair.pack(u0, v0),
                UVPair.pack(u0, v1),
                UVPair.pack(u1, v1),
                UVPair.pack(u1, v0),
                Direction.NORTH.getOpposite(),
                new BakedQuad.MaterialInfo(
                        sprite,
                        ChunkSectionLayer.TRANSLUCENT,
                        RenderTypes.LINES_TRANSLUCENT,
                        0,
                        false,
                        light
                )
        ), instance);
    }

    public static class TankRenderState extends BlockEntityRenderState {
        public TextureAtlasSprite sprite;
        public float fill;
        public int color;
    }
}
