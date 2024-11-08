package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.quests.ActionType;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestScheme;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.skills.BartenderSkill;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;

public class C2S_QuestActionPacket extends AbstractNetworkPacket<C2S_QuestActionPacket> {

    private ActionType actionType;
    private int entityId;
    // assign properties
    private int questIndex;
    // collection properties
    private int[] rewards;

    public C2S_QuestActionPacket() {
    }

    private C2S_QuestActionPacket(ActionType actionType, int entityId) {
        this.actionType = actionType;
        this.entityId = entityId;
    }

    public static C2S_QuestActionPacket makeAssignPacket(MayorEntity entity, Quest<?>[] quests, Quest<?> quest) {
        C2S_QuestActionPacket packet = new C2S_QuestActionPacket(ActionType.ASSIGN, entity.getId());
        packet.questIndex = ModUtils.indexOf(quests, quest);
        return packet;
    }

    public static C2S_QuestActionPacket makeCancelPacket(MayorEntity entity) {
        return new C2S_QuestActionPacket(ActionType.CANCEL, entity.getId());
    }

    public static C2S_QuestActionPacket makeCollectionPacket(MayorEntity entity, Integer[] data) {
        C2S_QuestActionPacket packet = new C2S_QuestActionPacket(ActionType.COLLECT, entity.getId());
        int[] selectedRewards = new int[data.length];
        int i = 0;
        for (Integer integer : data) {
            selectedRewards[i++] = integer;
        }
        packet.rewards = selectedRewards;
        return packet;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(actionType);
        buffer.writeInt(entityId);
        switch (actionType) {
            case ASSIGN:
                buffer.writeInt(questIndex);
                break;
            case COLLECT:
                buffer.writeVarIntArray(rewards);
                break;
        }
    }

    @Override
    public C2S_QuestActionPacket decode(PacketBuffer buffer) {
        ActionType actionType = buffer.readEnum(ActionType.class);
        int entityId = buffer.readInt();
        C2S_QuestActionPacket packet = new C2S_QuestActionPacket(actionType, entityId);
        switch (actionType) {
            case ASSIGN:
                packet.questIndex = buffer.readInt();
                break;
            case COLLECT:
                packet.rewards = buffer.readVarIntArray();
                break;
        }
        return packet;
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ServerWorld world = player.getLevel();
        Entity entity = world.getEntity(entityId);
        if (entity instanceof MayorEntity) {
            MayorEntity mayor = (MayorEntity) entity;
            QuestingDataProvider.getData(world).ifPresent(questing -> {
                QuestingGroup group = questing.getOrCreateGroup(player);
                if (!group.isLeader(player.getUUID())) {
                    GunsRPG.log.warn("Player {} attempted to perform quest event '{}' while not being a leader!", player, this.actionType);
                    return;
                }
                switch (this.actionType) {
                    case ASSIGN:
                        this.handleQuestAssign(questing, group, player, mayor);
                        break;
                    case CANCEL:
                        this.handleQuestCancellation(questing, group, player);
                        break;
                    case COLLECT:
                        this.handleQuestRewardCollection(questing, group, player);
                        break;
                }
                PlayerData.get(player).ifPresent(data -> {
                    ITraderStandings standings = data.getMayorReputationProvider();
                    ITraderStatus traderStatus = standings.getStatusWithTrader(mayor.getUUID());
                    ReputationStatus status = ReputationStatus.getStatus(traderStatus.getReputation());
                    MayorEntity.ListedQuests quests = mayor.getQuests(group.getGroupId()); // Load quests based on party
                    NetworkManager.sendClientPacket(player, new S2C_OpenQuestScreen(status, quests.toNbt(), mayor.getId(), mayor.getCurrentRefreshTarget()));
                });
            });
        }
    }

    private void handleQuestAssign(IQuestingData questingData, QuestingGroup group, ServerPlayerEntity player, MayorEntity mayor) {
        Quest<?> activeQuest = questingData.getActiveQuest(group);
        if (activeQuest != null) {
            GunsRPG.log.error("Cannot assign over active quest");
            return;
        }
        MayorEntity.ListedQuests listedQuests = mayor.getQuests(player.getUUID()); // at this point this player should be always a leader
        if (listedQuests != null) {
            Quest<?>[] quests = listedQuests.getQuests();
            if (questIndex >= 0 && questIndex < quests.length) {
                Quest<?> quest = quests[questIndex];
                quest.setStatus(QuestStatus.ACTIVE);
                listedQuests.filterActive();
                questingData.assignQuest(quest, group);
                quest.assign(group, player.level);
                questingData.sendData();
            }
        }
    }

    private void handleQuestCancellation(IQuestingData questing, QuestingGroup group, ServerPlayerEntity player) {
        Quest<?> activeQuest = questing.getActiveQuest(group);
        if (activeQuest == null) {
            GunsRPG.log.warn("Attempted to cancel already inactive quest by player {}, ignoring action", player);
            return;
        }
        QuestStatus status = activeQuest.getStatus();
        if (status != QuestStatus.ACTIVE) {
            GunsRPG.log.error("Cannot cancel inactive quest");
            return;
        }
        activeQuest.onFailed();
    }

    private void handleQuestRewardCollection(IQuestingData questing, QuestingGroup group, ServerPlayerEntity player) {
        Optional<ISkillProvider> skillProvider = PlayerData.get(player).map(IPlayerData::getSkillProvider);
        Quest<?> activeQuest = questing.getActiveQuest(group);
        if (activeQuest == null) {
            GunsRPG.log.warn("Player {} has attempted to claim reward from NULL quest", player);
            return;
        }
        QuestScheme<?> scheme = activeQuest.getScheme();
        if (scheme.isSpecialTaskQuest()) {
            GunsRPG.log.error("Unable to claim rewards from special quest");
            return;
        }
        if (activeQuest.getStatus() != QuestStatus.COMPLETED) {
            GunsRPG.log.error("Cannot claim rewards for quest in {} state. Requires COMPLETED state.", activeQuest.getStatus());
            return;
        }
        int rewardLimit = skillProvider
                .map(provider -> SkillUtil.getTopHierarchySkill(Skills.BARTENDER_I, provider))
                .map(BartenderSkill::getRewardCount).orElse(1);
        if (this.rewards.length > rewardLimit) {
            GunsRPG.log.error("Cannot claim more than {} rewards! {} Tried to pick {} rewards", rewardLimit, player.getName().getString(), rewards.length);
            return;
        }
        QuestReward reward = activeQuest.getReward();
        if (reward != null) {
            QuestReward.Choice[] choices = reward.getChoices();
            for (int selectedChoice : this.rewards) {
                if (selectedChoice >= 0 && selectedChoice < choices.length) {
                    QuestReward.Choice choice = choices[selectedChoice];
                    choice.distributeToInventory(player);
                }
            }
        }
        activeQuest.setStatus(QuestStatus.CLAIMED);
        questing.unassignQuest(group);
        questing.sendData();
    }
}
