package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketCheckSmithingRecipe;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SmithingTableScreen extends ContainerScreen<SmithingTableContainer> {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/smithing_table.png");
    private final SmithingTableTileEntity smithingTable;

    public SmithingTableScreen(SmithingTableContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        this.smithingTable = container.getTileEntity();
        imageWidth = 203;
        imageHeight = 172;
    }

    @Override
    public void init() {
        super.init();
        addButton(new Button(leftPos + 25, topPos + 65, 54, 20, new StringTextComponent("Craft"), this::buttonCraft_Clicked));
    }

    private void buttonCraft_Clicked(Button button) {
        boolean shiftKey = Screen.hasShiftDown();
        NetworkManager.sendServerPacket(new SPacketCheckSmithingRecipe(smithingTable.getBlockPos(), shiftKey, OptionalObject.empty()));
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(p_230450_1_, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }
}
