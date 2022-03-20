package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.CrystalStationContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CrystalStationScreen extends ContainerScreen<CrystalStationContainer> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/screen/crystal_station.png");

    public CrystalStationScreen(CrystalStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 156;
        inventoryLabelY = 63;
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }
}
