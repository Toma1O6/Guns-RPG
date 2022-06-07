package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.screen.widgets.ContainerWidget;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.quests.ActionType;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.*;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.skills.BartenderSkill;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_QuestActionPacket;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import lib.toma.animations.engine.screen.animator.widget.WidgetContainer;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.*;

import java.util.*;
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
    private static final ITextComponent TEXT_QUEST_OTHER_ACTIVE            = new TranslationTextComponent("screen.quests.has_other_active").withStyle(TextFormatting.RED);
    // parametrized localizations
    private static final String TEXT_MAYOR_STATUS_RAW = "screen.quests.mayor_status";
    private static final Function<IFormattableTextComponent, IFormattableTextComponent> TEXT_MAYOR_STATUS = (text) -> new TranslationTextComponent(TEXT_MAYOR_STATUS_RAW, text);
    private static final String TEXT_RESTOCK_TIMER_RAW = "screen.quests.restock_timer";
    private static final Function<IFormattableTextComponent, IFormattableTextComponent> TEXT_RESTOCK_TIMER = (time) -> new TranslationTextComponent(TEXT_RESTOCK_TIMER_RAW, time);
    private static final String TEXT_QUEST_TIER_RAW = "screen.quests.quest_tier";
    private static final Function<IFormattableTextComponent, IFormattableTextComponent> TEXT_QUEST_TIER = (tier) -> new TranslationTextComponent(TEXT_QUEST_TIER_RAW, tier);
    // formatting
    private static final Interval.IFormatFactory RESTOCK_TIMER_FORMAT = format -> format.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
    private static final Function<Integer, String> FORMATTER = (ticks) -> Interval.format(ticks, RESTOCK_TIMER_FORMAT);

    private final ReputationStatus status;
    private final Quest<?>[] quests;
    private final MayorEntity entity;
    private IQuests questProvider;
    private boolean hasActiveQuest;

    private QuestInfoPanelWidget infoPanelWidget;
    private Quest<?> selectedQuest;

    public QuestScreen(ReputationStatus status, Quest<?>[] quests, MayorEntity entity, long timer) {
        super(new TranslationTextComponent("screen.gunsrpg.quests"));
        this.status = status;
        this.quests = quests;
        this.entity = entity;
        this.entity.setClientTimer(timer);
    }

    @Override
    protected void init() {
        IPlayerData data = PlayerData.getUnsafe(minecraft.player);
        questProvider = data.getQuests();
        ISkillProvider skillProvider = data.getSkillProvider();
        this.hasActiveQuest = questProvider.getActiveQuest().isPresent();

        for (int i = 0; i < quests.length; i++) {
            Quest<?> quest = quests[i];
            addButton(new QuestWidget(10, 25 + i * 30, width / 3, 25, quest, this::questWidgetClicked));
        }
        questProvider.getActiveQuest().ifPresent(quest -> addButton(new QuestWidget(10, height - 30, width / 3, 25, quest, this::questWidgetClicked)));
        int panelLeft = 30 + width / 3;
        int panelWidth = width - panelLeft - 10;
        infoPanelWidget = addButton(new QuestInfoPanelWidget(panelLeft, 0, panelWidth, height, skillProvider, font));
        infoPanelWidget.setSelectedQuest(selectedQuest);
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
        IFormattableTextComponent statusComponent = TEXT_MAYOR_STATUS.apply(status.getStatusDescriptor());
        int remainingTime = (int) entity.getRemainingRestockTime();
        IFormattableTextComponent timeComponent = new TranslationTextComponent(this.getTimerColor(remainingTime) + FORMATTER.apply(remainingTime));
        IFormattableTextComponent timerComponent = TEXT_RESTOCK_TIMER.apply(timeComponent);
        font.drawShadow(matrixStack, statusComponent, infoPanelWidget.x + 5, 10, 0xFFFFFF);
        font.drawShadow(matrixStack, timerComponent, infoPanelWidget.x + infoPanelWidget.getWidth() - 5 - font.width(timerComponent), 10, 0xFFFFFF);
        if (!hasActiveQuest) {
            font.drawShadow(matrixStack, TEXT_NO_QUESTS, 10 + (width / 3f - font.width(TEXT_NO_QUESTS)) / 2.0F, height - 22, 0xff5555);
        }
        if (quests.length == 0) {
            font.drawShadow(matrixStack, TEXT_NO_QUESTS, 10 + (width / 3f - font.width(TEXT_NO_QUESTS)) / 2.0F, 20 + height * 0.2F, 0xff5555);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void questWidgetClicked(QuestWidget widget) {
        Quest<?> quest = widget.quest;
        if (quest.getScheme().isSpecialTaskQuest()) {
            return;
        }

        infoPanelWidget.setSelectedQuest(quest);
        selectedQuest = quest;
    }

    private void renderItemTooltip(MatrixStack matrix, ItemStack stack, int mouseX, int mouseY) {
        this.renderTooltip(matrix, stack, mouseX, mouseY);
    }

    private void refreshAllWidgets() {
        MainWindow window = minecraft.getWindow();
        this.init(minecraft, window.getGuiScaledWidth(), window.getGuiScaledHeight());
    }

    private TextFormatting getTimerColor(int timer) {
        Interval interval = Interval.ticks(timer);
        if (Interval.minutes(30).compareTo(interval) > 0) {
            return TextFormatting.GREEN;
        } else if (Interval.minutes(10).compareTo(interval) > 0) {
            return TextFormatting.YELLOW;
        } else {
            return TextFormatting.RED;
        }
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
            matrix.pushPose();
            matrix.translate(0, 0, 400);
            int count = stack.getCount();
            if (count > 1) {
                font.drawShadow(matrix, String.valueOf(count), x + 8, y + 8, 0xffffff);
            }
            if (isHovered) {
                tooltipRenderer.renderItemTooltip(matrix, stack, mouseX, mouseY);
            }
            matrix.popPose();
        }

        @FunctionalInterface
        private interface ITooltipRenderer {
            void renderItemTooltip(MatrixStack matrix, ItemStack stack, int mouseX, int mouseY);
        }
    }

    private final class QuestInfoPanelWidget extends ContainerWidget {

        private final FontRenderer font;
        private final ISkillProvider provider;

        private Quest<?> selectedQuest;
        private Integer[] selectedIndexes = new Integer[0];

        private ActionButton actionButton;

        public QuestInfoPanelWidget(int x, int y, int width, int height, ISkillProvider provider, FontRenderer font) {
            super(x, y, width, height);
            this.provider = provider;
            this.font = font;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            RenderUtils.drawSolid(stack.last().pose(), this.x, this.y, this.x + this.width, this.y + this.height, 0x66 << 24);
            super.renderButton(stack, mouseX, mouseY, partialTicks);
        }

        public void setSelectedQuest(Quest<?> quest) {
            clear();
            this.selectedQuest = quest == selectedQuest || shouldHideQuest(quest) ? null : quest;
            if (selectedQuest == null) return;

            QuestScheme<?> scheme = selectedQuest.getScheme();
            DisplayInfo displayInfo = scheme.getDisplayInfo();
            IQuestCondition[] conditions = selectedQuest.getConditions();
            QuestStatus status = selectedQuest.getStatus();

            int y = 0;
            int offset = 15;
            addTitle(y, TEXT_QUEST_NAME, TextFormatting.YELLOW, TextFormatting.BOLD);
            addDetail(y += offset, displayInfo.getName(), TextFormatting.ITALIC);
            if (status.shouldShowRewards()) {
                addTitle(y += offset, TEXT_QUEST_REWARDS, TextFormatting.YELLOW, TextFormatting.BOLD);
                BartenderSkill bartenderSkill = SkillUtil.getTopHierarchySkill(Skills.BARTENDER_I, provider);
                int selectionSize = bartenderSkill != null ? bartenderSkill.getRewardCount() : 1;
                addWidget(new RewardsWidget(this.x + 5, y + 45, width - 10, height - 75 - y, selectedQuest, selectionSize, this::onRewardSelectionChanged));
            } else {
                if (conditions.length > 0) {
                    addTitle(y += offset, TEXT_QUEST_CONDITIONS, TextFormatting.YELLOW, TextFormatting.BOLD);
                    for (IQuestCondition condition : conditions) {
                        addDetail(y += offset, condition.getDescriptor());
                    }
                }
                StringTextComponent tierComponent = new StringTextComponent(TextFormatting.AQUA.toString() + selectedQuest.getRewardTier());
                addDetail(y += offset, TEXT_QUEST_TIER.apply(tierComponent), 5);
                addTitle(y += offset, TEXT_QUEST_DETAIL, TextFormatting.YELLOW, TextFormatting.BOLD);
                List<IReorderingProcessor> list = font.split(displayInfo.getInfo().withStyle(TextFormatting.ITALIC), width - 30);
                for (IReorderingProcessor processor : list) {
                    y += offset;
                    addWidget(new ReorderedTextWidget(this.x + 15, this.y + 30 + y, this.width - 30, 10, processor, font));
                }
                if (selectedQuest instanceof IAdditionalClientInfo) {
                    y += offset;
                    IAdditionalClientInfo clientInfo = (IAdditionalClientInfo) selectedQuest;
                    ITextComponent[] text = clientInfo.getAdditionalNotes();
                    for (int i = 0; i < text.length; i++) {
                        ITextComponent textComponent = text[i];
                        addWidget(new LabelWidget(this.x + 5, this.y + 30 + y + i * offset, width - 10, 10, textComponent, font));
                    }
                }
            }


            ActionType actionType = this.getQuestActionType(status);
            if (actionType != null) {
                UUID traderId = QuestScreen.this.entity.getUUID();
                UUID questId = this.selectedQuest.getOriginalAssignerId();
                if (Objects.equals(traderId, questId) || questId.equals(Util.NIL_UUID) || actionType == ActionType.CANCEL) {
                    actionButton = addWidget(new ActionButton(this.x + this.width - 145, this.height - 25, 140, 20, actionType, this::handleResponse));
                    IQuests quests = QuestScreen.this.questProvider;
                    Optional<Quest<?>> activeQuestOpt = quests.getActiveQuest();
                    if (actionType == ActionType.COLLECT || (actionType == ActionType.ASSIGN && activeQuestOpt.isPresent() && activeQuestOpt.get() != selectedQuest)) {
                        actionButton.active = false;
                    }
                } else {
                    int textWidth = font.width(TEXT_QUEST_OTHER_ACTIVE);
                    addWidget(new LabelWidget(this.x + this.width - 5 - textWidth, this.height - 15, textWidth, 10, TEXT_QUEST_OTHER_ACTIVE, font));
                }
            }
        }

        private boolean shouldHideQuest(Quest<?> quest) {
            if (quest == null) return true;
            QuestStatus status = quest.getStatus();
            return status == QuestStatus.FAILED || status == QuestStatus.CLAIMED;
        }

        private void onRewardSelectionChanged(Integer[] choices) {
            actionButton.active = choices.length > 0;
            this.selectedIndexes = choices;
        }

        private void handleResponse(ActionType type) {
            C2S_QuestActionPacket packet = null;
            MayorEntity entity = QuestScreen.this.entity;
            switch (type) {
                case ASSIGN:
                    packet = C2S_QuestActionPacket.makeAssignPacket(entity, QuestScreen.this.quests, selectedQuest);
                    break;
                case CANCEL:
                    packet = C2S_QuestActionPacket.makeCancelPacket(entity);
                    break;
                case COLLECT:
                    packet = C2S_QuestActionPacket.makeCollectionPacket(entity, selectedIndexes);
                    break;
            }
            NetworkManager.sendServerPacket(packet);
            QuestScreen.this.refreshAllWidgets();
        }

        private void addTitle(int y, IFormattableTextComponent component, TextFormatting... formattings) {
            addWidget(new LabelWidget(this.x + 5, this.y + 30 + y, this.width - 10, 10, component.withStyle(formattings), font));
        }

        private void addDetail(int y, ITextComponent component) {
            addDetail(y, component, 15);
        }

        private void addDetail(int y, ITextComponent text, int xOffset) {
            addWidget(new LabelWidget(this.x + xOffset, this.y + 30 + y, this.width - xOffset, 10, text, font));
        }

        private void addDetail(int y, IFormattableTextComponent component, TextFormatting... formattings) {
            addDetail(y, component, 15, formattings);
        }

        private void addDetail(int y, IFormattableTextComponent text, int xOffset, TextFormatting... formattings) {
            addWidget(new LabelWidget(this.x + xOffset, this.y + 30 + y, this.width - xOffset, 10, text.withStyle(formattings), font));
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
            super(x, y, width, height, type.getText());
            this.actionType = type;
            this.consumer = consumer;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            RenderUtils.drawBorder(matrix.last().pose(), x, y, width, height, 1.25F, !active ? 0xFFAA0000 : isHovered ? 0xFF00FF22 : 0xFF00AA00);
            FontRenderer font = Minecraft.getInstance().font;
            ITextComponent text = this.getMessage();
            font.drawShadow(matrix, text, x + (width - font.width(text)) / 2.0f, y + (height - font.lineHeight) / 2.0f, active ? 0xffffff : 0xaa3333);
        }

        @Override
        public void onClick(double p_230982_1_, double p_230982_3_) {
            consumer.accept(actionType);
        }
    }

    private final class RewardsWidget extends WidgetContainer {

        private final int selectionSize;
        private final Consumer<Integer[]> selectionIndexConsumer;
        private final Set<ChoiceWidget> choices = new HashSet<>();

        public RewardsWidget(int x, int y, int width, int height, Quest<?> quest, int selectionSize, Consumer<Integer[]> selectionIndexConsumer) {
            super(x, y, width, height);
            this.selectionSize = selectionSize;
            this.selectionIndexConsumer = selectionIndexConsumer;

            int oneThird = width / 3;
            QuestReward reward = quest.getReward();
            QuestReward.Choice[] choices = reward.getChoices();
            int index = 0;
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.font;
            ItemRenderer itemRenderer = mc.getItemRenderer();
            for (int i = 0; i < choices.length; i++) {
                addWidget(new ChoiceWidget(5, index * 30, oneThird, 25, index, font, this::trySelectChoice));
                ++index;
            }
            index = 0;
            // render order nonsense
            for (QuestReward.Choice choice : choices) {
                ItemStack[] itemStacks = choice.getContents();
                for (int i = 0; i < itemStacks.length; i++) {
                    ItemStack stack = itemStacks[i];
                    addWidget(new ItemStackWidget(10 + i * 25, 4 + index * 30, 16, 16, stack, font, itemRenderer, QuestScreen.this::renderItemTooltip));
                }
                ++index;
            }
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            super.renderButton(stack, mouseX, mouseY, partialTicks);
            FontRenderer font = Minecraft.getInstance().font;
            String text = choices.size() + "/" + selectionSize;
            font.drawShadow(stack, text, this.x + this.width - font.width(text), this.y + this.height - font.lineHeight, 0xFFFFFF);
        }

        private void trySelectChoice(ChoiceWidget widget) {
            if (choices.contains(widget)) {
                choices.remove(widget);
                widget.setSelected(false);
            } else if (choices.size() < selectionSize) {
                choices.add(widget);
                widget.setSelected(true);
            }
            Integer[] selectedIndexes = choices.stream().map(choiceWidget -> choiceWidget.choiceIndex).toArray(Integer[]::new);
            this.selectionIndexConsumer.accept(selectedIndexes);
        }
    }

    private static final class ChoiceWidget extends Widget {

        private final int choiceIndex;
        private final FontRenderer font;
        private final Consumer<ChoiceWidget> clickResponder;
        private boolean selected;

        public ChoiceWidget(int x, int y, int width, int height, int choiceIndex, FontRenderer font, Consumer<ChoiceWidget> clickResponder) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.choiceIndex = choiceIndex;
            this.font = font;
            this.clickResponder = clickResponder;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            boolean selected = this.selected || isHovered;
            float thickness = selected ? 2.0F : 1.0F;
            int color = selected ? 0xFFFFFF00 : 0xFFFFFFFF;
            RenderUtils.drawBorder(stack.last().pose(), x, y, width, height, thickness, color);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            clickResponder.accept(this);
        }

        public void setSelected(boolean state) {
            this.selected = state;
        }
    }

    static {
        TEXT_AVAILABLE_QUESTS.withStyle(TextFormatting.BOLD, TextFormatting.AQUA);
        TEXT_ACTIVE_QUESTS.withStyle(TextFormatting.BOLD, TextFormatting.GREEN);
    }
}
