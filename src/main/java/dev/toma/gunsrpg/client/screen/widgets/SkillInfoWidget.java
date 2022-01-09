package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SkillInfoWidget extends ContainerWidget {

    private static final ITextComponent EXTENSIONS = new TranslationTextComponent("view.skill.extensions");

    private SkillType<?> src;
    private ITextComponent title;
    private ITextComponent[] description;
    private boolean extensions;

    public SkillInfoWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
        init();
    }

    public void updateSource(SkillType<?> type) {
        this.src = type;
        if (src != null) {
            title = type.getTitle();
            description = type.getDescription();
            extensions = !ModUtils.isNullOrEmpty(type.getHierarchy().getExtensions());
        }
        init();
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Matrix4f pose = stack.last().pose();
        RenderUtils.drawSolid(pose, x, y, x + width, y + height, 0x44 << 24);

        super.renderButton(stack, mouseX, mouseY, partialTicks);
    }

    private void init() {
        clear();
        if (src == null) {
            addWidget(new Label(x, y, width, height, new StringTextComponent("Nothing to show")));
            return;
        }
        addWidget(new Label(x + 10, y + 5, width - 100, 15, title));
        addWidget(new Button(x + width - 90, y + 5, 80, 20, EXTENSIONS, this::showExtensionsView));
    }

    private void showExtensionsView(Button button) {

    }

    private static class Label extends Widget {

        Label(int x, int y, int width, int height, ITextComponent label) {
            super(x, y, width, height, label);
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            FontRenderer renderer = Minecraft.getInstance().font;
            float yCenter = RenderUtils.getVerticalCenter(renderer, this);
            renderer.drawShadow(matrix, getMessage(), x, yCenter, 0xFFFFFF);
        }
    }
}
