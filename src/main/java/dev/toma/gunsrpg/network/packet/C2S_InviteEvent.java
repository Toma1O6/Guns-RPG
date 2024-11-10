package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.object.Interaction;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class C2S_InviteEvent extends AbstractNetworkPacket<C2S_InviteEvent> {

    private boolean accepted;
    private UUID targetId;

    public C2S_InviteEvent() {
    }

    public C2S_InviteEvent(boolean accepted, UUID targetId) {
        this.accepted = accepted;
        this.targetId = targetId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(this.accepted);
        buffer.writeUUID(this.targetId);
    }

    @Override
    public C2S_InviteEvent decode(PacketBuffer buffer) {
        boolean accepted = buffer.readBoolean();
        UUID targetId = buffer.readUUID();
        return new C2S_InviteEvent(accepted, targetId);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        IQuestingData questingData = QuestingDataProvider.getQuesting(sender.level);
        QuestingGroup group = questingData.getGroup(this.targetId);
        if (group == null) {
            questingData.sendData();
            GunsRPG.log.warn(QuestSystem.MARKER, "Received invite event packet for invalid group {} from player {}", this.targetId, sender.getName().getString());
            return;
        }
        GroupInvite invite = group.getInvite(sender.getUUID());
        if (invite == null) {
            questingData.sendData();
            GunsRPG.log.warn(QuestSystem.MARKER, "Player attempted to accept invalid invite", sender.getName().getString());
            return;
        }
        Interaction<?> result = this.accepted ? group.onInviteAccepted(invite, sender) : group.onInviteRejected(invite);
        result.ifSuccessOrElse(t -> {
            MinecraftServer server = sender.getServer();
            PlayerList playerList = server.getPlayerList();
            ServerPlayerEntity groupLeader = playerList.getPlayer(group.getGroupId());
            if (this.accepted) {
                sender.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.invite_accepted.receiver", group.getName()), Util.NIL_UUID);
                group.accept(sender.level, player -> player.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.invite_accepted.sender", sender.getName()), Util.NIL_UUID));
            } else {
                sender.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.invite_rejected.receiver", group.getName()), Util.NIL_UUID);
                if (groupLeader != null)
                    groupLeader.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.invite_rejected.sender", sender.getName()), Util.NIL_UUID);
            }
            questingData.sendData();
        }, err -> sender.sendMessage(err, Util.NIL_UUID));
    }
}
