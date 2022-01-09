package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.client.screen.skill.IViewContext;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class SkillInfoWidget extends ContainerWidget {

    private static final ITextComponent EXTENSIONS = new TranslationTextComponent("view.skill.extensions");
    private static final ITextComponent PURCHASE = new TranslationTextComponent("view.skill.purchase");

    private final IViewContext context;
    private SkillType<?> src;
    private ITextComponent title;
    private ITextComponent[] description;
    private boolean extensions;
    private int unlockState;

    public SkillInfoWidget(int x, int y, int width, int height, IViewContext context) {
        super(x, y, width, height);
        this.context = context;
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
        int unlockState = this.getUnlockState();
        addWidget(new Icon(x + 5, y + 5, 16, 16, src.getDisplayData()));
        addWidget(new Label(x + 26, y + 5, width - 100, 15, title));
        int rightColX = x + width - 90;
        Button ext = addWidget(new Button(rightColX, y + 5, 80, 20, EXTENSIONS, this::showExtensionsView));
        ext.active = extensions;
        Button buy = addWidget(new Button(rightColX, y + 27, 80, 20, PURCHASE, this::purchaseClicked));
        buy.active = unlockState == 1;
        buy.visible = unlockState > 0;

        ISkillProperties properties = src.getProperties();
        ITextComponent levelCondiditon = new TranslationTextComponent("view.skill.required_level", properties.getRequiredLevel());
        ITextComponent price = new TranslationTextComponent("view.skill.price", properties.getPrice());
        addWidget(new Label(rightColX, y + 50, 80, 15, levelCondiditon));
        addWidget(new Label(rightColX, y + 65, 80, 15, price));

        FontRenderer renderer = Minecraft.getInstance().font;
        int maxWidth = width - 130;
        int lineIndex = 0;
        for (ITextComponent component : description) {
            List<IReorderingProcessor> list = renderer.split(component, maxWidth);
            for (IReorderingProcessor processor : list) {
                addWidget(new Label(x + 5, y + 25 + (lineIndex++ * 10), maxWidth, 10, processor));
            }
        }
    }

    private void showExtensionsView(Button button) {

    }

    private void purchaseClicked(Button button) {

        init();
    }

    private int getUnlockState() {
        IPlayerData data = context.getData();
        ITransactionValidator validator = src.getProperties().getTransactionValidator();
        ISkillProvider provider = data.getSkillProvider();
        if (provider.hasSkill(src)) {
            return 0;
        } else {
            return validator.canUnlock(data, src) ? 1 : 2;
        }
    }

    private static class Label extends Widget {

        private IReorderingProcessor drawable;

        Label(int x, int y, int width, int height, ITextComponent label) {
            super(x, y, width, height, label);
        }

        Label(int x, int y, int width, int height, IReorderingProcessor processor) {
            this(x, y, width, height, StringTextComponent.EMPTY);
            this.drawable = processor;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            FontRenderer renderer = Minecraft.getInstance().font;
            float yCenter = RenderUtils.getVerticalCenter(renderer, this);
            if (drawable == null) {
                renderer.drawShadow(matrix, getMessage(), x, yCenter, 0xFFFFFF);
            } else {
                renderer.drawShadow(matrix, drawable, x, yCenter, 0xFFFFFF);
            }
        }
    }

    private static class Icon extends Widget {

        final DisplayData data;

        Icon(int x, int y, int width, int height, DisplayData data) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.data = data;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            data.renderAt(matrix, x, y);
        }
    }
}
