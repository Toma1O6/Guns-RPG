package dev.toma.gunsrpg.client.render.debuff;

import dev.toma.gunsrpg.common.debuffs.IStagedDebuff;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResistDebuffRenderer<D extends IStagedDebuff> extends IconDebuffRenderer<D> {

    private static final ITextComponent RESIST_LABEL = new TranslationTextComponent("debuff.text.resist");

    public ResistDebuffRenderer(ResourceLocation iconTexture) {
        super(iconTexture);
    }

    public ResistDebuffRenderer(ResourceLocation iconTexture, RenderControls controls) {
        super(iconTexture, controls);
    }

    @Override
    protected String getDebuffText(D debuff) {
        return RESIST_LABEL.getString();
    }

    @Override
    protected int getTextColor(D debuff, int textFadeIn, int colorFadeIn, boolean isDisabled) {
        return RenderUtils.combine4i(0, 255, 255, textFadeIn);
    }
}
