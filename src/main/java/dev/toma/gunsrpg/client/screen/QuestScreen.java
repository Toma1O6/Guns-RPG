package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.mayor.TraderStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Function;

public class QuestScreen extends Screen {

    // Simple localizations
    private static final ITextComponent TEXT_AVAILABLE_QUESTS   = new TranslationTextComponent("screen.quests.available_quests");   // title for available quests
    private static final ITextComponent TEXT_ACTIVE_QUESTS      = new TranslationTextComponent("screen.quests.active_quests");      // title for active quests
    private static final ITextComponent TEXT_NO_QUESTS          = new TranslationTextComponent("screen.quests.no_quests");          // displayed when no quests are available for specific context
    private static final ITextComponent TEXT_QUEST_NAME         = new TranslationTextComponent("screen.quests.quest_name");         // name of quest
    private static final ITextComponent TEXT_QUEST_DETAIL       = new TranslationTextComponent("screen.quests.quest_detail");       // description/detail of quest
    private static final ITextComponent TEXT_QUEST_CONDITIONS   = new TranslationTextComponent("screen.quests.conditions");         // title for condition overview
    private static final ITextComponent TEXT_QUEST_REWARDS      = new TranslationTextComponent("screen.quests.rewards");            // title for reward overview
    private static final ITextComponent BUTTON_TAKE_QUEST       = new TranslationTextComponent("screen.quests.choose_quest");       // Text to be displayed on button
    private static final ITextComponent BUTTON_STOP_QUEST       = new TranslationTextComponent("screen.quests.stop_quest");         // text for button which fails active quest
    private static final ITextComponent BUTTON_CLAIM_REWARDS    = new TranslationTextComponent("screen.quests.claim_rewards");      // text for button which claims selected rewards
    // parametrized localizations
    private static final String TEXT_MAYOR_STATUS_RAW = "screen.quests.mayor_status";
    private static final Function<ReputationStatus, ITextComponent> TEXT_MAYOR_STATUS = (status) -> new TranslationTextComponent(TEXT_MAYOR_STATUS_RAW, status.getStatusDescriptor().getString());
    private static final String TEXT_RESTOCK_TIMER_RAW = "screen.quests.restock_timer";
    private static final Function<String, ITextComponent> TEXT_RESTOCK_TIMER = (time) -> new TranslationTextComponent(TEXT_RESTOCK_TIMER_RAW, time);
    private static final String TEXT_QUEST_TIER_RAW = "screen.quests.quest_tier";
    private static final Function<Integer, ITextComponent> TEXT_QUEST_TIER = (tier) -> new TranslationTextComponent(TEXT_QUEST_TIER_RAW, tier);
    // formatting
    private static final Interval.IFormatFactory RESTOCK_TIMER_FORMAT = format -> format.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND);
    private static final Function<Integer, String> FORMATTER = (ticks) -> Interval.format(ticks, RESTOCK_TIMER_FORMAT);

    private final ReputationStatus status;
    private final Quest<?>[] quests;
    private final MayorEntity entity;

    public QuestScreen(ReputationStatus status, Quest<?>[] quests, MayorEntity entity) {
        super(new TranslationTextComponent("screen.gunsrpg.quests"));
        this.status = status;
        this.quests = quests;
        this.entity = entity;
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
    }

    private void questWidgetClicked(QuestWidget widget) {

    }

    // use for rendering clickable quest widget
    private static final class QuestWidget extends Widget {

        public QuestWidget(int x, int y, int width, int height, ITextComponent title) {
            super(x, y, width, height, title);
        }
    }

    // use for rendering rewards and then for hovered item details
    private static final class ItemStackWidget extends Widget {

        public ItemStackWidget(int x, int y, int width, int height) {
            super(x, y, width, height, StringTextComponent.EMPTY);
        }
    }
}
