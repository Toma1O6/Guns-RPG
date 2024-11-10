package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class C2S_RemoveFromGroup extends AbstractNetworkPacket<C2S_RemoveFromGroup> {

    private final UUID targetId;

    public C2S_RemoveFromGroup() {
        this(null);
    }

    public C2S_RemoveFromGroup(UUID targetId) {
        this.targetId = targetId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(this.targetId);
    }

    @Override
    public C2S_RemoveFromGroup decode(PacketBuffer buffer) {
        return new C2S_RemoveFromGroup(buffer.readUUID());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        IQuestingData questing = QuestingDataProvider.getQuesting(sender.level);
        QuestingGroup group = questing.getOrCreateGroup(sender);
        boolean isMe = this.targetId.equals(sender.getUUID());
        if (!isMe && !group.isLeader(sender.getUUID())) {
            GunsRPG.log.warn(QuestSystem.MARKER, "Player {} attempted to remove member {} from group without sufficient permissions", sender.getName().getString(), targetId, group.getGroupId());
            return;
        }
        if (group.getMemberCount() == 1) {
            GunsRPG.log.warn(QuestSystem.MARKER, "Ignoring group leave request as there is only one member in group");
            return;
        }
        questing.removeFromGroup(this.targetId);
    }
}
