package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.AmmoBenchContainer;
import dev.toma.gunsrpg.common.tileentity.AmmoBenchTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_AmmoBenchEventPacket;
import dev.toma.gunsrpg.resource.ammobench.AmmoBenchRecipe;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AmmoBenchScreen extends ContainerScreen<AmmoBenchContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = GunsRPG.makeResource("textures/screen/ammo_bench.png");
    private static final ITextComponent CRAFT_LABEL = new TranslationTextComponent("screen.ammo_bench.label.craft");
    private static final ITextComponent NONE_LABEL = new TranslationTextComponent("screen.ammo_bench.label.none_selected");
    private static final String SELECTED_RECIPE_LABEL_KEY = "screen.ammo_bench.label.selected_recipe";

    private Button craftButton;
    private Button nextRecipeButton;
    private Button previousRecipeButton;

    public AmmoBenchScreen(AmmoBenchContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 175;
    }

    @Override
    protected void init() {
        super.init();
        craftButton = addButton(new Button(leftPos + 61, topPos + 69, 54, 20, CRAFT_LABEL, this::craftButtonClicked));
        craftButton.active = false;
        previousRecipeButton = addButton(new Button(leftPos + 38, topPos + 69, 20, 20, new StringTextComponent("<"), this::previousRecipeButtonClicked));
        previousRecipeButton.active = false;
        nextRecipeButton = addButton(new Button(leftPos + 118, topPos + 69, 20, 20, new StringTextComponent(">"), this::nextRecipeButtonClicked));
        nextRecipeButton.active = false;
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
        blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        AmmoBenchTileEntity tileEntity = menu.getTileEntity();
        if (tileEntity.isCrafting()) {
            float progress = tileEntity.getCraftingProgress();
            blit(matrix, leftPos + 75, topPos + 43, 176, 0, (int)(26 * progress) + 1, 12);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        font.draw(matrix, title, titleLabelX, titleLabelY, 0x404040);

        AmmoBenchRecipe recipe = menu.getTileEntity().getActiveRecipe();
        ITextComponent recipeLabel = recipe != null ? new TranslationTextComponent("recipe." + recipe.getId().toString().replaceAll("[:/]", ".")) : NONE_LABEL;
        ITextComponent selectedRecipeLabel = new TranslationTextComponent(SELECTED_RECIPE_LABEL_KEY, recipeLabel);
        font.draw(matrix, selectedRecipeLabel, titleLabelX, titleLabelY + 10, 0x404040);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    public void tick() {
        updateButtonStates();
        super.tick();
    }

    private void craftButtonClicked(Button button) {
        NetworkManager.sendServerPacket(C2S_AmmoBenchEventPacket.craftingEvent(getPositionForNetworking()));
    }

    private void nextRecipeButtonClicked(Button button) {
        NetworkManager.sendServerPacket(C2S_AmmoBenchEventPacket.nextRecipe(getPositionForNetworking()));
    }

    private void previousRecipeButtonClicked(Button button) {
        NetworkManager.sendServerPacket(C2S_AmmoBenchEventPacket.previousRecipe(getPositionForNetworking()));
    }

    private void updateButtonStates() {
        AmmoBenchTileEntity tileEntity = menu.getTileEntity();
        AmmoBenchRecipe recipe = tileEntity.getActiveRecipe();
        if (recipe == null) {
            craftButton.active = false;
            previousRecipeButton.active = false;
            nextRecipeButton.active = false;
        } else {
            craftButton.active = !tileEntity.isCrafting() && tileEntity.canCraftCurrentRecipe();
            previousRecipeButton.active = tileEntity.canSelectPreviousRecipe();
            nextRecipeButton.active = tileEntity.canSelectNextRecipe();
        }
    }

    private BlockPos getPositionForNetworking() {
        return menu.getTileEntity().getBlockPos();
    }
}
