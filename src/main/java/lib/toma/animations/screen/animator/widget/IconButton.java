package lib.toma.animations.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
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

    private static final ResourceLocation ANIMATOR_ICONS = GunsRPG.makeResource("textures/icons/animator/animator_icons.png");
    private static final int ELEMENTS_PER_ROW = 2;
    private static final int TEX_SIZE = 16;

    private final float xTex1;
    private final float yTex1;
    private final float xTex2;
    private final float yTex2;

    public IconButton(int x, int y, int width, int height, int textureIndex, IPressable pressable, ITooltip tooltip) {
        super(x, y, width, height, StringTextComponent.EMPTY, pressable, tooltip);
        int tx = textureIndex % ELEMENTS_PER_ROW;
        int ty = textureIndex / ELEMENTS_PER_ROW;

        double size = TEX_SIZE / ((float) ELEMENTS_PER_ROW * TEX_SIZE);
        xTex1 = (float) (tx * size);
        xTex2 = (float) (xTex1 + size);
        yTex1 = (float) (ty * size);
        yTex2 = (float) (yTex1 + size);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (isHovered()) {
            AbstractGui.fill(stack, x, y, x + width, y + height, 0x67FFFFFF);
            renderToolTip(stack, mouseX, mouseY);
        }
        fillUV(stack.last().pose(), x + 2, y + 2, x + width - 2, y + height - 2);
    }

    private void fillUV(Matrix4f pose, int x1, int y1, int x2, int y2) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bind(ANIMATOR_ICONS);
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.vertex(pose, x1, y1, 0).uv(xTex1, yTex1).endVertex();
        builder.vertex(pose, x1, y2, 0).uv(xTex1, yTex2).endVertex();
        builder.vertex(pose, x2, y2, 0).uv(xTex2, yTex2).endVertex();
        builder.vertex(pose, x2, y1, 0).uv(xTex2, yTex1).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
    }
}
