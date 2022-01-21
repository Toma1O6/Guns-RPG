package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_RequestSmithingCraftPacket;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import dev.toma.gunsrpg.resource.smithing.SmithingRecipe;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmithingTableScreen extends ContainerScreen<SmithingTableContainer> {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/smithing_table.png");
    private static final ITextComponent INFO_LABEL = new TranslationTextComponent("screen.smithing_table.condition").withStyle(TextFormatting.BOLD);
    private final SmithingTableTileEntity smithingTable;
    private List<IRecipeCondition> failedCheckList = Collections.emptyList();

    // widgets
    private Button buttonCraft;

    public SmithingTableScreen(SmithingTableContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        this.smithingTable = container.getTileEntity();
        imageHeight = 172;
    }

    @Override
    public void removed() {
        super.removed();
        smithingTable.detachCallback();
    }

    @Override
    public void init() {
        super.init();
        buttonCraft = addButton(new Button(leftPos + 7, topPos + 65, 54, 20, new StringTextComponent("Craft"), this::buttonCraft_Clicked));
        buttonCraft.active = false;

        smithingTable.detachCallback();
        smithingTable.attachCallback(this::onRecipeChanged);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partial, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        if (failedCheckList.isEmpty())
            return;
        CharacterManager splitter = font.getSplitter();
        LanguageMap map = LanguageMap.getInstance();
        List<IReorderingProcessor> list = new ArrayList<>();
        for (IRecipeCondition condition : failedCheckList) {
            ITextComponent info = new StringTextComponent("- " + condition.getDisplayInfo().getString());
            splitter.splitLines(info, imageWidth - 71, info.getStyle()).stream().map(map::getVisualOrder).forEach(list::add);
        }
        int xPos = 66;
        int yPos = 88 - list.size() * 11;
        font.draw(stack, INFO_LABEL, xPos, yPos - 11, 0x373737);
        int i = 0;
        for (IReorderingProcessor processor : list) {
            font.draw(stack, processor, xPos, yPos + 11 * i++, 0x373737);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }

    private void buttonCraft_Clicked(Button button) {
        boolean shiftKey = Screen.hasShiftDown();
        NetworkManager.sendServerPacket(new C2S_RequestSmithingCraftPacket(smithingTable.getBlockPos(), shiftKey));
    }

    private void onRecipeChanged(SmithingRecipe recipe) {
        boolean crafting = false;
        clearFailedCheckMap();
        if (recipe != null) {
            PlayerEntity player = minecraft.player;
            if (recipe.canCraft(player)) {
                crafting = true;
            } else {
                populateFailedCheckMap(recipe.getFailedChecks(player));
            }
        }
        buttonCraft.active = crafting;
    }

    private void clearFailedCheckMap() {
        failedCheckList.clear();
    }

    private void populateFailedCheckMap(List<IRecipeCondition> conditions) {
        failedCheckList = conditions;
    }
}
