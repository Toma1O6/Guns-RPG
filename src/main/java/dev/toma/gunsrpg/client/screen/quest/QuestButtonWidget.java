package dev.toma.gunsrpg.client.screen.quest;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.screen.animation.FadeAnimation;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public final class QuestButtonWidget extends AbstractButton {

    public static final ResourceLocation SHEET = GunsRPG.makeResource("textures/screen/badges.png");

    private final FontRenderer font;
    private final Quest<?> quest;
    private final ClickHandler clickHandler;
    private boolean highlighted;
    private FadeAnimation fadeOutAnim = FadeAnimation.NO_FADE;

    public QuestButtonWidget(int x, int y, int width, int height, FontRenderer font, Quest<?> quest, ClickHandler clickHandler) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.font = font;
        this.quest = quest;
        this.clickHandler = clickHandler;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public void setFadeOutAnim(FadeAnimation fadeOutAnim) {
        this.fadeOutAnim = fadeOutAnim;
    }

    @Override
    public void onPress() {
        this.clickHandler.onClick();
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        if ((this.isHovered || this.highlighted) && this.active) {
            this.fadeOutAnim.reset();
        }
        float animProgress = this.fadeOutAnim.getInvertedProgress();
        int fadeColor = this.fadeOutAnim.getAdjustedAlphaColor(0x44FFFFFF, animProgress);
        if (RenderUtils.isVisible(fadeColor)) {
            fill(matrix, this.x, this.y, this.x + this.width, this.y + this.height, fadeColor);
        }
        // tier badge + quest name
        int texSize = this.height - 4;
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        manager.bind(SHEET);
        Matrix4f pose = matrix.last().pose();
        float texOffset = 1.0F / 8; // 8 tiers for a single texture
        int tierIndex = this.quest.getRewardTier() - 1;
        float v1 = texOffset * tierIndex;
        float v2 = v1 + texOffset;
        RenderUtils.drawTex(pose, this.x + 2, this.y + 2, this.x + 2 + texSize, this.y + 2 + texSize, 0.0F, v1, 1.0F, v2);
        ITextComponent name = this.quest.getScheme().getDisplayInfo().getName();
        int textColor = this.active ? tierIndex >= 7 ? 0xFFC305 : 0xFFFFFF : 0x888888;
        this.font.drawShadow(matrix, name, this.x + texSize + 8, 1 + this.y + (this.height - this.font.lineHeight) / 2.0F, textColor);
    }

    @FunctionalInterface
    public interface ClickHandler {
        void onClick();
    }
}
