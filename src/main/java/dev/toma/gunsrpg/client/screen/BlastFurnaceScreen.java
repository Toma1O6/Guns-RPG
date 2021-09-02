package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.container.BlastFurnaceContainer;
import net.minecraft.client.gui.recipebook.AbstractRecipeBookGui;
import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BlastFurnaceScreen extends ContainerScreen<BlastFurnaceContainer> implements IRecipeShownListener {

    private static final ResourceLocation RECIPE_BUTTON = new ResourceLocation("textures/gui/recipe_button.png");
    private static final ResourceLocation ICON = new ResourceLocation("textures/gui/container/furnace.png");
    private final AbstractRecipeBookGui recipeBookComponent;
    private boolean widthTooNarrow;

    public BlastFurnaceScreen(BlastFurnaceContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        this.recipeBookComponent = new FurnaceRecipeGui();
    }

    @Override
    protected void init() {
        super.init();

        widthTooNarrow = width < 379;
        recipeBookComponent.init(width, height, minecraft, widthTooNarrow, menu);
        leftPos = recipeBookComponent.updateScreenPosition(widthTooNarrow, width, imageWidth);

        addButton(new ImageButton(leftPos + 20, height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON, this::recipeButton_clicked));
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void tick() {
        super.tick();
        recipeBookComponent.tick();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        if (recipeBookComponent.isVisible() && widthTooNarrow) {
            renderBg(stack, partialTicks, mouseX, mouseY);
            recipeBookComponent.render(stack, mouseX, mouseY, partialTicks);
        } else {
            recipeBookComponent.render(stack, mouseX, mouseY, partialTicks);
            super.render(stack, mouseX, mouseY, partialTicks);
            recipeBookComponent.renderGhostRecipe(stack, leftPos, topPos, true, partialTicks);
        }
        renderTooltip(stack, mouseX, mouseY);
        recipeBookComponent.renderTooltip(stack, leftPos, topPos, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            return widthTooNarrow && recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return !recipeBookComponent.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_) {
        return recipeBookComponent.charTyped(p_231042_1_, p_231042_2_) || super.charTyped(p_231042_1_, p_231042_2_);
    }

    @Override
    public void recipesUpdated() {
        recipeBookComponent.recipesUpdated();
    }

    @Override
    public AbstractRecipeBookGui getRecipeBookComponent() {
        return recipeBookComponent;
    }

    @Override
    public void removed() {
        recipeBookComponent.removed();
        super.removed();
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

    @Override
    protected void slotClicked(Slot slot, int startIndex, int endIndex, ClickType clickType) {
        super.slotClicked(slot, startIndex, endIndex, clickType);
        recipeBookComponent.slotClicked(slot);
    }

    @Override
    protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
        boolean flag = p_195361_1_ < (double)p_195361_5_ || p_195361_3_ < (double)p_195361_6_ || p_195361_1_ >= (double)(p_195361_5_ + this.imageWidth) || p_195361_3_ >= (double)(p_195361_6_ + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(p_195361_1_, p_195361_3_, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, p_195361_7_) && flag;
    }

    private void recipeButton_clicked(Button button) {
        recipeBookComponent.initVisuals(widthTooNarrow);
        recipeBookComponent.toggleVisibility();
        leftPos = recipeBookComponent.updateScreenPosition(widthTooNarrow, width, imageWidth);
        ((ImageButton) button).setPosition(leftPos + 20, height / 2 - 49);
    }
}
