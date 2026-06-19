package com.crystal.renderer;

import com.crystal.CrystalMod;
import com.crystal.CrystalModClient;
import com.crystal.block.entity.FluidTankBlockEntity;
import com.crystal.renderer.state.TankRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class FluidTankBlockRenderer implements BlockEntityRenderer<@NotNull FluidTankBlockEntity, @NotNull TankRenderState> {
    public final BlockEntityRendererProvider.Context context;

    public FluidTankBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public TankRenderState createRenderState() {
        return new TankRenderState();
    }

    @Override
    public void extractRenderState(FluidTankBlockEntity entity, @NotNull TankRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
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
        state.color = FluidVariantRendering.getColor(fluidVariant, BlockAndTintGetter.EMPTY, entity.getBlockPos());
        // 获取静态精灵图（静态纹理图）
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidVariant.getFluid().defaultFluidState());
        state.sprite = fluidModel.stillMaterial().sprite();
    }

    @Override
    public void submit(TankRenderState state, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        throw new RuntimeException();
        // TextureAtlasSprite sprite = state.sprite;
        /*if (sprite == null) return;*/

        // submitTankFluids(collector, matrices, sprite, state.color, state.fill, state.lightCoords, OverlayTexture.NO_OVERLAY);
    }

    private void submitTankFluid(@NotNull SubmitNodeCollector collector, @NotNull PoseStack matrices, TextureAtlasSprite sprite, int color, float fill, int light, int overlay) {
        // 纹理图的位置
        float y1 = 1;
        float y2 = fill + y1;

        // 纹理图的大小
        // 像素宽度和长度
        float minU = sprite.getU(1);
        float maxU = sprite.getU(2);
        // 像素高度
        float minV = sprite.getV(y1);
        float maxV = sprite.getV(y2);

        matrices.pushPose();

        collector.submitCustomGeometry(matrices, Sheets.translucentBlockSheet(), (pose, vertexConsumer) -> {
            vertexConsumer.addVertex(pose, 1, y1, 1)
                    .setColor(color)
                    .setUv(minU, minV)
                    .setLight(light)
                    .setOverlay(overlay)
                    .setNormal(0, 1, 0); // 左下角
            vertexConsumer.addVertex(pose, 1, y2, 1)
                    .setColor(color)
                    .setUv(minU, maxV)
                    .setLight(light)
                    .setOverlay(overlay)
                    .setNormal(0, 1, 0); // 左上角
            vertexConsumer.addVertex(pose, 2, y2, 1)
                    .setColor(color)
                    .setUv(maxU, maxV)
                    .setLight(light)
                    .setOverlay(overlay)
                    .setNormal(0, 1, 0); // 右上角

            vertexConsumer.addVertex(pose, 2, y1, 1)
                    .setColor(color)
                    .setUv(maxU, minV)
                    .setLight(light)
                    .setOverlay(overlay)
                    .setNormal(0, 1, 0); // 右下角
        });

        matrices.popPose();
    }

    /**
     * Draws the tank's contained fluid as a translucent box (full-tank shape, scaled by fill) through the
     * NeoForge 26.1 submit pipeline. Shared by the tank block-entity renderer and the tank item renderer.
     */
    public static void submitTankFluids(SubmitNodeCollector collector, PoseStack poseStack, TextureAtlasSprite sprite, int color, float fill, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.126, 0.126, 0.126);
        poseStack.scale(0.745f, 0.745f * fill, 0.745f);

        // snapshot the transformed pose and defer the actual vertex emission to the submit pipeline
        collector.submitCustomGeometry(poseStack, Sheets.translucentBlockSheet(), (pose, consumer) -> {
            for (var direction : Direction.values()) {
                if (direction.equals(Direction.DOWN)) continue; // skip bottom, as it's never visible
                drawQuad(direction, consumer, pose.pose(), pose, sprite, color, light, overlay);
            }
        });

        poseStack.popPose();
    }

    public static void drawQuad(Direction direction, VertexConsumer consumer, Matrix4f modelMatrix, PoseStack.Pose normalMatrix, TextureAtlasSprite sprite, int color, int light, int overlay) {
        // Define the vertices of the quad based on the direction it's facing

        var normal = direction.step();

        var positions = getQuadVerticesByDirection(direction);

        for (int i = positions.length - 1; i >= 0; i--) {

            var pos = positions[i];
            var u = sprite.getU(getFrameU()[i]);
            var v = sprite.getV(getFrameV()[i]);

            consumer.addVertex(modelMatrix, pos[0], pos[1], pos[2])
                    .setColor(color)
                    .setUv(u, v)
                    .setLight(light)
                    .setOverlay(overlay)
                    .setNormal(normalMatrix, normal.x, normal.y, normal.z);
        }

    }

    private static float[] getFrameU() {
        return new float[]{0, 1, 1, 0};
    }

    private static float[] getFrameV() {
        return new float[]{0, 0, 1, 1};
    }

    /**
     *
     * @param direction 液体方块中每一面纹理的方向（上、下、左、右，前、后）
     * @return 返回其中一个方向的顶点位置
     */
    private static float[] @NotNull [] getQuadVerticesByDirection(Direction direction) {
        // Define the vertices for each face of the cube
        return switch (direction) {
            case UP -> new float[][]{
                    {0, 1, 0}, // Top-left
                    {1, 1, 0}, // Top-right
                    {1, 1, 1}, // Bottom-right
                    {0, 1, 1}  // Bottom-left
            };
            case DOWN -> new float[][]{
                    {0, 0, 1}, // Top-left
                    {1, 0, 1}, // Top-right
                    {1, 0, 0}, // Bottom-right
                    {0, 0, 0}  // Bottom-left
            };
            case NORTH -> new float[][]{
                    {1, 1, 0}, // Top-left
                    {0, 1, 0}, // Top-right
                    {0, 0, 0}, // Bottom-right
                    {1, 0, 0}  // Bottom-left
            };
            case SOUTH -> new float[][]{
                    {0, 1, 1}, // Top-left
                    {1, 1, 1}, // Top-right
                    {1, 0, 1}, // Bottom-right
                    {0, 0, 1}  // Bottom-left
            };
            case WEST -> new float[][]{
                    {0, 1, 0}, // Top-left
                    {0, 1, 1}, // Top-right
                    {0, 0, 1}, // Bottom-right
                    {0, 0, 0}  // Bottom-left
            };
            case EAST -> new float[][]{
                    {1, 1, 1}, // Top-left
                    {1, 1, 0}, // Top-right
                    {1, 0, 0}, // Bottom-right
                    {1, 0, 1}  // Bottom-left
            };
        };
    }

    /**
     * A single resolved fluid box to draw, expressed in the model space used by the host renderer.
     * Used by the refinery renderers to ship their fluid geometry through the GeckoLib DataTicket mechanism.
     *
     * @param min      the lower corner of the box (in 1/16th model units, matching the original constants)
     * @param size     the box dimensions
     * @param fill     0..1 fill level (scales the height)
     * @param sprite   the still fluid sprite
     * @param color    the ARGB tint
     * @param rotation an optional rotation applied around the model origin before translating to {@code min}
     */
    public record FluidCube(Vector3f min, Vector3f size, float fill, TextureAtlasSprite sprite, int color,
                            @Nullable Quaternionf rotation) {
    }

    /**
     * Emits a list of {@link FluidCube}s into the NeoForge 26.1 submit pipeline. Each cube is submitted as its own
     * custom-geometry node (the collector snapshots the pose at submit time), so per-cube transforms are independent.
     */
    public static void submitFluidCubes(SubmitNodeCollector collector, PoseStack poseStack, List<FluidCube> cubes, int light, int overlay) {
        for (var cube : cubes) {
            poseStack.pushPose();

            if (cube.rotation() != null) poseStack.mulPose(cube.rotation());

            poseStack.translate(cube.min().x + 0.01f, cube.min().y + 0.01f, cube.min().z + 0.01f);
            poseStack.scale(cube.size().x - 0.02f, cube.size().y * cube.fill() - 0.03f, cube.size().z - 0.02f);

            var sprite = cube.sprite();
            var color = cube.color();

            collector.submitCustomGeometry(poseStack, Sheets.translucentBlockSheet(), (pose, consumer) -> {
                for (Direction direction : Direction.values()) {
                    if (direction.equals(Direction.DOWN)) continue; // skip bottom, as it's never visible
                    drawQuad(direction, consumer, pose.pose(), pose, sprite, color, light, overlay);
                }
            });

            poseStack.popPose();
        }
    }
}
