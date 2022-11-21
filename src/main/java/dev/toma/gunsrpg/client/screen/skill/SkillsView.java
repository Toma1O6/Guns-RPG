package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.api.common.data.IProgressData;
import dev.toma.gunsrpg.client.screen.widgets.*;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.util.math.Mth;
import lib.toma.animations.Easings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SkillsView extends View {

    private NavigatorWidget<SkillCategory> navigatorWidget;
    private PannableWidget skillViewWidget;
    private FooterWidget footerWidget;
    private SkillInfoWidget skillInfoWidget;
    private ViewSwitchWidget viewSwitchWidget;

    public SkillsView(int windowWidth, int windowHeight, IViewManager manager) {
        super(windowWidth, windowHeight, manager);
    }

    @Override
    protected void init() {
        clear();
        // add perk/skill switch
        viewSwitchWidget = addWidget(new ViewSwitchWidget(x + width - 42, height - 62, 32, 32, new ItemStack(ModItems.PERKPOINT_BOOK)));
        viewSwitchWidget.setClickEvent(this::openPerkView);
        viewSwitchWidget.setColorSchema(0xFFA60C, 0xFFD21E);
        // add skill view
        skillViewWidget = addWidget(new PannableWidget(x, y + 40, width, height - 60));
        skillViewWidget.setEmptyClickResponder(this::skillClicked);
        // add header
        ITextComponent username = client.player.getName();
        ITextComponent header = new TranslationTextComponent("view.skill.header", username.getString());
        addWidget(new HeaderWidget(0, 0, width, 20, header, font));
        // add category selector
        SkillCategory[] navEntries = Arrays.stream(SkillCategory.values())
                .sorted((o1, o2) -> o2.ordinal() - o1.ordinal())
                .toArray(SkillCategory[]::new);
        navigatorWidget = addWidget(new NavigatorWidget<>(x, y + 20, width, 20, navEntries));
        navigatorWidget.setTextFormatter(cat -> cat.name().toLowerCase(Locale.ROOT));
        navigatorWidget.setClickResponder(this::updateCanvasSource);
        navigatorWidget.setColorProvider(CategoryColorProvider::new);
        // add footer with data
        IViewContext context = manager.getContext();
        IPlayerData data = context.getData();
        IPointProvider pointProvider = data.getProgressData();
        footerWidget = addWidget(new FooterWidget(x, y + height - 20, width, 20, client.font, pointProvider));
        footerWidget.setColorSchema(0xFFFF, 0xCCCC);
        // add skill info panel
        skillInfoWidget = addWidget(new SkillInfoWidget(x, y + height - 80, width, 80, manager));

        this.loadTree();
        this.updateSkillInformationVisibility(false);
    }

    public void loadTree() {
        this.updateCanvasSource(this.navigatorWidget.getSelectedValue());
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderView(stack, mouseX, mouseY, partialTicks);
    }

    private void openPerkView() {
        manager.setView(new PerkView(width, height, manager));
    }

    private void updateCanvasSource(SkillCategory category) {
        if (!SkillTreeScreen.Cache.hasBeenBuilt())
            return;
        updateSkillInformationVisibility(false);
        Map<SkillCategory, SkillTrees> map = SkillTreeScreen.Cache.queryData();
        SkillTrees skillTrees = map.get(category);
        int xUnitSize = 6;
        int yUnitSize = 10;
        int componentSize = 22;
        int lineOff = componentSize / 2;
        IPlayerData data = manager.getContext().getData();
        IProgressData progressData = data.getProgressData();
        int level = progressData.getLevel();
        skillViewWidget.fill((filler, x, y) -> {
            Tree[] trees = skillTrees.getTrees();
            for (Tree tree : trees) {
                for (Tree.Connector connector : tree.getConnectorList()) {
                    IVec2i start = connector.getStart();
                    IVec2i end = connector.getEnd();
                    filler.add(new LineWidget(x + start.x() * xUnitSize + lineOff, y + start.y() * yUnitSize + lineOff, x + end.x() * xUnitSize + lineOff, y + end.y() * yUnitSize + lineOff));
                }
                for (Map.Entry<SkillType<?>, SkillViewData> entry : tree.getDataSet()) {
                    SkillType<?> source = entry.getKey();
                    boolean invisible = source.getProperties().getRequiredLevel() > level;
                    SkillViewData viewData = entry.getValue();
                    IVec2i pos = viewData.getPos();
                    SkillWidget widget = filler.add(new SkillWidget(x + pos.x() * xUnitSize, y + pos.y() * yUnitSize, componentSize, componentSize, source, manager.getContext(), invisible));
                    widget.setClickResponder(this::skillClicked);
                }
            }
        });
        synchronized (lock) {
            skillViewWidget.sortRenderOrder();
        }
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
            this.skillViewWidget.updateSize(width, height - (visibilityState ? 120 : 60));
        }
    }

    private static final class CategoryColorProvider implements NavigatorWidget.ITextColorProvider<SkillCategory> {

        private final SkillCategory category;
        private final Set<SkillType<?>> skills;
        private boolean hasNewSkills;
        private int animCounter;

        CategoryColorProvider(SkillCategory category) {
            this.category = category;
            this.skills = ModRegistries.SKILLS.getValues().stream()
                    .filter(skillType -> skillType.getHierarchy().getCategory().equals(category))
                    .collect(Collectors.toSet());
        }

        @Override
        public void tick(SkillCategory category) {
            this.hasNewSkills = false;
            for (SkillType<?> type : this.skills) {
                if (type.isFresh()) {
                    this.hasNewSkills = true;
                    break;
                }
            }
            if (!this.hasNewSkills) {
                this.animCounter = 0;
            } else {
                ++this.animCounter;
            }
        }

        @Override
        public int getColor(SkillCategory categoryElement, float partialTicks) {
            if (!this.hasNewSkills) {
                return 0xFFFFFF;
            }
            int timer = this.animCounter % 40;
            float triangleFn = Mth.triangleFunc(timer / 40.0F);
            float easedWave = Easings.EASE_IN_OUT_QUAD.ease(triangleFn);
            int color = (int) (0xFF * (1.0F - easedWave));
            return 0xFFFF00 | color;
        }
    }
}
