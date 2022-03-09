package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.RepairStationContainer;
import dev.toma.gunsrpg.common.container.SimpleContainerChangeListener;
import dev.toma.gunsrpg.common.tileentity.RepairStationTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_RequestRepairPacket;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RepairStationScreen extends ContainerScreen<RepairStationContainer> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/screen/repair_station.png");
    private final IContainerListener listener;

    private Button repairButton;

    public RepairStationScreen(RepairStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 164;
        listener = new SimpleContainerChangeListener(this::slotChanged);
        menu.addSlotListener(listener);
    }

    @Override
    public void removed() {
        menu.removeSlotListener(listener);
        super.removed();
    }

    @Override
    protected void init() {
        super.init();
        ITextComponent inventoryText = inventory.getDisplayName();
        int labelWidth = font.width(inventoryText);
        inventoryLabelX = imageWidth - 8 - labelWidth;
        repairButton = addButton(new Button(leftPos + 7, topPos + 57, 72, 20, new StringTextComponent("Repair"), this::repair));
        slotChanged(0, ItemStack.EMPTY);
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

    private void slotChanged(int slot, ItemStack stack) {
        if (repairButton == null) return;
        RepairStationTileEntity tile = menu.getTileEntity();
        repairButton.active = tile.canRepair();
    }

    private void repair(Button button) {
        if (menu.getTileEntity().canRepair()) {
            NetworkManager.sendServerPacket(new C2S_RequestRepairPacket(menu.getTileEntity().getBlockPos()));
        }
    }
}
