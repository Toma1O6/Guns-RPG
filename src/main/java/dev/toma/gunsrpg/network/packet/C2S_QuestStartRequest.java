package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public final class C2S_QuestStartRequest extends AbstractNetworkPacket<C2S_QuestStartRequest> {

    private final int entityId;
    private final int questIndex;

    public C2S_QuestStartRequest(int entityId, int questIndex) {
        this.entityId = entityId;
        this.questIndex = questIndex;
    }

    public C2S_QuestStartRequest(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
        this.questIndex = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.questIndex);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        ServerWorld level = sender.getLevel();
        IQuestingData questing = QuestingDataProvider.getQuesting(level);
        QuestingGroup group = questing.getOrCreateGroup(sender);
        Quest<?> activeQuest = questing.getActiveQuest(group);
        if (!group.isLeader(sender)) {
            GunsRPG.log.warn("Player {} attempted to start quest while not being a leader!", sender);
            return;
        }
        if (activeQuest != null) {
            GunsRPG.log.error("Player {} attempted to start quest while having already one active quest", sender);
            return;
        }
        Entity entity = level.getEntity(this.entityId);
        if (!(entity instanceof MayorEntity)) {
            GunsRPG.log.error("Player {} attempted to start quest for invalid entity {}", sender, entity);
            return;
        }
        MayorEntity mayor = (MayorEntity) entity;
        // check that the player is actually near the mayor
        double dist = entity.distanceTo(sender);
        if (dist > 64) {
            GunsRPG.log.error("Player {} attempted to start quest for entity {} at distance {}", sender, entity, dist);
            return;
        }

        MayorEntity.ListedQuests quests = mayor.getQuestsForGroup(sender.getUUID());
        if (quests == null || this.questIndex < 0 || this.questIndex >= quests.getQuestCount()) {
            GunsRPG.log.error("Player {} attempted to start quest for invalid quest index {}", sender, this.questIndex);
            return;
        }
        Quest<?> quest = quests.getQuest(this.questIndex);
        quest.setStatus(QuestStatus.ACTIVE);
        quests.refreshList();
        questing.assignQuest(quest, group);
        questing.sendData();
    }
}
