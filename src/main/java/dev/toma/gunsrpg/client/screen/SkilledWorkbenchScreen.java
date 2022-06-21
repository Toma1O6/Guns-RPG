package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.SkilledWorkbenchContainer;
import dev.toma.gunsrpg.common.tileentity.ISkilledCrafting;
import dev.toma.gunsrpg.common.tileentity.VanillaInventoryTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_RequestSkilledCraftPacket;
import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
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

public class SkilledWorkbenchScreen<T extends VanillaInventoryTileEntity & ISkilledCrafting, C extends SkilledWorkbenchContainer<T>> extends ContainerScreen<C> {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/screen/skilled_workbench.png");
    private static final ITextComponent TEXT_CRAFT = new TranslationTextComponent("screen.button.craft");
    private static final ITextComponent INFO_LABEL = new TranslationTextComponent("screen.skilled_workbench.condition").withStyle(TextFormatting.BOLD);
    private final T tile;
    private List<IRecipeCondition> failedChecks = Collections.emptyList();
    private Button craftButton;

    public SkilledWorkbenchScreen(C container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.tile = container.getTileEntity();
        this.imageHeight = 172;
    }

    @Override
    public void removed() {
        super.removed();
        tile.detachCallback();
    }

    @Override
    public void init() {
        super.init();
        craftButton = addButton(new Button(leftPos + 7, topPos + 65, 54, 20, TEXT_CRAFT, this::buttonCraft_Clicked));
        craftButton.active = false;

        tile.detachCallback();
        tile.attachCallback(this::onRecipeChanged);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partial, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        if (failedChecks.isEmpty())
            return;
        CharacterManager splitter = font.getSplitter();
        LanguageMap map = LanguageMap.getInstance();
        List<IReorderingProcessor> list = new ArrayList<>();
        for (IRecipeCondition condition : failedChecks) {
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
        NetworkManager.sendServerPacket(new C2S_RequestSkilledCraftPacket(tile.getBlockPos(), shiftKey));
    }

    private void onRecipeChanged(SkilledRecipe<?> recipe) {
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
        craftButton.active = crafting;
    }

    private void clearFailedCheckMap() {
        failedChecks.clear();
    }

    private void populateFailedCheckMap(List<IRecipeCondition> conditions) {
        failedChecks = conditions;
    }
}
