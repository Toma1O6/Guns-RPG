package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.client.screen.QuestScreen;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_QuestActionPacket extends AbstractNetworkPacket<C2S_QuestActionPacket> {

    private QuestScreen.ActionType actionType;
    // assign properties
    private int entityId;
    private int questIndex;
    // collection properties
    private int[] rewards;

    public C2S_QuestActionPacket() {
    }

    private C2S_QuestActionPacket(QuestScreen.ActionType actionType) {
        this.actionType = actionType;
    }

    public static C2S_QuestActionPacket makeAssignPacket(MayorEntity entity, Quest<?>[] quests, Quest<?> quest) {
        C2S_QuestActionPacket packet = new C2S_QuestActionPacket(QuestScreen.ActionType.ASSIGN);
        packet.entityId = entity.getId();
        packet.questIndex = ModUtils.indexOf(quests, quest);
        return packet;
    }

    public static C2S_QuestActionPacket makeCancelPacket() {
        return new C2S_QuestActionPacket(QuestScreen.ActionType.CANCEL);
    }

    public static C2S_QuestActionPacket makeCollectionPacket(int[] data) {
        C2S_QuestActionPacket packet = new C2S_QuestActionPacket(QuestScreen.ActionType.COLLECT);
        packet.rewards = data;
        return packet;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(actionType);
        switch (actionType) {
            case ASSIGN:
                buffer.writeInt(entityId);
                buffer.writeInt(questIndex);
                break;
            case COLLECT:
                buffer.writeVarIntArray(rewards);
                break;
        }
    }

    @Override
    public C2S_QuestActionPacket decode(PacketBuffer buffer) {
        QuestScreen.ActionType actionType = buffer.readEnum(QuestScreen.ActionType.class);
        C2S_QuestActionPacket packet = new C2S_QuestActionPacket(actionType);
        switch (actionType) {
            case ASSIGN:
                packet.entityId = buffer.readInt();
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
        PlayerData.get(player).ifPresent(playerData -> {
            IQuests quests = playerData.getQuests();
            switch (actionType) {
                case ASSIGN:
                    handleQuestAssign(quests, player);
                    break;
                case CANCEL:
                    handleQuestCancellation(quests, player);
                    break;
                case COLLECT:
                    handleQuestRewardCollection(quests, player);
                    break;
            }
        });
    }

    private void handleQuestAssign(IQuests questProvider, ServerPlayerEntity player) {
        Entity entity = player.level.getEntity(entityId);
        if (questProvider.getActiveQuest().isPresent()) {
            GunsRPG.log.error("Cannot assign over active quest");
            return;
        }
        if (entity instanceof MayorEntity) {
            MayorEntity mayor = (MayorEntity) entity;
            MayorEntity.ListedQuests listedQuests = mayor.getQuests(player.getUUID());
            if (listedQuests != null) {
                Quest<?>[] quests = listedQuests.getQuests();
                if (questIndex >= 0 && questIndex < quests.length) {
                    Quest<?> quest = quests[questIndex];
                    quest.setStatus(QuestStatus.ACTIVE);
                    listedQuests.filterActive();
                    questProvider.assignQuest(quest);
                }
            }
            ITraderStatus status = questProvider.getTraderStandings().getStatusWithTrader(mayor.getUUID());
            ReputationStatus reputationStatus = ReputationStatus.getStatus(status.getReputation());
            MayorEntity.ListedQuests quests = mayor.getQuests(player.getUUID());
            NetworkManager.sendClientPacket(player, new S2C_OpenQuestScreen(reputationStatus, quests, mayor.getId()));
        }
    }

    private void handleQuestCancellation(IQuests provider, ServerPlayerEntity player) {
        provider.getActiveQuest().ifPresent(quest -> {
            if (quest.getStatus() != QuestStatus.ACTIVE) {
                GunsRPG.log.error("Cannot cancel inactive quest");
                return;
            }
            quest.onFailed(player);
        });
        provider.clearActiveQuest();
    }

    private void handleQuestRewardCollection(IQuests provider, ServerPlayerEntity player) {
        provider.getActiveQuest().ifPresent(quest -> {
            if (quest.getStatus() != QuestStatus.COMPLETED) {
                GunsRPG.log.error("Cannot claim rewards for quest in {} state. Requires COMPLETED state.", quest.getStatus());
                return;
            }
            QuestReward reward = quest.getReward();
            if (reward != null) {
                QuestReward.Choice[] choices = reward.getChoices();
                for (int choiceIndex : rewards) {
                    if (choiceIndex >= 0 && choiceIndex < choices.length) {
                        QuestReward.Choice choice = choices[choiceIndex];
                        choice.distributeToInventory(player);
                    }
                }
            }
            quest.setStatus(QuestStatus.CLAIMED);
        });
        provider.clearActiveQuest();
    }
}
