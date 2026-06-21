package com.crystal.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ScreenUtils {

    public static void renderTiledSprite(GuiGraphicsExtractor context, TextureAtlasSprite sprite, int x, int y, int width, int height, int color) {
        int spriteWidth = sprite.contents().width();
        int spriteHeight = sprite.contents().height();

        // 在xOy平面上平铺多少精灵图（纹理图）
        int xCount = Mth.floor((float) width / spriteWidth);
        int yCount = Mth.floor((float) height / spriteHeight);
        // 如果宽度无法整除（即数量为0），则需要求余数
        int xRemainder = width % spriteWidth;
        int yRemainder = height % spriteHeight;

        // 通过uv坐标确定为纹理的位置
        Identifier atlasId = sprite.atlasLocation();
        float minU = sprite.getU0();
        float minV = sprite.getV0();

        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                int x1 = x + i * xRemainder;
                int y1 = y + j * yRemainder;
                int x2 = x1 + spriteWidth;
                int y2 = y1 + spriteHeight;

                float maxU = sprite.getU1();
                float maxV = sprite.getV1();
                context.innerBlit(RenderPipelines.GUI_TEXTURED, atlasId, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }

            // 绘制剩余部分
            if (yRemainder > 0) {
                int x1 = x + (i * spriteWidth);
                int y1 = y + (yCount * spriteHeight);
                int x2 = x1 + spriteWidth;
                int y2 = y1 + yRemainder;

                float maxU = sprite.getU1();
                float maxV = minV + (sprite.getV1() - minV) * ((float) yRemainder / spriteHeight);
                context.innerBlit(RenderPipelines.GUI_TEXTURED, atlasId, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }
        }

        if (xRemainder > 0) {
            for (int j = 0; j < yCount; j++) {
                int x1 = x + (xCount * spriteWidth);
                int y1 = y + (j * spriteHeight);
                int x2 = x1 + xRemainder;
                int y2 = y1 + spriteHeight;

                float maxU = minU + (sprite.getU1() - minU) * ((float) xRemainder / spriteWidth);
                float maxV = sprite.getV1();
                context.innerBlit(RenderPipelines.GUI_TEXTURED, atlasId, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }

            if (yRemainder > 0) {
                int x1 = x + (xCount * spriteWidth);
                int y1 = y + (yCount * spriteHeight);
                int x2 = x1 + xRemainder;
                int y2 = y1 + yRemainder;

                float maxU = minU + (sprite.getU1() - minU) * ((float) xRemainder / spriteWidth);
                float maxV = minV + (sprite.getV1() - minV) * ((float) yRemainder / spriteHeight);
                context.innerBlit(RenderPipelines.GUI_TEXTURED, atlasId, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }
        }
    }
}
