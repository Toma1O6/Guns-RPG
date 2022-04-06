package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.client.render.IOrderedRender;
import dev.toma.gunsrpg.client.screen.widgets.*;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.RenderUtils;
import lib.toma.animations.QuickSort;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Comparator;

public class ExtensionsView extends View {

    private final View last;
    private final SkillType<?> head;

    private PannableWidget canvas;
    private FooterWidget footer;
    private SkillInfoWidget skillInfo;

    public ExtensionsView(int width, int height, IViewManager manager, View last, SkillType<?> head) {
        super(width, height, manager);
        this.last = last;
        this.head = head;
    }

    @Override
    protected void init() {
        clear();
        // skills
        canvas = addWidget(new PannableWidget(x, y + 30, width, height - 60));
        canvas.setEmptyClickResponder(this::skillClicked);
        // return button
        addWidget(new BackButton(x + 5, y + 5, 20, 20, this::backButtonClicked));
        // header
        ITextComponent title = new TranslationTextComponent("view.extension.header", head.getTitle());
        addWidget(new HeaderWidget(x, y, width, 30, title, font));
        // footer
        IViewContext context = manager.getContext();
        IPlayerData data = context.getData();
        ISkillProperties properties = head.getHierarchy().getExtensions()[0].getProperties();
        ITransactionValidator validator = properties.getTransactionValidator();
        IPointProvider provider = validator.getData(data);
        footer = addWidget(new FooterWidget(x, y + height - 20, width, 20, font, provider));
        footer.setColorSchema(0xFFFF00, 0xCCCC00);
        // info
        skillInfo = addWidget(new SkillInfoWidget(x, y + height - 80, width, 80, manager));

        // visibility
        showSkills();
        setSkillInfoVisibility(false);
        sortRenderOrder();
    }

    private void skillClicked(@Nullable SkillType<?> type) {
        skillInfo.updateSource(type);
        setSkillInfoVisibility(type != null);
    }

    private void backButtonClicked(Button button) {
        manager.setView(last);
    }

    private void showSkills() {
        canvas.fill(this::placeSkills);
    }

    private void placeSkills(PannableWidget.IContentManager manager, int x, int y) {
        IViewContext context = this.manager.getContext();
        int index = 0;
        int level = 0;
        int posY = y;
        SkillType<?>[] ext = head.getHierarchy().getExtensions();
        QuickSort.sort(ext, Comparator.comparing(SkillType::getProperties, Comparator.comparingInt(ISkillProperties::getRequiredLevel)));
        for (SkillType<?> type : ext) {
            ISkillProperties properties = type.getProperties();
            int lvl = properties.getRequiredLevel();
            if (lvl > level) {
                level = lvl;
                posY += 40;
                index = 0;
            }
            SkillWidget widget = manager.add(new SkillWidget(x + 40 + (index++) * 35, 30 + posY, 22, 22, type, context, false));
            widget.setClickResponder(this::skillClicked);
        }
    }

    private void setSkillInfoVisibility(boolean visibility) {
        synchronized (lock) {
            footer.visible = !visibility;
            skillInfo.visible = visibility;
            canvas.updateSize(width, height - (visibility ? 110 : 50));
        }
    }

    private static class BackButton extends Button implements IOrderedRender {

        BackButton(int x, int y, int width, int height, IPressable clickEvent) {
            super(x, y, width, height, new StringTextComponent("<<<"), clickEvent);
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            if (isHovered) {
                RenderUtils.drawSolid(matrix.last().pose(), x, y, x + width, y + height, 0x66FFFFFF);
            }
            FontRenderer renderer = Minecraft.getInstance().font;
            RenderUtils.drawCenteredShadowText(matrix, getMessage(), renderer, this, 0xFFFFFF);
        }

        @Override
        public int getRenderIndex() {
            return 1;
        }
    }
}
