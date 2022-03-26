package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.configuration.api.client.widget.ITickable;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.Easings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

public class PerkWidget extends Widget implements ITickable {

    private static final DecimalFormat FORMAT = new DecimalFormat("0.00", ModUtils.DOT_DECIMAL_SEPARATOR);

    private final FontRenderer font;
    private final Perk perk;
    private final ResourceLocation icon;
    private final State state;
    private final float value;
    private final boolean maxLevel;
    private int loadTimer = 40;
    private int prevLoadTimer;

    public PerkWidget(int x, int y, int width, int height, FontRenderer renderer, Perk perk, float value, State state) {
        super(x, y, width, height, SkillUtil.Localizations.makeReadable(perk.getPerkId()));
        this.perk = perk;
        this.font = renderer;
        this.value = value;
        this.state = state;
        ResourceLocation perkId = perk.getPerkId();
        this.icon = new ResourceLocation(perkId.getNamespace(), "textures/icons/perk/" + perkId.getPath() + ".png");
        this.maxLevel = ModUtils.equals(perk.getBounds(state == State.DEBUFF ? PerkType.DEBUFF : PerkType.BUFF), value, 0.001);
    }

    @Override
    public void tick() {
        if (state == State.NULLIFIED || state == State.NONE) {
            return;
        }
        prevLoadTimer = loadTimer;
        if (loadTimer > 0) {
            --loadTimer;
        }
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        int bgColor = state.bgColor | 0xFF << 24;
        int bgColorSecondary = RenderUtils.darken(bgColor, 0.6F) | 0xFF << 24;
        int borderColor = maxLevel ? 0xFFAE00 : 0;
        float interpolatedLoadTimer = AnimationUtils.linearInterpolate(loadTimer, prevLoadTimer, partialTicks);
        float numberProgress = 1.0F - (interpolatedLoadTimer / 40.0F);
        float value = Easings.EASE_OUT_EXP.ease(numberProgress) * this.value;
        Matrix4f pose = matrix.last().pose();
        RenderUtils.drawSolid(pose, x, y, x + width, y + height, 0xFF << 24 | borderColor);
        RenderUtils.drawGradient(pose, x + 1, y + 1, x + width - 1, y + height - 1, bgColor, bgColorSecondary);
        Minecraft.getInstance().getTextureManager().bind(icon);
        RenderUtils.drawTex(pose, x + 2, y + 2, x + 18, y + 18);
        String text = FORMAT.format(Math.abs(value) * 100) + "%";
        if (maxLevel) {
            text = TextFormatting.BOLD + text;
        }
        int width = font.width(text);
        font.drawShadow(matrix, text, x + this.width - 2 - width, y + (height - font.lineHeight) / 2f, 0xCCCCCC);
        if (isHovered) {
            ITextComponent name = this.getMessage();
            int nameWidth = font.width(name);
            font.drawShadow(matrix, name, x + (this.width - nameWidth) / 2f, y + height + 2, 0xFFFFFF);
        }
    }

    public enum State {
        BUFF(0x9900),
        DEBUFF(0x990000),
        NULLIFIED(0x999900),
        NONE(0x666666);

        private final int bgColor;

        State(int bgColor) {
            this.bgColor = bgColor;
        }
    }
}
