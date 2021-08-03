package lib.toma.animations.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class ControlButton extends Widget {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");
    private final IGetter getter;
    private final ISetter setter;

    public ControlButton(int x, int y, int width, int height, ITextComponent title, IGetter getter, ISetter setter) {
        super(x, y, width, height, title);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void onClick(double p_230982_1_, double p_230982_3_) {
        boolean value = getter.get();
        setter.set(!value);
    }

    @Override
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(TEXTURE);
        RenderSystem.enableDepthTest();
        FontRenderer fontrenderer = minecraft.font;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(p_230431_1_, this.x, this.y, this.isFocused() ? 20.0F : 0.0F, this.getter.get() ? 20.0F : 0.0F, 20, this.height, 64, 64);
        this.renderBg(p_230431_1_, minecraft, p_230431_2_, p_230431_3_);
        if (this.getMessage() != null) {
            drawString(p_230431_1_, fontrenderer, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @FunctionalInterface
    public interface IGetter {
        boolean get();
    }

    @FunctionalInterface
    public interface ISetter {
        void set(boolean value);
    }
}
