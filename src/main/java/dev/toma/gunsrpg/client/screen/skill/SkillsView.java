package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.client.screen.widgets.FooterWidget;
import dev.toma.gunsrpg.client.screen.widgets.HeaderWidget;
import dev.toma.gunsrpg.client.screen.widgets.NavigatorWidget;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Locale;

public class SkillsView extends View {

    private FooterWidget footerWidget;

    public SkillsView(int windowWidth, int windowHeight, IViewManager manager) {
        super(windowWidth, windowHeight, manager);
    }

    @Override
    protected void init() {
        clear();
        // add header
        ITextComponent username = client.player.getName();
        ITextComponent header = new TranslationTextComponent("view.skill.header", username.getString());
        addWidget(new HeaderWidget(0, 0, width, 20, header, font));
        // add category selector
        SkillCategory[] navEntries = Arrays.stream(SkillCategory.values()).sorted((o1, o2) -> o2.ordinal() - o1.ordinal()).toArray(SkillCategory[]::new);
        NavigatorWidget<SkillCategory> nav = addWidget(new NavigatorWidget<>(x, y + 20, width, 20, navEntries));
        nav.setTextFormatter(cat -> cat.name().toLowerCase(Locale.ROOT));
        nav.setClickResponder(this::updateCanvasSource);
        // add canvas
        // add footer with data
        IViewContext context = manager.getContext();
        IPlayerData data = context.getData();
        IPointProvider pointProvider = data.getGenericData();
        footerWidget = addWidget(new FooterWidget(x, y + height - 20, width, 20, client.font, pointProvider));
        footerWidget.setColorSchema(0xFFFF, 0xCCCC);
        // add skill info panel
        // add perk/skill switch

        this.updateSkillInformationVisibility(false);
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderView(stack, mouseX, mouseY, partialTicks);
    }

    private void updateCanvasSource(SkillCategory category) {
        updateSkillInformationVisibility(false);
    }

    private void updateSkillInformationVisibility(boolean visibilityState) {
        this.footerWidget.visible = !visibilityState;
    }
}
