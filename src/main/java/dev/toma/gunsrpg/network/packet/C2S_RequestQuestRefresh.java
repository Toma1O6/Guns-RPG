package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_RequestQuestRefresh extends AbstractNetworkPacket {

    private final int mayorId;

    public C2S_RequestQuestRefresh(int mayorId) {
        this.mayorId = mayorId;
    }

    public C2S_RequestQuestRefresh(PacketBuffer buffer) {
        this(buffer.readInt());
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(mayorId);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        ServerWorld world = sender.getLevel();
        Entity entity = world.getEntity(this.mayorId);
        if (!(entity instanceof MayorEntity)) {
            GunsRPG.log.error("Player {} attempted to refresh quests for invalid entity {}", sender, entity);
            return;
        }
        MayorEntity mayor = (MayorEntity) entity;
        double dist = entity.distanceTo(sender);
        if (dist > 64) {
            GunsRPG.log.error("Player {} attempted to refresh quests for entity {} at distance {}", sender, entity, dist);
            return;
        }
        IPlayerData data = PlayerData.getUnsafe(sender);
        IPointProvider provider = data.getPerkProvider();
        int points = provider.getPoints();
        int price = MayorEntity.REFRESH_PRICE;
        if (points < price) {
            GunsRPG.log.error("Player {} attempted to refresh quests but only {} points were available", sender, points);
            return;
        }
        provider.addPoints(-price);
        data.sync(DataFlags.PERK);

        IQuestingData questing = QuestingDataProvider.getQuesting(world);
        QuestingGroup group = questing.getOrCreateGroup(sender);
        if (!group.isLeader(sender)) {
            GunsRPG.log.warn("Player {} attempted to refresh quests while not being a leader!", sender);
            return;
        }

        mayor.reloadQuests(group.getGroupId(), sender);
        NetworkManager.sendClientPacket(sender, QuestingDataProvider.createQuestScreenRequest(mayor, sender));
    }
}
