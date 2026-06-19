package com.crystal.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;

public class TankRenderState extends BlockEntityRenderState {
    public @Nullable TextureAtlasSprite sprite;
    public float fill;
    public int color;
}
