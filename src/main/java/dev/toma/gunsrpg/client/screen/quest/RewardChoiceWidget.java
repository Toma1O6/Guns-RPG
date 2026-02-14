package dev.toma.gunsrpg.client.screen.quest;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.screen.animation.FadeAnimation;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.util.RenderUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class RewardChoiceWidget extends AbstractButton {

    private final QuestReward.Choice choice;
    private final SelectionListener listener;
    private FadeAnimation fadeOutAnim = FadeAnimation.NO_FADE;
    private boolean selected;
    private TooltipRenderer tooltipRenderer;

    public RewardChoiceWidget(int x, int y, int width, int height, QuestReward.Choice choice, SelectionListener selectionListener) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.choice = choice;
        this.listener = selectionListener;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setFadeOutAnim(FadeAnimation fadeOutAnim) {
        this.fadeOutAnim = fadeOutAnim;
    }

    public void setTooltipRenderer(TooltipRenderer tooltipRenderer) {
        this.tooltipRenderer = tooltipRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float renderDelta) {
        boolean hovered = this.isHovered || this.selected;
        if (hovered)
            this.fadeOutAnim.reset();
        float progress = this.fadeOutAnim.getInvertedProgress();
        int bgColor = this.fadeOutAnim.getAdjustedAlphaColor(0x44FFFFFF, progress);
        if (RenderUtils.isVisible(bgColor)) {
            fill(matrix, this.x, this.y, this.x + this.width, this.y + this.height, bgColor);
        }
        QuestReward.Choice.DisplayData data = this.choice.getDisplayInfo();
        Minecraft instance = Minecraft.getInstance();
        FontRenderer font = instance.font;
        ItemRenderer renderer = instance.getItemRenderer();
        ItemStack itemStack = data.getItemStack();
        int count = data.getCount();
        int textColor = hovered ? 0xFFFF00 : 0xFFFFFF;
        renderer.renderGuiItem(itemStack, this.x + 2, this.y + 2);
        font.drawShadow(matrix, count + "x", this.x + 20, 1 + this.y + (this.height - font.lineHeight) / 2.0F, textColor);
        if (this.tooltipRenderer != null && mouseX >= this.x && mouseX <= this.x + 20 && mouseY >= this.y && mouseY <= this.y + this.height) {
            this.tooltipRenderer.render(matrix, itemStack, mouseX, mouseY);
        }
    }

    @Override
    public void onPress() {
        this.listener.onSelectionChanged(!this.selected, state -> this.selected = state);
    }

    @FunctionalInterface
    public interface SelectionListener {
        void onSelectionChanged(boolean selected, BooleanConsumer stateManager);
    }

    @FunctionalInterface
    public interface TooltipRenderer {
        void render(MatrixStack matrix, ItemStack itemStack, int mouseX, int mouseY);
    }
}
