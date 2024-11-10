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
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class C2S_InviteMember extends AbstractNetworkPacket<C2S_InviteMember> {

    private final UUID inviteeId;

    public C2S_InviteMember() {
        this(null);
    }

    public C2S_InviteMember(UUID inviteeId) {
        this.inviteeId = inviteeId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(this.inviteeId);
    }

    @Override
    public C2S_InviteMember decode(PacketBuffer buffer) {
        return new C2S_InviteMember(buffer.readUUID());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        IQuestingData questing = QuestingDataProvider.getQuesting(sender.level);
        QuestingGroup group = questing.getOrCreateGroup(sender);
        ServerPlayerEntity invitee = sender.getServer().getPlayerList().getPlayer(this.inviteeId);
        if (invitee == null) {
            GunsRPG.log.warn(QuestSystem.MARKER, "Attempted to invite unknown player by ID {}", this.inviteeId);
            return;
        }
        Interaction<GroupInvite> interaction = group.invite(invitee);
        if (interaction.isFailure()){
            sender.sendMessage(interaction.getMessage(), Util.NIL_UUID);
            return;
        }
        invitee.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.invite_received", group.getName()), Util.NIL_UUID);
        sender.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.invite_sent", invitee.getName()), Util.NIL_UUID);
    }
}
