package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.container.BlastFurnaceContainer;
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
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(ICON);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (menu.isLit()) {
            int progress = menu.getLitProgress();
            blit(stack, leftPos + 56, topPos + 48 - progress, 176, 12 - progress, 14, progress + 1);
        }
        int burningProgress = menu.getBurnProgress();
        blit(stack, leftPos + 79, topPos + 34, 176, 14, burningProgress + 1, 16);
    }
}
