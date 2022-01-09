package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.client.screen.widgets.*;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Locale;

public class SkillsView extends View {

    private static final ResourceLocation PERK_VIEW = GunsRPG.makeResource("textures/screen/perk_view_icon.png");
    private FooterWidget footerWidget;
    private SkillInfoWidget skillInfoWidget;
    private ViewSwitchWidget viewSwitchWidget;

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
        skillInfoWidget = addWidget(new SkillInfoWidget(x, y + height - 80, width, 80, manager.getContext()));
        // add perk/skill switch
        viewSwitchWidget = addWidget(new ViewSwitchWidget(x + width - 42, y + height - 62, 32, 32, PERK_VIEW));
        viewSwitchWidget.setClickEvent(this::openPerkView);

        this.updateSkillInformationVisibility(false);
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderView(stack, mouseX, mouseY, partialTicks);
    }

    private void openPerkView() {

    }

    private void updateCanvasSource(SkillCategory category) {
        updateSkillInformationVisibility(false);
    }

    private void skillClicked(SkillType<?> type) {
        skillInfoWidget.updateSource(type);
        this.updateSkillInformationVisibility(type != null);
    }

    private void updateSkillInformationVisibility(boolean visibilityState) {
        synchronized (lock) {
            this.footerWidget.visible = !visibilityState;
            this.viewSwitchWidget.visible = !visibilityState;
            this.skillInfoWidget.visible = visibilityState;
        }
    }
}
