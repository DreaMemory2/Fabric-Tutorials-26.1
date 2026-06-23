package com.crystal.screen;

import com.crystal.CrystalMod;
import com.crystal.block.entity.FluidTankBlockEntity;
import com.crystal.screenhandler.FluidTankScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FluidTankScreen extends AbstractContainerScreen<@NotNull FluidTankScreenHandler> {
    public static final Identifier TEXTURE = CrystalMod.of("textures/gui/fluid_tank.png");
    public final Inventory inventory;

    public FluidTankScreen(FluidTankScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        super.init();
        // 设置标题在容器页面的中心位置上
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;

        FluidTankBlockEntity blockEntity = this.menu.getBlockEntity();
        this.addRenderableWidget(FluidWidget.builder(blockEntity.getFluidTank()).position(this.leftPos + 49, this.topPos + 19).size(65, 47).posSupplier(blockEntity::getBlockPos).build());
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
