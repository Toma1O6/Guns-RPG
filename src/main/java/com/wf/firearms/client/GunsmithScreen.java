package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.gunsmith.GunsmithMenu;
import com.wf.firearms.network.GunsmithCraftPacket;
import com.wf.firearms.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** 枪械台 GUI：贴图与槽位对齐 Guns RPG {@code SkilledWorkbenchScreen}。 */
public class GunsmithScreen extends AbstractContainerScreen<GunsmithMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/gui/skilled_workbench.png");

    private static final int OUTPUT_SLOT_X = 124;
    private static final int OUTPUT_SLOT_Y = 26;

    private Button craftButton;

    public GunsmithScreen(GunsmithMenu menu, Inventory inv, Component title) {
        super(menu, inv, Component.empty());
        imageWidth = 176;
        imageHeight = 172;
        inventoryLabelY = imageHeight + 100;
    }

    @Override
    protected void init() {
        super.init();
        craftButton = Button.builder(Component.translatable("screen.button.craft"), b -> onCraft())
                .bounds(leftPos + 7, topPos + 65, 54, 20)
                .build();
        addRenderableWidget(craftButton);
    }

    private void onCraft() {
        ModNetwork.sendToServer(new GunsmithCraftPacket(menu.getBlockEntity().getBlockPos()));
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        gfx.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        gfx.blit(INVENTORY_LOCATION, x + OUTPUT_SLOT_X, y + OUTPUT_SLOT_Y, 7, 83, 18, 18);
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        // 不绘制「枪械台」「物品栏」标题（原版 SkilledWorkbenchScreen 同样不画标题）
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        super.render(gfx, mouseX, mouseY, partialTick);
        renderTooltip(gfx, mouseX, mouseY);
    }
}
