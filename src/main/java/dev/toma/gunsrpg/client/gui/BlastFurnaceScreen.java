package dev.toma.gunsrpg.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.container.BlastFurnaceContainer;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BlastFurnaceScreen extends ContainerScreen<BlastFurnaceContainer> {

    private static final ResourceLocation ICON = new ResourceLocation("textures/gui/container/furnace.png");

    public BlastFurnaceScreen(BlastFurnaceContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float deltaRenderTime, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(ICON);
        blit(matrix, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
        BlastFurnaceTileEntity tile = menu.getTileEntity();
        if (tile.timeCooking > 0) {
            int pixels = (int) (13 * (tile.fuelValue / (float) BlastFurnaceTileEntity.FUEL_VALUE_LIMIT));
            float modifier = pixels / 13.0F;
            int i = (int) (13 * modifier);
            blit(matrix, getGuiLeft() + 56, getGuiTop() + 48 - i, 176, 12 - i, 14, i + 1);
        }
        int pixels = (int) (24 * (tile.timeCooking / (float) BlastFurnaceTileEntity.SMELT_TIME));
        float modifier = pixels / 24.0F;
        blit(matrix, getGuiLeft() + 79, getGuiTop() + 34, 176, 14, (int) (24 * modifier), 16);
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        font.draw(matrix, title, getXSize() / 2f - font.width(title) / 2f, 6, 0x404040);
        font.draw(matrix, inventory.getName(), 8, getYSize() - 94, 0x404040);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }
}
