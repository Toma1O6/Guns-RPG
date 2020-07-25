package dev.toma.gunsrpg.common.skills.interfaces;

import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface Cooldown extends TickableSkill, OverlayRenderer {

    int getCooldown();

    void setOnCooldown();

    int getMaxCooldown();

    void onUse(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    @Override
    default void drawOnTop(int x, int y, int width, int heigth) {
        float f = getCooldown() / (float) getMaxCooldown();
        if(f == 0) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 1);
        ModUtils.renderColor(x, y + heigth, x + width, y + heigth + 3, 0.0F, 0.0F, 0.0F, 1.0F);
        ModUtils.renderColor(x + 1, y + heigth + 1, x + 1 + (int)((width - 1) * f), y + heigth + 2, 1.0F, 0.2F, 0.0F, 1.0F);
        String cooldown = ModUtils.formatTicksToTime(this.getCooldown());
        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
        renderer.drawString(cooldown, x + (width - renderer.getStringWidth(cooldown)) / 2, y + heigth + 4, 0xffffff);
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    @Override
    default void renderInHUD(ISkill skill, int renderIndex, int left, int top) {
        if(skill.apply(Minecraft.getMinecraft().player)) {
            int x = left + renderIndex * 20;
            ModUtils.renderColor(x, top, x + 20, top + 20, 0.0F, 0.0F, 0.0F, 0.4F);
            ModUtils.renderTexture(x + 2, top + 2, x + 18, top + 18, skill.getType().icon);
        }
    }
}
