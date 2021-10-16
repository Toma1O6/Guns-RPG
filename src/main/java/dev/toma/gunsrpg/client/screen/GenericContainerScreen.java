package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class GenericContainerScreen<C extends Container> extends ContainerScreen<C> {

    public GenericContainerScreen(C container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    public abstract ResourceLocation getBackgroundImage();

    @Override
    protected void renderBg(MatrixStack matrix, float partial, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(this.getBackgroundImage());
        blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }
}
