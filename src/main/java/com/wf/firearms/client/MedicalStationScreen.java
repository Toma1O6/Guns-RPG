package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.medical.MedicalStationMenu;
import com.wf.firearms.network.MedicalStationCraftPacket;
import com.wf.firearms.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MedicalStationScreen extends AbstractContainerScreen<MedicalStationMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/gui/skilled_workbench.png");

    public MedicalStationScreen(MedicalStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, Component.translatable("container.gunsrpg.medical_station"));
        imageWidth = 176;
        imageHeight = 172;
        inventoryLabelY = imageHeight + 100;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(
                Button.builder(Component.translatable("screen.button.craft"), b -> {
                            if (menu.getBlockEntity() != null) {
                                ModNetwork.sendToServer(
                                        new MedicalStationCraftPacket(menu.getBlockEntity().getBlockPos()));
                            }
                        })
                        .bounds(leftPos + 124, topPos + 44, 40, 16)
                        .build());
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
        gfx.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        super.render(gfx, mouseX, mouseY, partialTick);
        renderTooltip(gfx, mouseX, mouseY);
    }
}
