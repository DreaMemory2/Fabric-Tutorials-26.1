package com.crystal.renderer;

import com.crystal.block.entity.FluidTankBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

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
        // 纹理图的位置
        float y1 = 2f / 16f;
        float y2 = fill * l + y1;
        float[][] uv = getTextureUV(sprite, y1, y2);
        // 轻微偏移，防止图层叠加
        Vector3fc[][] vec = new FluidSquare(2.01f / 16f, y1, 2.01f / 16f, y2).getVec();

        matrices.pushPose();

        collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucentEmissive(sprite.atlasLocation()), (pose, vertexConsumer) -> {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    vertexConsumer.addVertex(pose, vec[i][j])
                            .setColor(color)
                            .setUv(uv[j][0], uv[j][1])
                            .setLight(light)
                            .setOverlay(overlay)
                            .setNormal(pose, FluidSquare.getDirectionVec()[i]);
                }
            }
        });

        // 绘制顶部（当液体未装满整个容器时）
        if (fill < 1) {
            float minU = sprite.getU(2f / 16f);
            float maxU = sprite.getU(14f / 16f);
            float minV = sprite.getV(2f / 16f);
            float maxV = sprite.getV(14f / 16f);

            collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucentEmissive(sprite.atlasLocation()), (pose, vertexConsumer) -> {
                vertexConsumer.addVertex(pose, 2f / 16f, y2, 2f / 16f)
                        .setColor(color)
                        .setUv(minU, maxV)
                        .setLight(light)
                        .setOverlay(overlay)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose, 2f / 16f, y2, 14f / 16f)
                        .setColor(color)
                        .setUv(minU, minV)
                        .setLight(light)
                        .setOverlay(overlay)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose, 14f / 16f, y2, 14f / 16f)
                        .setColor(color)
                        .setUv(maxU, minV)
                        .setLight(light)
                        .setOverlay(overlay)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose, 14f / 16f, y2, 2f / 16f)
                        .setColor(color)
                        .setUv(maxU, maxV)
                        .setLight(light)
                        .setOverlay(overlay)
                        .setNormal(pose, 0, 1, 0);
            });
        }

        matrices.popPose();
    }

    /**
     * <p>液体方块四个顶点坐标：</p>
     * <ul>
     *     <li>左下角(minU, minV)</li>
     *     <li>左上角(minU, maxV)</li>
     *     <li>右上角(maxU, maxV)</li>
     *     <li>右下角(maxU, minV)</li>
     * </ul>
     * @param sprite 液体的纹理图
     * @param y1 最低高度
     * @param y2 最大高度
     * @return 液体方块侧面的四个顶点坐标
     */
    private static float[][] getTextureUV(TextureAtlasSprite sprite, float y1, float y2) {
        // 纹理图的大小
        // u轴
        float minU = sprite.getU(2f / 16f);
        float maxU = sprite.getU(12f / 16f);
        // v轴
        float minV = sprite.getV(y1);
        float maxV = sprite.getV(y2);
        return new float[][] {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}};
    }

    public static class FluidSquare {
        private final float x1;
        private final float x2;
        private final float y1;
        private final float y2;
        private final float z1;
        private final float z2;

        public FluidSquare(float x, float y, float z, float fillY) {
            this.x1 = x;
            this.x2 = x + (11.98f / 16f);
            this.y1 = y;
            this.y2 = fillY;
            this.z1 = z;
            this.z2 = z + (11.98f / 16f);
        }

        public static Vector3fc[] getDirectionVec() {
            return new Vector3fc[] {
                    Direction.EAST.getUnitVec3f(),
                    Direction.SOUTH.getUnitVec3f(),
                    Direction.WEST.getUnitVec3f(),
                    Direction.NORTH.getUnitVec3f()
            };
        }

        public Vector3fc[][] getVec() {
            return new Vector3fc[][] {
                    // 东面
                    {
                        new Vector3f(x1, y1, z1), // 左下
                        new Vector3f(x1, y2, z1), // 左上
                        new Vector3f(x1, y2, z2), // 右上
                        new Vector3f(x1, y1, z2)  // 右下
                    },
                    // 南面
                    {
                        new Vector3f(x1, y1, z2), // 左下
                        new Vector3f(x1, y2, z2), // 左上
                        new Vector3f(x2, y2, z2), // 右上
                        new Vector3f(x2, y1, z2)  // 右下
                    },
                    // 西面
                    {
                        new Vector3f(x2, y1, z1), // 左下
                        new Vector3f(x2, y2, z1), // 左上
                        new Vector3f(x2, y2, z2), // 右上
                        new Vector3f(x2, y1, z2)  // 右下
                    },
                    // 北面
                    {
                        new Vector3f(x1, y1, z1), // 左下
                        new Vector3f(x1, y2, z1), // 左上
                        new Vector3f(x2, y2, z1), // 右上
                        new Vector3f(x2, y1, z1)  // 右下
                    }
            };
        }
    }

    // 其他方案
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

    private static float[][] getQuadVerticesByDirection(Direction direction) {
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

    public static class TankRenderState extends BlockEntityRenderState {
        public TextureAtlasSprite sprite;
        public float fill;
        public int color;
    }
}
