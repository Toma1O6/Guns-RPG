package dev.toma.gunsrpg.client.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHandRenderer {

    @SideOnly(Side.CLIENT)
    void renderRightArm();

    @SideOnly(Side.CLIENT)
    void renderLeftArm();

    @SideOnly(Side.CLIENT)
    default void renderArm(EnumHandSide side) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
        Render<AbstractClientPlayer> render = mc.getRenderManager().getEntityRenderObject(mc.player);
        RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(40.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
        if (side == EnumHandSide.RIGHT) {
            GlStateManager.translate(0.8F, -0.3F, -0.4F);
            GlStateManager.rotate(-41.0F, 0.0F, 0.0F, 1.0F);
            renderplayer.renderRightArm(mc.player);
        } else {
            GlStateManager.translate(-0.5F, 0.6F, -0.36F);
            GlStateManager.rotate(-41.0F, 0.0F, 0.0F, 1.0F);
            renderplayer.renderLeftArm(mc.player);
        }
        GlStateManager.popMatrix();
    }
}
