package com.wf.firearms.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

/** Guns RPG 风格 HUD 绘制辅助（进度条、面板）。 */
public final class FirearmHudDraw {
    public static final int BAR_WIDTH = 75;
    private static final int BAR_HEIGHT = 7;
    private static final int BAR_GAP = 2;

    private FirearmHudDraw() {}

    public static void drawSolid(GuiGraphics g, int x0, int y0, int x1, int y1, int argb) {
        g.fill(x0, y0, x1, y1, argb);
    }

    public static void drawGradientH(GuiGraphics g, int x0, int y0, int x1, int y1, int leftArgb, int rightArgb) {
        g.fillGradient(x0, y0, x1, y1, leftArgb, rightArgb);
    }

    public static void drawKillProgressBar(
            GuiGraphics g,
            Font font,
            int x,
            int y,
            int level,
            float progress01,
            int mainColor,
            int secondaryColor) {
        String levelText = String.valueOf(level);
        int textW = font.width(levelText);
        g.drawString(font, levelText, x, y, mainColor, true);
        int barLeft = x + textW + 5;
        int barRight = barLeft + BAR_WIDTH;
        drawSolid(g, barLeft, y, barRight, y + BAR_HEIGHT, 0xFF000000);
        float f = Math.min(1f, Math.max(0f, progress01));
        int fillRight = barLeft + 2 + (int) ((BAR_WIDTH - 4) * f);
        if (fillRight > barLeft + 2) {
            drawGradientH(g, barLeft + 2, y + 2, fillRight, y + BAR_HEIGHT - 2, mainColor, secondaryColor);
        }
    }

    public static int barBlockHeight() {
        return BAR_HEIGHT + BAR_GAP;
    }

    /** 屏幕右下：弹药数量。 */
    public static void drawAmmoPanelBottomRight(
            GuiGraphics g, Font font, int width, int height, int ammo, int magSize, boolean lowAmmo) {
        String text = ammo + " / " + magSize;
        int padX = 8;
        int padY = 4;
        int textW = font.width(text);
        int panelW = textW + padX * 2;
        int panelH = 14 + padY * 2;
        int margin = 10;
        int x1 = width - margin;
        int x0 = x1 - panelW;
        int y1 = height - margin - 6;
        int y0 = y1 - panelH;
        int bg = 0xCC101018;
        int border = lowAmmo ? 0xFFAA3333 : 0xFF3A4A5A;
        drawSolid(g, x0 - 1, y0 - 1, x1 + 1, y1 + 1, border);
        drawSolid(g, x0, y0, x1, y1, bg);
        int color = lowAmmo ? 0xFFFF6666 : 0xFFFFFFFF;
        g.drawString(font, text, x0 + padX, y0 + padY, color, false);
    }

    /** 右下弹药面板正上方：子弹材质行。 */
    public static int drawMaterialLineBottomRight(
            GuiGraphics g, Font font, int width, int height, net.minecraft.network.chat.Component line) {
        int margin = 10;
        int ammoPanelTop = height - margin - 6 - (14 + 8);
        int y = ammoPanelTop - 12;
        int x = width - margin - font.width(line);
        g.drawString(font, line, x, y, 0xFFCCCCCC, true);
        return y;
    }

    public static void drawDifficultyBadge(GuiGraphics g, Font font, int x, int y, float difficulty) {
        String label = String.format(java.util.Locale.ROOT, "Difficulty %.1f", difficulty);
        int w = font.width(label) + 12;
        int h = 14;
        drawSolid(g, x, y, x + w, y + h, 0xCC6A3A7A);
        drawSolid(g, x + 1, y + 1, x + w - 1, y + h - 1, 0xDD4A2858);
        g.drawString(font, label, x + 6, y + 3, 0xFFFFFFFF, false);
    }
}
