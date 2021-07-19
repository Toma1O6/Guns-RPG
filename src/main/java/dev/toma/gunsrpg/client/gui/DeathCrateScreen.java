package dev.toma.gunsrpg.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.DeathCrateContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DeathCrateScreen extends ContainerScreen<DeathCrateContainer> {

    static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/death_crate.png");

    public DeathCrateScreen(DeathCrateContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        imageHeight = 208;
        titleLabelY = 9;
        inventoryLabelY = 115;
    }

    @Override
    protected void renderBg(MatrixStack matrix, float deltaRenderTime, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }
}
