package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.common.container.ContainerBlastFurnace;
import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiBlastFurnace extends GuiContainer {

    private static final ResourceLocation ICON = new ResourceLocation("textures/gui/container/furnace.png");
    private final TileEntityBlastFurnace tile;
    private final InventoryPlayer player;

    public GuiBlastFurnace(TileEntityBlastFurnace tile, InventoryPlayer player) {
        super(new ContainerBlastFurnace(player, tile));
        this.tile = tile;
        this.player = player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ICON);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
        if (tile.timeCooking > 0) {
            int pixels = (int)(13 * (tile.fuelValue / (float) TileEntityBlastFurnace.FUEL_VALUE_LIMIT));
            float mod = pixels / 13.0F;
            int i = (int)(13 * mod);
            drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - i, 176, 12 - i, 14, i + 1);
        }
        int pixels = (int)(24 * (tile.timeCooking / (float) TileEntityBlastFurnace.SMELT_TIME));
        float m = pixels / 24.0F;
        drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, (int)(24 * m), 16);
    }

    private int getCookProgressScaled(int pixels) {
        return (tile.timeCooking / TileEntityBlastFurnace.SMELT_TIME) * pixels;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = new TextComponentTranslation(this.tile.getName()).getFormattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
}
