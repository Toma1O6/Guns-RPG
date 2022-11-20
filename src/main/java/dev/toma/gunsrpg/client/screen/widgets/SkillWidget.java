package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.client.ISkillRenderer;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.client.render.skill.SkillRendererRegistry;
import dev.toma.gunsrpg.client.screen.skill.IViewContext;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ITickable;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.math.Mth;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.Easings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;

import java.util.Objects;

public class SkillWidget extends Widget implements ITickable {

    public static final DisplayData UNKNOWN = DisplayData.create(DisplayType.ICON, GunsRPG.makeResource("textures/icons/unknown.png"));

    private final SkillType<?> source;
    private final IViewContext context;
    private final FontRenderer renderer;
    private final int tagSize;
    private final boolean invisible;
    private IClickResponder<SkillType<?>> responder = type -> {};
    private int lastAnimationTime;
    private int animationTime;
    private int glowTimer;

    public SkillWidget(int x, int y, int width, int height, SkillType<?> source, IViewContext context, boolean invisible) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.source = source;
        this.context = context;
        this.renderer = Minecraft.getInstance().font;
        this.tagSize = invisible ? renderer.width("???") : renderer.width(source.getTitle());
        this.invisible = invisible;
    }

    @Override
    public void tick() {
        lastAnimationTime = animationTime;
        animationTime = MathHelper.clamp((isHovered ? ++animationTime : --animationTime), 0, 15);
        ++glowTimer;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (isHovered) {
            source.acknowledge();
        }
        Matrix4f pose = matrix.last().pose();
        ISkillProvider provider = context.getData().getSkillProvider();
        boolean unlocked = !invisible && provider.hasSkill(source);
        int primary = unlocked ? 0xFF00BB00 : 0xFF343434;
        int secondary = unlocked ? 0xFF006600 : 0xFF232323;
        renderBackgroundFrame(pose);
        RenderUtils.drawGradient(pose, x + 1, y + 1, x + width - 1, y + height - 1, primary, secondary);
        DisplayData data = invisible ? UNKNOWN : source.getDisplayData();
        data.renderAt(matrix, x + 3, y + 3);
        renderExtraData(matrix, source, provider, partialTicks);
        renderHoverInfo(partialTicks, matrix, pose);
    }

    public void setClickResponder(IClickResponder<SkillType<?>> responder) {
        this.responder = Objects.requireNonNull(responder);
    }

    @Override
    public void onClick(double p_230982_1_, double p_230982_3_) {
        responder.onElementClicked(source);
    }

    private void renderHoverInfo(float partialTicks, MatrixStack stack, Matrix4f pose) {
        float current = getAnimationProgress(animationTime);
        float last = getAnimationProgress(lastAnimationTime);
        float rawAnimationProgress = AnimationUtils.linearInterpolate(current, last, partialTicks);
        renderTagBackground(rawAnimationProgress < 0.5F ? 0.0F : rawAnimationProgress < 0.9F ? (rawAnimationProgress - 0.5F) / 0.4F : 1.0F, pose);
        renderTag(rawAnimationProgress < 0.8F ? 0.0F : (rawAnimationProgress - 0.8F) / 0.2F, stack);
    }

    private void renderTagBackground(float raw, Matrix4f pose) {
        float f = Easings.EASE_OUT_SINE.ease(raw);
        float center = this.getComponentCenter();
        float tagHalfWidth = 2 + tagSize / 2.0F;
        float left = center - tagHalfWidth * f;
        float right = center + tagHalfWidth * f;
        float y = this.y + height + 3;
        RenderUtils.drawSolid(pose, left, y, right, y + 13, 0x77 << 24);
    }

    private void renderTag(float raw, MatrixStack stack) {
        if (raw < 0.2F) return;
        int alpha = (int) (255 * raw);
        if (invisible) {
            renderer.draw(stack, "???", getComponentCenter() - tagSize / 2.0F, y + height + 6, alpha << 24 | 0xFFFFFF);
            return;
        }
        renderer.draw(stack, source.getTitle(), getComponentCenter() - tagSize / 2.0F, y + height + 6, alpha << 24 | 0xFFFFFF);
    }

    private float getAnimationProgress(int value) {
        return value / 15.0F;
    }

    private float getComponentCenter() {
        return x + width / 2.0F;
    }

    private void renderBackgroundFrame(Matrix4f pose) {
        int bgColor;
        if (!invisible && source.isFresh()) {
            int glowTime = this.glowTimer % 40;
            float raw = Mth.triangleFunc(glowTime / 40.0F);
            float eased = Easings.EASE_IN_OUT_QUAD.ease(raw);
            int saturation = (int) (255 * eased);
            bgColor = 0xFF << 24 | (saturation << 16) | (saturation << 8);
        } else {
            bgColor = isHovered ? 0xFFFFFFFF : 0xFF << 24;
        }

        RenderUtils.drawSolid(pose, x, y, x + width, y + height, bgColor);
    }

    private <S extends ISkill> void renderExtraData(MatrixStack stack, SkillType<S> type, ISkillProvider provider, float partialTicks) {
        ISkillRenderer<S> renderer = SkillRendererRegistry.getRendererFor(type);
        if (invisible || renderer == null) return;
        S skill = SkillUtil.getTopHierarchySkill(type, provider);
        if (skill == null) return;
        if (skill.getType() != type) return;
        float raw = AnimationUtils.linearInterpolate(
                getAnimationProgress(animationTime),
                getAnimationProgress(lastAnimationTime),
                partialTicks
        );
        renderer.renderExtraSkillData(skill, stack, this.renderer, x, y, width, height, raw < 0.5F ? 1.0F - (raw / 0.5F) : 0.0F);
    }
}
