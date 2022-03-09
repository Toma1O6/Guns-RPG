package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.configuration.api.client.widget.ITickable;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

public class PerkWidget extends Widget implements ITickable {

    private final FontRenderer font;
    private final Perk perk;
    private final ResourceLocation icon;
    private final State state;
    private final float value;

    public PerkWidget(int x, int y, int width, int height, FontRenderer renderer, Perk perk, float value, State state) {
        super(x, y, width, height, SkillUtil.Localizations.makeReadable(perk.getPerkId()));
        this.perk = perk;
        this.font = renderer;
        this.value = value;
        this.state = state;
        ResourceLocation perkId = perk.getPerkId();
        this.icon = new ResourceLocation(perkId.getNamespace(), "textures/icons/perk/" + perkId.getPath() + ".png");
    }

    @Override
    public void tick() {
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        int bgColor = state.bgColor | 0xFF << 24;
        int bgColorSecondary = RenderUtils.darken(bgColor, 0.7F) | 0xFF << 24;
        Matrix4f pose = matrix.last().pose();
        RenderUtils.drawSolid(pose, x, y, x + width, y + height, 0xFF << 24);
        RenderUtils.drawGradient(pose, x + 1, y + 1, x + width - 1, y + height - 1, bgColor, bgColorSecondary);
        Minecraft.getInstance().getTextureManager().bind(icon);
        RenderUtils.drawTex(pose, x + 2, y + 2, x + 18, y + 18);
        String text = CrystalItem.DECIMAL_FORMAT.format(value) + "%";
        int width = font.width(text);
        font.drawShadow(matrix, text, x + this.width - 2 - width, y + (height - font.lineHeight) / 2f, 0xFFFFFF);
        if (isHovered) {
            ITextComponent name = this.getMessage();
            int nameWidth = font.width(name);
            font.drawShadow(matrix, name, x + (this.width - nameWidth) / 2f, y + height + 2, 0xFFFFFF);
        }
    }

    public enum State {
        BUFF(0xFF00),
        DEBUFF(0xFF0000),
        NULLIFIED(0xFFFF00),
        NONE(0x666666);

        private final int bgColor;

        State(int bgColor) {
            this.bgColor = bgColor;
        }
    }
}
