package dev.toma.gunsrpg.client.screen.quest;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.ScreenDataEventListener;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.client.screen.DialogScreen;
import dev.toma.gunsrpg.client.screen.animation.FadeAnimation;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.skills.BartenderSkill;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_QuestCancelRequest;
import dev.toma.gunsrpg.network.packet.C2S_QuestClaimRequest;
import dev.toma.gunsrpg.network.packet.C2S_QuestCompleteRequest;
import dev.toma.gunsrpg.network.packet.C2S_QuestStartRequest;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class QuestScreen extends Screen implements ScreenDataEventListener {

    public static final ITextComponent TITLE = new TranslationTextComponent("screen.gunsrpg.quests");
    private static final ITextComponent TEXT_AVAILABLE_QUESTS = new TranslationTextComponent("screen.quests.available_quests").withStyle(TextFormatting.UNDERLINE);
    private static final ITextComponent TEXT_ACTIVE_QUESTS = new TranslationTextComponent("screen.quests.active_quests").withStyle(TextFormatting.UNDERLINE);
    private static final ITextComponent START_QUEST = new TranslationTextComponent("screen.quests.start_quest").withStyle(TextFormatting.GREEN);
    private static final ITextComponent CANCEL_QUEST = new TranslationTextComponent("screen.quests.cancel_quest").withStyle(TextFormatting.RED);
    private static final ITextComponent COMPLETE_QUEST = new TranslationTextComponent("screen.quests.complete_quest").withStyle(TextFormatting.GREEN);
    private static final ITextComponent CLAIM_REWARDS = new TranslationTextComponent("screen.quests.claim_rewards").withStyle(TextFormatting.GREEN);
    private static final ITextComponent DIALOG_START_HEADER = new TranslationTextComponent("screen.dialog.quest.start.header").withStyle(TextFormatting.BOLD);
    private static final ITextComponent DIALOG_START_INFO = new TranslationTextComponent("screen.dialog.quest.start.info");
    private static final ITextComponent DIALOG_START_DANGER = new TranslationTextComponent("screen.dialog.quest.start.danger").withStyle(TextFormatting.ITALIC, TextFormatting.RED);
    private static final ITextComponent DIALOG_CANCEL_HEADER = new TranslationTextComponent("screen.dialog.quest.cancel.header").withStyle(TextFormatting.BOLD);
    private static final ITextComponent DIALOG_CANCEL_INFO = new TranslationTextComponent("screen.dialog.quest.cancel.info");
    private static final ITextComponent DIALOG_CANCEL_DANGER = new TranslationTextComponent("screen.dialog.quest.cancel.danger").withStyle(TextFormatting.ITALIC, TextFormatting.RED);
    private static final ITextComponent DIALOG_CLAIM_REWARD_HEADER = new TranslationTextComponent("screen.dialog.quest.claim.header").withStyle(TextFormatting.BOLD);
    private static final ITextComponent DIALOG_CLAIM_REWARD_INFO = new TranslationTextComponent("screen.dialog.quest.claim.info");
    private static final ITextComponent DIALOG_CLAIM_REWARD_WARNING = new TranslationTextComponent("screen.dialog.quest.claim.warning").withStyle(TextFormatting.ITALIC, TextFormatting.YELLOW);

    private final MayorEntity entity;
    private final ReputationStatus status;
    private final List<Quest<?>> quests;
    private final QuestReward pendingReward;
    private final IntSet rewardSelection = new IntArraySet();

    private IQuestingData questData;
    private Quest<?> activeQuest;
    private Quest<?> selectedQuest;
    private int selectedQuestIndex;
    private int sidebarWidth;
    private int rewardSelectionCount;
    private int rewardChoiceCount;
    private int rewardsTop;

    public QuestScreen(MayorEntity entity, ReputationStatus status, List<Quest<?>> quests, QuestReward pendingReward) {
        super(TITLE);
        this.entity = entity;
        this.status = status;
        this.quests = quests;
        this.pendingReward = pendingReward;
    }

    @Override
    protected void init() {
        this.questData = QuestingDataProvider.getQuesting(this.minecraft.level);
        QuestingGroup group = this.questData.getOrCreateGroup(this.minecraft.player);
        this.activeQuest = this.questData.getActiveQuest(group);
        this.rewardSelectionCount = BartenderSkill.getRewardClaimSize(this.minecraft.player);
        if (activeQuest != null && this.selectedQuest == null) {
            this.selectedQuest = this.activeQuest;
        }

        int buttonWidth = 100;
        int buttonHeight = 20;
        this.sidebarWidth = Math.min((this.width / 5) * 2, 180);
        if (this.pendingReward != null) {
            // reward widgets
            QuestReward.Choice[] choices = this.pendingReward.getChoices();
            this.rewardChoiceCount = choices.length;
            int left = (this.width - buttonWidth) / 2;
            int rewardsHeight = this.rewardChoiceCount * (buttonHeight + 5) + 15 + buttonHeight + 5;
            this.rewardsTop = (this.height - rewardsHeight) / 2;
            for (int i = 0; i < this.rewardChoiceCount; i++) {
                final int choiceIndex = i;
                QuestReward.Choice choice = choices[choiceIndex];
                RewardChoiceWidget widget = this.addButton(new RewardChoiceWidget(left, this.rewardsTop + 15 + i * (buttonHeight + 5), buttonWidth, buttonHeight, choice, (selected, manager) -> this.rewardChoiceChanged(selected, manager, choiceIndex)));
                boolean isSelected = this.rewardSelection.contains(choiceIndex);
                widget.setSelected(isSelected);
                widget.setFadeOutAnim(new FadeAnimation(250L));
                widget.setTooltipRenderer(this::renderTooltip);
            }
            int claimButtonY = this.rewardsTop + rewardsHeight - buttonHeight;
            SolidColorButton claimButton = this.addButton(new SolidColorButton(left, claimButtonY, buttonWidth, buttonHeight, CLAIM_REWARDS, this::claimSelectedRewards));
            claimButton.setFadeOutAnim(FadeAnimation.createDefault());
        } else {
            // header setup
            int left = this.sidebarWidth + 10;
            // trader stat header
            this.addButton(new MayorInfoHeader(left, 0, this.width - left - 10, 20, this.entity, this.status));
            // quest sidebar
            List<Quest<?>> quests = this.getQuests();
            for (int i = 0; i < quests.size(); i++) {
                final int index = i;
                Quest<?> quest = quests.get(i);
                int width = this.sidebarWidth - 10;
                int questButtonHeight = 36;
                QuestButtonWidget widget = this.addButton(new QuestButtonWidget(5, 20 + i * questButtonHeight, width, questButtonHeight, this.font, quest, () -> this.selectQuest(quest, index)));
                widget.setHighlighted(this.selectedQuest == quest);
                widget.setFadeOutAnim(new FadeAnimation(250L));
            }
            // Quest details
            if (this.selectedQuest != null) {
                QuestDetailsWidget widget = this.addButton(new QuestDetailsWidget(left, 20, this.width - left - 10, this.height - 50, this.selectedQuest, this.entity));
                widget.setTextMargin(0);
                widget.setExtendedInformation(this.isViewingActiveQuest());
            }
            if (this.selectedQuest != null) {
                UUID clientId = this.minecraft.player.getUUID();
                if (this.isViewingActiveQuest()) {
                    if (this.selectedQuest.isOwner(clientId)) {
                        if (this.selectedQuest.isManageableByMayor(this.entity)) {
                            QuestStatus questStatus = this.selectedQuest.getStatus();
                            switch (questStatus) {
                                case ACTIVE:
                                    // cancel quest button
                                    SolidColorButton cancelButton = this.addButton(new SolidColorButton(this.width - 10 - buttonWidth, this.height - buttonHeight - 10, buttonWidth, buttonHeight, CANCEL_QUEST, this::cancelSelectedQuest));
                                    cancelButton.setFadeOutAnim(FadeAnimation.createDefault());
                                    break;
                                case COMPLETED:
                                    // complete quest button
                                    SolidColorButton completeButton = this.addButton(new SolidColorButton(this.width - 10 - buttonWidth, this.height - buttonHeight - 10, buttonWidth, buttonHeight, COMPLETE_QUEST, this::completeSelectedQuest));
                                    completeButton.setFadeOutAnim(FadeAnimation.createDefault());
                                    break;
                            }
                        }
                    }
                } else if (group.isLeader(clientId)) {
                    // start quest button
                    SolidColorButton button = this.addButton(new SolidColorButton(this.width - 10 - buttonWidth, this.height - buttonHeight - 10, buttonWidth, buttonHeight, START_QUEST, this::startSelectedQuest));
                    button.setFadeOutAnim(FadeAnimation.createDefault());
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, delta);

        if (this.pendingReward == null) {
            // Quest sidebar label
            ITextComponent text = this.activeQuest != null ? TEXT_ACTIVE_QUESTS : TEXT_AVAILABLE_QUESTS;
            this.font.drawShadow(matrix, text, 5, 5, 0xFFFFFF);
        } else {
            // Reward title
            ITextComponent text = new TranslationTextComponent("screen.quests.reward_claim", this.rewardSelection.size(), this.rewardSelectionCount).withStyle(TextFormatting.UNDERLINE);
            this.font.drawShadow(matrix, text, (this.width - this.font.width(text)) / 2.0F, this.rewardsTop, 0xFFFFFF);
        }
    }

    @Override
    public void onQuestingDataReceived(IQuestingData questingData) {
        this.init(this.minecraft, this.width, this.height);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private List<Quest<?>> getQuests() {
        if (this.activeQuest != null) {
            return Collections.singletonList(this.activeQuest);
        }
        return this.quests;
    }

    private boolean isViewingActiveQuest() {
        return this.activeQuest != null && this.selectedQuest == this.activeQuest;
    }

    private void selectQuest(Quest<?> quest, int index) {
        this.selectedQuest = this.selectedQuest == quest ? null : quest;
        this.selectedQuestIndex = index;
        this.init(this.minecraft, this.width, this.height);
    }

    private void rewardChoiceChanged(boolean selected, BooleanConsumer stateManager, int index) {
        if (selected) {
            if (this.rewardSelection.size() < this.rewardSelectionCount) {
                this.rewardSelection.add(index);
                stateManager.accept(true);
            }
        } else {
            this.rewardSelection.remove(index);
            stateManager.accept(false);
        }
    }

    private void startSelectedQuest() {
        DialogScreen dialog = DialogScreen.create(this, DIALOG_START_HEADER);
        dialog.setContent(DIALOG_START_INFO, new StringTextComponent(" "), DIALOG_START_DANGER);
        dialog.setConfirmHandler(this::startSelectedQuestConfirmed);
        dialog.setOpenParentOnConfirm(false);
        dialog.setActive(this.minecraft);
    }

    private void cancelSelectedQuest() {
        DialogScreen dialog = DialogScreen.create(this, DIALOG_CANCEL_HEADER);
        dialog.setContent(DIALOG_CANCEL_INFO, new StringTextComponent(" "), DIALOG_CANCEL_DANGER);
        dialog.setConfirmHandler(this::cancelSelectedQuestConfirmed);
        dialog.setActive(this.minecraft);
    }

    private void completeSelectedQuest() {
        NetworkManager.sendServerPacket(new C2S_QuestCompleteRequest(this.entity.getId()));
    }

    private void startSelectedQuestConfirmed() {
        NetworkManager.sendServerPacket(new C2S_QuestStartRequest(this.entity.getId(), this.selectedQuestIndex));
    }

    private void cancelSelectedQuestConfirmed() {
        NetworkManager.sendServerPacket(new C2S_QuestCancelRequest(this.entity.getId()));
    }

    private void claimSelectedRewards() {
        DialogScreen dialogScreen = DialogScreen.create(this, DIALOG_CLAIM_REWARD_HEADER);
        List<ITextComponent> content = new ArrayList<>();
        content.add(DIALOG_CLAIM_REWARD_INFO);

        QuestReward.Choice[] choices = this.pendingReward.getChoices();
        for (int index : this.rewardSelection) {
            QuestReward.Choice choice = choices[index];
            QuestReward.Choice.DisplayData data = choice.getDisplayInfo();
            content.add(new StringTextComponent("- ").append(data.getText()));
        }

        if (this.rewardSelection.size() < this.rewardSelectionCount) {
            content.add(new StringTextComponent(" "));
            content.add(DIALOG_CLAIM_REWARD_WARNING);
        }

        dialogScreen.setContent(content);
        dialogScreen.setConfirmHandler(this::claimSelectedRewardsConfirmed);
        dialogScreen.setActive(this.minecraft);
    }

    private void claimSelectedRewardsConfirmed() {
        List<Integer> indices = new ArrayList<>(this.rewardSelection);
        NetworkManager.sendServerPacket(new C2S_QuestClaimRequest(this.entity.getId(), indices));
        this.rewardSelection.clear();
    }
}
