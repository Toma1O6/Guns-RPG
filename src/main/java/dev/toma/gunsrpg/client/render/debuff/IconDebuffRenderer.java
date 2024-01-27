package dev.toma.gunsrpg.client.render.debuff;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.debuffs.IStagedDebuff;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.math.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class IconDebuffRenderer<D extends IStagedDebuff> implements IDebuffRenderer<D> {

    public static final ResourceLocation POISON_ICON = icon("poison");
    public static final ResourceLocation INFECTION_ICON = icon("infection");
    public static final ResourceLocation FRACTURE_ICON = icon("fracture");
    public static final ResourceLocation BLEED_ICON = icon("bleed");

    private final ResourceLocation iconTexture;
    private final RenderControls controls;

    public IconDebuffRenderer(ResourceLocation iconTexture) {
        this(iconTexture, RenderControls.getDefault());
    }

    public IconDebuffRenderer(ResourceLocation iconTexture, RenderControls controls) {
        this.iconTexture = iconTexture;
        this.controls = controls;
    }

    private static ResourceLocation icon(String name) {
        return GunsRPG.makeResource(String.format("textures/icons/%s.png", name));
    }

    @Override
    public void drawOnScreen(D debuff, IAttributeProvider attributes, MatrixStack poseStack, int left, int top, int width, int height, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager manager = mc.getTextureManager();
        Matrix4f pose = poseStack.last().pose();
        FontRenderer font = mc.font;
        int transparency = 0x88 << 24;
        int addProgress = Math.min(debuff.ticksSinceAdded(), controls.addEffectTime);
        float backgroundSlideIn = Mth.asLinearInterpolatedFunction(addProgress, 0, controls.slideInTime, partialTicks);
        RenderUtils.drawGradient(pose, left, top, left + (int) (width * backgroundSlideIn), top + height, transparency, transparency);
        float contentFadeIn = Mth.asLinearFunction(addProgress, controls.slideInTime, controls.slideInTime + controls.contentFadeIn);
        int textFadeIn = Math.max(4, (int) (contentFadeIn * 0xff));
        int colorFadeInEffect = (int) (contentFadeIn * 0xff);
        int iconFadeIn = (int) (contentFadeIn * 0xff) << 24;
        manager.bind(iconTexture);
        RenderUtils.drawColoredTex(pose, left + 2, top + 2, left + 18, top + 18, 0xFFFFFF | iconFadeIn);
        boolean isDisabled = debuff.isFrozen(attributes);
        String text = getDebuffText(debuff);
        int textWidth = font.width(text);
        int resultColor = getTextColor(debuff, textFadeIn, colorFadeInEffect, isDisabled);
        if (isDisabled) {
            float progress = debuff.getBlockingProgress(attributes);
            RenderUtils.drawSolid(pose, left, top + height - 1, left + (int) (width * progress), top + height, 0xFF00CC00);
        }
        font.draw(poseStack, text, left + width - 5 - textWidth, top + 6.5F, resultColor);
    }

    protected String getDebuffText(D debuff) {
        return debuff.getCurrentProgress() + " %";
    }

    protected int getTextColor(D debuff, int textFadeIn, int colorFadeIn, boolean isDisabled) {
        int healProgress = debuff.ticksSinceHealed();
        int damageProgress = debuff.ticksSinceProgressed();
        boolean isHealing = healProgress < damageProgress && healProgress < 15;
        float effect;
        if (isDisabled) {
            return 0xff << 8;
        }
        if (isHealing) {
            effect = Mth.asLinearFunction(healProgress, 0, controls.effectLength);
            int clr = Math.min((int) (effect * 0xff), colorFadeIn);
            return RenderUtils.combine4i(clr, 255, clr, textFadeIn);
        } else {
            effect = Mth.asLinearFunction(damageProgress, 0, controls.effectLength);
            int clr = Math.min((int) (effect * 0xff), colorFadeIn);
            return RenderUtils.combine4i(255, clr, clr, textFadeIn);
        }
    }

    public static class RenderControls {

        private static final RenderControls DEFAULT_CONTROLS = new RenderControls(35, 20, 25);

        private final int addEffectTime;
        private final int slideInTime;
        private final int contentFadeIn;
        private final int effectLength;

        public RenderControls(int addEffect, int slideIn, int effect) {
            addEffectTime = addEffect;
            slideInTime = slideIn;
            contentFadeIn = addEffect - slideIn;
            effectLength = effect;
        }

        public static RenderControls getDefault() {
            return DEFAULT_CONTROLS;
        }
    }
}
