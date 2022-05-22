package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.client.screen.widgets.ContainerWidget;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.DisplayInfo;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestScheme;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_QuestActionPacket;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.RenderUtils;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuestScreen extends Screen {

    // Simple localizations
    private static final IFormattableTextComponent TEXT_AVAILABLE_QUESTS   = new TranslationTextComponent("screen.quests.available_quests");   // title for available quests
    private static final IFormattableTextComponent TEXT_ACTIVE_QUESTS      = new TranslationTextComponent("screen.quests.active_quests");      // title for active quests
    private static final IFormattableTextComponent TEXT_NO_QUESTS          = new TranslationTextComponent("screen.quests.no_quests");          // displayed when no quests are available for specific context
    private static final IFormattableTextComponent TEXT_QUEST_NAME         = new TranslationTextComponent("screen.quests.quest_name");         // name of quest
    private static final IFormattableTextComponent TEXT_QUEST_DETAIL       = new TranslationTextComponent("screen.quests.quest_detail");       // description/detail of quest
    private static final IFormattableTextComponent TEXT_QUEST_CONDITIONS   = new TranslationTextComponent("screen.quests.conditions");         // title for condition overview
    private static final IFormattableTextComponent TEXT_QUEST_REWARDS      = new TranslationTextComponent("screen.quests.rewards");            // title for reward overview
    private static final IFormattableTextComponent BUTTON_TAKE_QUEST       = new TranslationTextComponent("screen.quests.choose_quest");       // Text to be displayed on button
    private static final IFormattableTextComponent BUTTON_CANCEL_QUEST     = new TranslationTextComponent("screen.quests.cancel_quest");       // text for button which fails active quest
    private static final IFormattableTextComponent BUTTON_CLAIM_REWARDS    = new TranslationTextComponent("screen.quests.claim_rewards");      // text for button which claims selected rewards
    // parametrized localizations
    private static final String TEXT_MAYOR_STATUS_RAW = "screen.quests.mayor_status";
    private static final Function<ReputationStatus, IFormattableTextComponent> TEXT_MAYOR_STATUS = (status) -> new TranslationTextComponent(TEXT_MAYOR_STATUS_RAW, status.getStatusDescriptor().getString());
    private static final String TEXT_RESTOCK_TIMER_RAW = "screen.quests.restock_timer";
    private static final Function<String, IFormattableTextComponent> TEXT_RESTOCK_TIMER = (time) -> new TranslationTextComponent(TEXT_RESTOCK_TIMER_RAW, time);
    private static final String TEXT_QUEST_TIER_RAW = "screen.quests.quest_tier";
    private static final Function<IFormattableTextComponent, IFormattableTextComponent> TEXT_QUEST_TIER = (tier) -> new TranslationTextComponent(TEXT_QUEST_TIER_RAW, tier);
    // formatting
    private static final Interval.IFormatFactory RESTOCK_TIMER_FORMAT = format -> format.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND);
    private static final Function<Integer, String> FORMATTER = (ticks) -> Interval.format(ticks, RESTOCK_TIMER_FORMAT);

    private final ReputationStatus status;
    private final Quest<?>[] quests;
    private final MayorEntity entity;
    private IQuests questProvider;
    private boolean hasActiveQuest;

    private QuestInfoPanelWidget infoPanelWidget;

    public QuestScreen(ReputationStatus status, Quest<?>[] quests, MayorEntity entity) {
        super(new TranslationTextComponent("screen.gunsrpg.quests"));
        this.status = status;
        this.quests = quests;
        this.entity = entity;
    }

    @Override
    protected void init() {
        this.questProvider = PlayerData.getUnsafe(minecraft.player).getQuests();
        this.hasActiveQuest = questProvider.getActiveQuest().isPresent();

        for (int i = 0; i < quests.length; i++) {
            Quest<?> quest = quests[i];
            addButton(new QuestWidget(10, 25 + i * 30, width / 3, 25, quest, this::questWidgetClicked));
        }
        this.questProvider.getActiveQuest().ifPresent(quest -> addButton(new QuestWidget(10, height - 30, width / 3, 25, quest, this::questWidgetClicked)));
        int panelLeft = 30 + width / 3;
        int panelWidth = width - panelLeft - 10;
        infoPanelWidget = addButton(new QuestInfoPanelWidget(panelLeft, 0, panelWidth, height, questProvider, font, this::renderItemTooltip));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        Matrix4f pose = matrixStack.last().pose();
        RenderUtils.drawSolid(pose, 5, 0, width / 3 + 15, height, 0x66 << 24);
        // widgets
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        // labels
        font.drawShadow(matrixStack, TEXT_AVAILABLE_QUESTS, 10, 10, 0xFFFFFF);
        font.drawShadow(matrixStack, TEXT_ACTIVE_QUESTS, 10, height - 45, 0xFFFFFF);
        ITextComponent statusComponent = TEXT_MAYOR_STATUS.apply(status);
        int remainingTime = (int) entity.getRemainingRestockTime();
        ITextComponent timerComponent = TEXT_RESTOCK_TIMER.apply(FORMATTER.apply(remainingTime));
        font.drawShadow(matrixStack, statusComponent, infoPanelWidget.x + 5, 10, 0xFFFFFF);
        font.drawShadow(matrixStack, timerComponent, infoPanelWidget.x + infoPanelWidget.getWidth() - 5 - font.width(timerComponent), 10, 0xFFFFFF);
        if (!hasActiveQuest) {
            font.drawShadow(matrixStack, TEXT_NO_QUESTS, 10 + (width / 3f - font.width(TEXT_NO_QUESTS)) / 2.0F, height - 22, 0xff5555);
        }
        if (quests.length == 0) {
            font.drawShadow(matrixStack, TEXT_NO_QUESTS, 10 + (width / 3f - font.width(TEXT_NO_QUESTS)) / 2.0F, 20 + height * 0.2F, 0xff5555);
        }
    }

    private void questWidgetClicked(QuestWidget widget) {
        infoPanelWidget.setSelectedQuest(widget.quest);
    }

    private void renderItemTooltip(MatrixStack matrix, ItemStack stack, int mouseX, int mouseY) {
        this.renderTooltip(matrix, stack, mouseX, mouseY);
    }

    // use for rendering clickable quest widget
    private static final class QuestWidget extends Widget {

        private final Quest<?> quest;
        private final Consumer<QuestWidget> onClick;

        public QuestWidget(int x, int y, int width, int height, Quest<?> quest, Consumer<QuestWidget> onClick) {
            super(x, y, width, height, quest.getScheme().getDisplayInfo().getName());
            this.quest = quest;
            this.onClick = onClick;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            Matrix4f pose = matrix.last().pose();
            RenderUtils.drawBorder(pose, x, y, width, height, 1.5F, isHovered ? 0xFFFFFF00 : 0xFFFFFFFF);
            Minecraft.getInstance().font.drawShadow(matrix, this.getMessage(), x + width * 0.05F, y + (height - 7) / 2.0F, 0xFFFFFF);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            onClick.accept(this);
        }
    }

    // use for rendering rewards and then for hovered item details
    private static final class ItemStackWidget extends Widget {

        private final ItemStack stack;
        private final FontRenderer font;
        private final ItemRenderer renderer;
        private final ITooltipRenderer tooltipRenderer;

        public ItemStackWidget(int x, int y, int width, int height, ItemStack stack, FontRenderer font, ItemRenderer renderer, ITooltipRenderer tooltipRenderer) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.stack = stack;
            this.font = font;
            this.renderer = renderer;
            this.tooltipRenderer = tooltipRenderer;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            renderer.renderGuiItem(stack, x, y);
            font.draw(matrix, String.valueOf(stack.getCount()), x + 16, y + 12, 0xffffff);
            tooltipRenderer.renderItemTooltip(matrix, stack, mouseX, mouseY);
        }

        @FunctionalInterface
        private interface ITooltipRenderer {
            void renderItemTooltip(MatrixStack matrix, ItemStack stack, int mouseX, int mouseY);
        }
    }

    private final class QuestInfoPanelWidget extends ContainerWidget {

        private final FontRenderer font;
        private final IQuests provider;
        private final ItemStackWidget.ITooltipRenderer tooltipRenderer;

        private Quest<?> selectedQuest;

        private ActionButton actionButton;

        public QuestInfoPanelWidget(int x, int y, int width, int height, IQuests provider, FontRenderer font, ItemStackWidget.ITooltipRenderer tooltipRenderer) {
            super(x, y, width, height);
            this.provider = provider;
            this.font = font;
            this.tooltipRenderer = tooltipRenderer;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            RenderUtils.drawSolid(stack.last().pose(), this.x, this.y, this.x + this.width, this.y + this.height, 0x66 << 24);
            super.renderButton(stack, mouseX, mouseY, partialTicks);
        }

        public void setSelectedQuest(Quest<?> quest) {
            clear();
            this.selectedQuest = quest == selectedQuest ? null : quest;
            if (selectedQuest == null) return;

            QuestScheme<?> scheme = selectedQuest.getScheme();
            DisplayInfo displayInfo = scheme.getDisplayInfo();
            IQuestCondition[] conditions = selectedQuest.getConditions();
            QuestStatus status = selectedQuest.getStatus();

            int y = 0;
            int offset = 15;
            addTitle(y, TEXT_QUEST_NAME);
            addDetail(y += offset, displayInfo.getName());
            if (status.shouldShowRewards()) {
                if (conditions.length > 0) {
                    addTitle(y += offset, TEXT_QUEST_CONDITIONS);
                    for (IQuestCondition condition : conditions) {
                        addDetail(y += offset, condition.getDescriptor());
                    }
                }
                StringTextComponent tierComponent = new StringTextComponent(TextFormatting.AQUA.toString() + selectedQuest.getRewardTier());
                addDetail(y += offset, TEXT_QUEST_TIER.apply(tierComponent), 5);
                addTitle(y += offset, TEXT_QUEST_DETAIL);
                List<IReorderingProcessor> list = font.split(displayInfo.getInfo(), width - 30);
                for (IReorderingProcessor processor : list) {
                    y += offset;
                    addWidget(new ReorderedTextWidget(this.x + 15, this.y + 30 + y, this.width - 30, 10, processor, font));
                }
            } else {
                addTitle(y += offset, TEXT_QUEST_REWARDS);
                // add reward widget
            }

            ActionType actionType = this.getQuestActionType(status);
            if (actionType != null) {
                actionButton = addWidget(new ActionButton(this.x + this.width - 145, this.height - 25, 140, 20, actionType, this::handleResponse));
            }
        }

        private void onRewardSelectionChanged(QuestReward.Choice[] choices) {
            actionButton.active = choices.length > 0;
        }

        private void handleResponse(ActionType type) {
            C2S_QuestActionPacket packet = null;
            switch (type) {
                case ASSIGN:
                    packet = C2S_QuestActionPacket.makeAssignPacket(QuestScreen.this.entity, QuestScreen.this.quests, selectedQuest);
                    break;
                case CANCEL:
                    packet = C2S_QuestActionPacket.makeCancelPacket();
                    break;
                case COLLECT:
                    packet = C2S_QuestActionPacket.makeCollectionPacket(new int[0]); // TODO actual data
                    break;
            }
            NetworkManager.sendServerPacket(packet);
        }

        private void addTitle(int y, IFormattableTextComponent component) {
            addWidget(new LabelWidget(this.x + 5, this.y + 30 + y, this.width - 10, 10, component.withStyle(TextFormatting.BOLD), font));
        }

        private void addDetail(int y, ITextComponent component) {
            addDetail(y, component, 15);
        }

        private void addDetail(int y, ITextComponent text, int xOffset) {
            addWidget(new LabelWidget(this.x + xOffset, this.y + 30 + y, this.width - xOffset, 10, text, font));
        }

        private ActionType getQuestActionType(QuestStatus status) {
            switch (status) {
                case ACTIVE:
                    return ActionType.CANCEL;
                case COMPLETED:
                    return ActionType.COLLECT;
                case CREATED:
                    return ActionType.ASSIGN;
                default:
                    return null;
            }
        }
    }

    private static final class ReorderedTextWidget extends Widget {

        private final IReorderingProcessor processor;
        private final FontRenderer font;

        private ReorderedTextWidget(int x, int y, int width, int height, IReorderingProcessor processor, FontRenderer font) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.processor = processor;
            this.font = font;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            font.drawShadow(matrix, processor, x, y, 0xFFFFFF);
        }
    }

    private static final class ActionButton extends Widget {

        private final ActionType actionType;
        private final Consumer<ActionType> consumer;

        private ActionButton(int x, int y, int width, int height, ActionType type, Consumer<ActionType> consumer) {
            super(x, y, width, height, type.text);
            this.actionType = type;
            this.consumer = consumer;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            RenderUtils.drawBorder(matrix.last().pose(), x, y, width, height, 1.25F, isHovered ? 0xFFFFFF00 : 0xFFFFFFFF);
            FontRenderer font = Minecraft.getInstance().font;
            ITextComponent text = this.getMessage();
            font.drawShadow(matrix, text, x + (width - font.width(text)), y + (height - font.lineHeight) / 2.0f, 0xffffff);
        }

        @Override
        public void onClick(double p_230982_1_, double p_230982_3_) {
            consumer.accept(actionType);
        }
    }

    public enum ActionType {

        ASSIGN(BUTTON_TAKE_QUEST),
        CANCEL(BUTTON_CANCEL_QUEST),
        COLLECT(BUTTON_CLAIM_REWARDS);

        private final ITextComponent text;

        ActionType(ITextComponent text) {
            this.text = text;
        }
    }

    static {
        TEXT_AVAILABLE_QUESTS.withStyle(TextFormatting.BOLD);
        TEXT_ACTIVE_QUESTS.withStyle(TextFormatting.BOLD);
    }
}
