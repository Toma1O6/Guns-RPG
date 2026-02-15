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

public final class C2S_QuestCancelRequest extends AbstractNetworkPacket {

    private final int entityId;

    public C2S_QuestCancelRequest(int entityId) {
        this.entityId = entityId;
    }

    public C2S_QuestCancelRequest(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        ServerWorld level = sender.getLevel();
        IQuestingData questing = QuestingDataProvider.getQuesting(level);
        QuestingGroup group = questing.getOrCreateGroup(sender);
        Quest<?> quest = questing.getActiveQuest(group);
        if (!group.isLeader(sender)) {
            GunsRPG.log.warn("Player {} attempted to cancel quest while not being a leader!", sender);
            return;
        }
        if (quest == null) {
            GunsRPG.log.error("Player {} attempted to cancel quest while having no active quest", sender);
            return;
        }
        if (quest.getStatus() != QuestStatus.ACTIVE) {
            GunsRPG.log.error("Player {} attempted to cancel quest in {} state", sender, quest.getStatus());
            return;
        }
        Entity entity = level.getEntity(this.entityId);
        if (!(entity instanceof MayorEntity)) {
            GunsRPG.log.error("Player {} attempted to cancel quest for invalid entity {}", sender, entity);
            return;
        }
        MayorEntity mayor = (MayorEntity) entity;
        // check that the player is actually near the mayor
        double dist = entity.distanceTo(sender);
        if (dist > 64) {
            GunsRPG.log.error("Player {} attempted to cancel quest for entity {} at distance {}", sender, entity, dist);
            return;
        }
        if (!quest.isManageableByMayor(mayor)) {
            GunsRPG.log.error("Player {} attempted to cancel quest for entity {} that is not quest owner", sender, entity);
            return;
        }
        quest.onFailed();
        NetworkManager.sendClientPacket(sender, QuestingDataProvider.createQuestScreenRequest(mayor, sender));
    }
}
