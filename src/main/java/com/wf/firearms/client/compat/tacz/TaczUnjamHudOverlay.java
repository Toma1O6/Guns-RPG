package com.wf.firearms.client.compat.tacz;

import com.wf.firearms.client.FirearmHudDraw;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczPerkBridge;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/** TaCZ 整合包枪排障：屏幕中心提示与进度条。 */
public final class TaczUnjamHudOverlay {
    public static final IGuiOverlay OVERLAY = TaczUnjamHudOverlay::render;

    private TaczUnjamHudOverlay() {}

    private static void render(ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) {
        if (!TaczBackendConfig.useTaczShooting()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }
        ItemStack gun = mc.player.getMainHandItem();
        if (!TaczBridge.isTaczGun(gun)) {
            return;
        }

        int cx = width / 2;
        int cy = height / 2;

        if (TaczUnjamClientState.isActive()) {
            Component text = Component.translatable("gunsrpg.gun.unjamming");
            int tw = mc.font.width(text);
            g.drawString(mc.font, text, cx - tw / 2, cy + 18, 0xFFFFCC66, true);

            int barW = 72;
            int x0 = cx - barW / 2;
            int y0 = cy + 30;
            FirearmHudDraw.drawSolid(g, x0 - 1, y0 - 1, x0 + barW + 1, y0 + 7, 0xAA000000);
            float p = TaczUnjamClientState.progress();
            FirearmHudDraw.drawSolid(g, x0, y0, x0 + (int) (barW * p), y0 + 5, 0xFFFFAA55);
            return;
        }

        if (TaczPerkBridge.isJammed(gun)) {
            Component jam = Component.translatable("gunsrpg.gun.jammed");
            g.drawString(mc.font, jam, cx - mc.font.width(jam) / 2, cy + 12, 0xFFFF5555, true);
        }
    }
}
