package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.ContainerDeathCrate;
import dev.toma.gunsrpg.common.tileentity.TileEntityDeathCrate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDeathCrate extends GuiContainer {

    static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/death_crate.png");

    public GuiDeathCrate(InventoryPlayer player, TileEntityDeathCrate deathCrate) {
        super(new ContainerDeathCrate(player, deathCrate));
        ySize = 208;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
