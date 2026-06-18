package com.wf.firearms.client.bloodmoon;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wf.firearms.GunsRpg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, value = Dist.CLIENT)
public final class BloodmoonClientEffects {
    private BloodmoonClientEffects() {}

    /** 血月暗红遮罩（含白天强制测试）；不依赖天空 mixin。 */
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != Level.OVERWORLD || mc.options.hideGui) {
            return;
        }
        float intensity = BloodmoonClientState.visualIntensity(mc.level.getDayTime());
        if (intensity <= 0f) {
            return;
        }
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        int alpha = (int) (intensity * 72);
        GuiGraphics g = event.getGuiGraphics();
        RenderSystem.enableBlend();
        g.fill(0, 0, w, h, (alpha << 24) | 0x550000);
        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onFogDensity(ViewportEvent.RenderFog event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != Level.OVERWORLD) {
            return;
        }
        float intensity = BloodmoonClientState.visualIntensity(mc.level.getDayTime());
        if (intensity <= 0f) {
            return;
        }
        float scale = 1f - intensity * 0.55f;
        event.scaleFarPlaneDistance(scale);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != Level.OVERWORLD) {
            return;
        }
        float intensity = BloodmoonClientState.visualIntensity(mc.level.getDayTime());
        if (intensity <= 0f) {
            return;
        }
        float r = event.getRed();
        float g = event.getGreen();
        float b = event.getBlue();
        float r1 = 1f - r;
        float idiff = 1f - intensity;
        event.setRed(r + r1 * intensity * 0.4f);
        event.setGreen(g * idiff);
        event.setBlue(b * idiff);
    }
}
