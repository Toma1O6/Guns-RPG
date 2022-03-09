package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.RepairStationContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RepairStationScreen extends ContainerScreen<RepairStationContainer> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/screen/repair_station.png");
    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/repair_man_i.png");

    public RepairStationScreen(RepairStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 164;
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    private void repair(Button button) {

    }
}
