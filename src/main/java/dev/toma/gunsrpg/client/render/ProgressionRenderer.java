package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.IProgressionDetailProvider;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.OverlayPlacement;
import dev.toma.gunsrpg.config.client.ConfigurableOverlay;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ProgressionRenderer {

    public static final int PROGRESS_BAR_WIDTH = 75;
    private static final int PROGRESS_BAR_SPACING_X = 5;
    private static final int PROGRESS_BAR_HEIGHT = 7;
    private static final int PROGRESS_BAR_SPACING_Y = 2;

    private static final ProgressionRenderer INSTANCE = new ProgressionRenderer();
    private final List<IProgressionDetailProvider> renderList = new ArrayList<>();

    public static ProgressionRenderer getBuffer() {
        return INSTANCE;
    }

    public static int getProgressBarWidth(FontRenderer font, IKillData data) {
        return font.width(String.valueOf(data.getLevel())) + PROGRESS_BAR_SPACING_X + PROGRESS_BAR_WIDTH;
    }

    public static int getProgressBarHeight() {
        return PROGRESS_BAR_HEIGHT + PROGRESS_BAR_SPACING_Y;
    }

    public static void renderProgressionBar(MatrixStack matrix, IKillData data, FontRenderer font, int x, int y, int width, int height, OverlayPlacement.VerticalAlignment alignment, int mainColor, int secondaryColor) {
        Matrix4f pose = matrix.last().pose();
        IVec2i position = OverlayPlacement.getPlacement(OverlayPlacement.HorizontalAlignment.LEFT, alignment, x, y, width, height, width, PROGRESS_BAR_HEIGHT, 0, 0);
        float f = data.getLevel() >= data.getLevelLimit() ? 1.0F : data.getKills() / (float) data.getRequiredKillCount();
        // Left column
        String text = String.valueOf(data.getLevel());
        int textWidth = font.width(text);
        int textLeft = x + width - PROGRESS_BAR_WIDTH - PROGRESS_BAR_SPACING_X - textWidth;
        font.draw(matrix, text, textLeft, position.y(), mainColor);
        // Right column
        int barLeft = textLeft + PROGRESS_BAR_SPACING_X + textWidth;
        RenderUtils.drawSolid(pose, barLeft, position.y(), barLeft + PROGRESS_BAR_WIDTH, position.y() + PROGRESS_BAR_HEIGHT, 0xFF << 24); // background
        RenderUtils.drawGradient(pose, barLeft + 2, position.y() + 2, barLeft + MathHelper.ceil((PROGRESS_BAR_WIDTH - 2) * f), position.y() + PROGRESS_BAR_HEIGHT - 2, mainColor, secondaryColor);
    }

    public void add(IProgressionDetailProvider provider) {
        renderList.add(provider);
    }

    public void draw(MatrixStack matrix, FontRenderer font, MainWindow window, PlayerEntity player, IPlayerData data) {
        if (this.renderList.isEmpty())
            return;
        ConfigurableOverlay overlay = ClientSideManager.config.levelProgressOverlay;
        int panelWidth = this.getWidth(font, player, data);
        int panelHeight = this.getHeight(font, player, data);
        IVec2i pos = OverlayPlacement.getPlacement(overlay, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), panelWidth, panelHeight);
        int verticalOffset = 0;
        for (IProgressionDetailProvider provider : this.renderList) {
            int height = provider.getHeight(font, player, data);
            provider.draw(matrix, font, pos, 0, verticalOffset, panelWidth, height, player, data);
            verticalOffset += height;
        }
        this.renderList.clear();
    }

    private int getWidth(FontRenderer font, PlayerEntity player, IPlayerData data) {
        return this.renderList.stream().mapToInt(detail -> detail.getWidth(font, player, data)).max().orElse(0);
    }

    private int getHeight(FontRenderer font, PlayerEntity player, IPlayerData data) {
        return this.renderList.stream().mapToInt(detail -> detail.getHeight(font, player, data)).sum();
    }
}
