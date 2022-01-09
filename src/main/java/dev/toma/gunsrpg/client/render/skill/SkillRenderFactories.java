package dev.toma.gunsrpg.client.render.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class SkillRenderFactories {

    public static void renderIcon(MatrixStack stack, int x, int y, ResourceLocation location) {
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        manager.bind(location);
        RenderUtils.drawTex(stack.last().pose(), x, y, x + 16, y + 16);
    }

    public static void renderItem(MatrixStack stack, int x, int y, ItemStack itemStack) {
        Minecraft client = Minecraft.getInstance();
        ItemRenderer renderer = client.getItemRenderer();
        renderer.renderGuiItem(itemStack, x, y);
    }
}
