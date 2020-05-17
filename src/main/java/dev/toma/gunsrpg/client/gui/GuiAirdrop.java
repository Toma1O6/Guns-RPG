package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.ContainerAirdrop;
import dev.toma.gunsrpg.common.tileentity.TileEntityAirdrop;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiAirdrop extends GuiContainer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/airdrop.png");
    private final InventoryPlayer playerInv;
    private final TileEntityAirdrop tileEntityAirdrop;

    public GuiAirdrop(InventoryPlayer player, TileEntityAirdrop tileEntityAirdrop) {
        super(new ContainerAirdrop(player, tileEntityAirdrop));
        this.playerInv = player;
        this.tileEntityAirdrop = tileEntityAirdrop;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String tileName = new TextComponentTranslation(tileEntityAirdrop.getName()).getFormattedText();
        this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) + 3, 8, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 115, this.ySize - 127, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
