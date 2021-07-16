package dev.toma.gunsrpg.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.AirdropContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiAirdrop extends ContainerScreen<AirdropContainer> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/airdrop.png");

    public GuiAirdrop(AirdropContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        font.draw(matrix, title, (getXSize() / 2f - font.width(title) / 2f) + 3, 8, 0x404040);
        font.draw(matrix, inventory.getName(), 115, getYSize() - 127, 0x404040);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(matrix, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float deltaRenderTime) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, deltaRenderTime);
        renderTooltip(matrixStack, mouseX, mouseY);
    }
}
