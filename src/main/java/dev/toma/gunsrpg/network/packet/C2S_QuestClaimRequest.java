package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.skills.BartenderSkill;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

public final class C2S_QuestClaimRequest extends AbstractNetworkPacket {

    private final int entityId;
    private final List<Integer> rewardIndices;

    public C2S_QuestClaimRequest(int entityId, List<Integer> rewardIndices) {
        this.entityId = entityId;
        this.rewardIndices = rewardIndices;
    }

    public C2S_QuestClaimRequest(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
        int size = buffer.readInt();
        this.rewardIndices = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.rewardIndices.add(buffer.readInt());
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.rewardIndices.size());
        this.rewardIndices.forEach(buffer::writeInt);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        ServerWorld level = sender.getLevel();
        Entity entity = level.getEntity(this.entityId);
        if (!(entity instanceof MayorEntity)) {
            GunsRPG.log.error("Player {} attempted to claim reward for invalid entity {}", sender, entity);
            return;
        }
        MayorEntity mayor = (MayorEntity) entity;
        // check that the player is actually near the mayor
        double dist = entity.distanceTo(sender);
        if (dist > 64) {
            GunsRPG.log.error("Player {} attempted to claim reward for entity {} at distance {}", sender, entity, dist);
            return;
        }
        int maxCount = BartenderSkill.getRewardClaimSize(sender);
        if (this.rewardIndices.size() > maxCount) {
            GunsRPG.log.error("Player {} attempted to claim more rewards than allowed ({})", sender, maxCount);
            return;
        }

        QuestReward reward = mayor.claimReward(sender.getUUID());
        if (reward == null) {
            GunsRPG.log.error("Player {} attempted to claim reward for entity {} but no reward was available", sender, entity);
            return;
        }
        QuestReward.Choice[] choices = reward.getChoices();
        for (int index : this.rewardIndices) {
            if (index < 0 || index >= choices.length) {
                GunsRPG.log.error("Player {} attempted to claim reward for entity {} with invalid reward choice index {}", sender, entity, index);
                return;
            }
            QuestReward.Choice choice = choices[index];
            choice.distributeToInventory(sender);
        }
        NetworkManager.sendClientPacket(sender, QuestingDataProvider.createQuestScreenRequest(mayor, sender));
    }
}
