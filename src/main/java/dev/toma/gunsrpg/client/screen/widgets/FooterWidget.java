package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.api.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class FooterWidget extends Widget {

    final FontRenderer renderer;
    final IPointProvider data;
    final IKillData killData;

    int progressColor1;
    int progressColor2;

    public FooterWidget(int x, int y, int width, int height, FontRenderer renderer, IPointProvider data) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.renderer = renderer;
        this.data = data;
        this.killData = data instanceof IKillData ? (IKillData) data : null;
    }

    public void setColorSchema(int primaryColor, int secondaryColor) {
        this.progressColor1 = primaryColor;
        this.progressColor2 = secondaryColor;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        Matrix4f pose = matrix.last().pose();
        // background
        RenderUtils.drawSolid(pose, x, y, x + width, y + height, 0x44 << 24);
        // points
        ITextComponent component = new StringTextComponent(data.getPoints() + " pts");
        float yCenter = y + (height - renderer.lineHeight) / 2.0F;
        renderer.drawShadow(matrix, component, x + width - renderer.width(component) - 10, yCenter, 0xFFFF00);
        // level
        if (killData == null) return;
        int left = 15;
        int top = y + 5;
        int right = left + (int) (width / 2.5);
        int bottom = y + height - 5;
        StringBuilder levelInfo = new StringBuilder();
        int level = killData.getLevel();
        levelInfo.append("Level: ").append(level);
        if (level < killData.getLevelLimit()) {
            int amount = killData.getKills();
            int required = killData.getRequiredKillCount();
            float progress = amount / (float) required;
            int pct = IValueFormatter.PERCENT.formatAttributeValue(progress);
            RenderUtils.drawSolid(pose, left, top, right, bottom, 0xFF << 24);
            int diff = right - left - 2;
            RenderUtils.drawGradient(pose, left + 2, top + 2, left + (int) (progress * diff), bottom - 2, progressColor1 | 0xFF << 24, progressColor2 | 0xFF << 24);
            levelInfo.append(" (").append(pct).append("%)").append(" - ").append(amount).append(" / ").append(required).append(" kills");
        } else {
            right = left;
            levelInfo.append(" - ").append(killData.getKills()).append(" kills");
        }
        renderer.drawShadow(matrix, levelInfo.toString(), right + 5, yCenter, 0xFFFFFF);
    }
}
