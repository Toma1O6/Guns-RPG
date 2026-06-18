package com.wf.firearms.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wf.firearms.client.debuff.ClientDebuffState;
import com.wf.firearms.debuff.DebuffType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

/** 原版 Guns RPG debuff 面板：左下角图标 + 进度百分比 + 滑入/红绿闪烁 + 止血抗性条。 */
public final class DebuffHudOverlay {
    public static final IGuiOverlay OVERLAY = DebuffHudOverlay::render;

    private static final int PANEL_W = 65;
    private static final int PANEL_H = 20;
    private static final int BOTTOM_OFFSET = 50;

    private DebuffHudOverlay() {}

    private static void render(ForgeGui gui, GuiGraphics gfx, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        List<RenderRow> rows = collectRows();
        if (rows.isEmpty()) {
            return;
        }

        int left = 0;
        int top = screenHeight - BOTTOM_OFFSET - rows.size() * PANEL_H;
        RenderSystem.enableBlend();
        Font font = mc.font;
        for (int i = 0; i < rows.size(); i++) {
            drawRow(gfx, font, rows.get(i), left, top + i * PANEL_H, partialTick);
        }
    }

    private static List<RenderRow> collectRows() {
        List<RenderRow> rows = new ArrayList<>();
        ClientDebuffState.FractureEntry fracture = ClientDebuffState.fracture();
        if (fracture.active) {
            rows.add(new RenderRow(
                    DebuffType.FRACTURE,
                    fracture,
                    fracture.progress,
                    false,
                    0f,
                    fracture.progress + " %"));
        }
        for (DebuffType type : DebuffType.values()) {
            if (type == DebuffType.FRACTURE) {
                continue;
            }
            ClientDebuffState.Entry e = ClientDebuffState.entry(type);
            if (e != null && e.active) {
                String text = e.resist
                        ? Component.translatable("debuff.text.resist").getString()
                        : e.progress + " %";
                rows.add(new RenderRow(type, e, e.progress, e.resist, e.resistProgress, text));
            }
        }
        return rows;
    }

    private static void drawRow(
            GuiGraphics gfx, Font font, RenderRow row, int left, int top, float partialTick) {
        ClientDebuffState.AnimCounters anim = row.anim;
        int addProgress = Math.min(anim.ticksSinceAdded, ClientDebuffState.ADD_EFFECT_TIME);
        float slideIn = linear(addProgress, 0, ClientDebuffState.SLIDE_IN_TIME, partialTick);
        int bgAlpha = 0x88000000;
        int bgRight = left + (int) (PANEL_W * slideIn);
        if (bgRight > left) {
            gfx.fill(left, top, bgRight, top + PANEL_H, bgAlpha);
        }

        float fadeIn = linear(addProgress, ClientDebuffState.SLIDE_IN_TIME,
                ClientDebuffState.ADD_EFFECT_TIME, partialTick);
        int textAlpha = Math.max(4, (int) (fadeIn * 255));
        int colorCap = (int) (fadeIn * 255);
        int iconAlpha = (int) (fadeIn * 255);

        RenderSystem.setShaderColor(1f, 1f, 1f, iconAlpha / 255f);
        gfx.blit(row.type.icon(), left + 2, top + 2, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (row.resist) {
            int barLeft = left;
            int barRight = left + (int) (PANEL_W * row.resistProgress);
            gfx.fill(barLeft, top + PANEL_H - 1, barRight, top + PANEL_H, 0xFF00CC00);
        }

        int textColor = textColor(anim, textAlpha, colorCap, row.resist);
        int textWidth = font.width(row.text);
        gfx.drawString(font, row.text, left + PANEL_W - 5 - textWidth, top + 6, textColor, false);
    }

    private static int textColor(
            ClientDebuffState.AnimCounters anim, int textAlpha, int colorCap, boolean resist) {
        if (resist) {
            return argb(textAlpha, 0, 255, 255);
        }
        int heal = anim.ticksSinceHealed;
        int damage = anim.ticksSinceProgressed;
        boolean healing = heal < damage && heal < 15;
        float effect;
        if (healing) {
            effect = linear(heal, 0, ClientDebuffState.EFFECT_LENGTH, 0);
            int g = Math.min((int) (effect * 255), colorCap);
            return argb(textAlpha, g, 255, g);
        }
        effect = linear(damage, 0, ClientDebuffState.EFFECT_LENGTH, 0);
        int r = Math.min((int) (effect * 255), colorCap);
        return argb(textAlpha, 255, r, r);
    }

    private static int argb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** 原版 Mth.asLinearInterpolatedFunction / asLinearFunction 简化版。 */
    private static float linear(int progress, int start, int end, float partial) {
        if (end <= start) {
            return progress >= start ? 1f : 0f;
        }
        float t = (progress + partial - start) / (end - start);
        return Math.max(0f, Math.min(1f, t));
    }

    private record RenderRow(
            DebuffType type,
            ClientDebuffState.AnimCounters anim,
            int progress,
            boolean resist,
            float resistProgress,
            String text) {}
}
