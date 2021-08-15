package lib.toma.animations.engine.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;

public class IconButton extends Button {

    public static final Settings SETTINGS = new Settings();

    private final ResourceLocation source;
    private final float xTex1;
    private final float yTex1;
    private final float xTex2;
    private final float yTex2;

    public IconButton(int x, int y, int width, int height, int textureIndex, IPressable pressable, ITooltip tooltip) {
        super(x, y, width, height, StringTextComponent.EMPTY, pressable, tooltip);
        this.source = SETTINGS.src;
        int max = SETTINGS.cells;
        int tx = textureIndex % max;
        int ty = textureIndex / max;
        int texSize = SETTINGS.texSize;
        int size = max * SETTINGS.texSize;
        int xTex = tx * texSize;
        int yTex = ty * texSize;
        xTex1 = xTex / (float) size;
        yTex1 = yTex / (float) size;
        xTex2 = (xTex + texSize) / (float) size;
        yTex2 = (yTex + texSize) / (float) size;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (isHovered() && active) {
            AbstractGui.fill(stack, x - 1, y - 1, x + width + 1, y + height + 1, 0x67FFFFFF);
            renderToolTip(stack, mouseX, mouseY);
        }
        fillUV(stack.last().pose(), x, y, x + width, y + height, active ? 0xFFFFFFFF : 0x67999999);
    }

    private void fillUV(Matrix4f pose, int x1, int y1, int x2, int y2, int colorMask) {
        int alpha = (colorMask >> 24) & 255;
        int red = (colorMask >> 16) & 255;
        int green = (colorMask >> 8) & 255;
        int blue = colorMask & 255;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bind(source);
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        builder.vertex(pose, x1, y1, 0).color(red, green, blue, alpha).uv(xTex1, yTex1).endVertex();
        builder.vertex(pose, x1, y2, 0).color(red, green, blue, alpha).uv(xTex1, yTex2).endVertex();
        builder.vertex(pose, x2, y2, 0).color(red, green, blue, alpha).uv(xTex2, yTex2).endVertex();
        builder.vertex(pose, x2, y1, 0).color(red, green, blue, alpha).uv(xTex2, yTex1).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
    }

    public static class Settings {

        private ResourceLocation src;
        private int cells;
        private int texSize;

        public void set(ResourceLocation src) {
            set(src, cells, texSize);
        }

        public void set(ResourceLocation src, int cells) {
            set(src, cells, texSize);
        }

        public void set(ResourceLocation src, int cells, int texSize) {
            this.src = src;
            this.cells = cells;
            this.texSize = texSize;
        }
    }
}
