package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.quest.QuestTypes;
import dev.toma.gunsrpg.common.quests.quest.area.IAreaQuest;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SendQuestingData;
import dev.toma.gunsrpg.util.helper.NbtHelper;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class QuestingData implements IQuestingData {

    private final World world;
    private final Map<UUID, QuestingGroup> groupsById = new HashMap<>();
    private final Map<UUID, QuestingGroup> groupsByPlayerId = new HashMap<>();
    private final Map<UUID, Quest<?>> activeQuests = new HashMap<>();

    public QuestingData() {
        this(null);
    }

    public QuestingData(World world) {
        this.world = world;
    }

    @Override
    public QuestingGroup getGroup(UUID playerId) {
        return this.groupsByPlayerId.get(playerId);
    }

    @Override
    public QuestingGroup getOrCreateGroup(PlayerEntity player) {
        UUID playerId = player.getUUID();
        QuestingGroup group = this.getGroup(playerId);
        if (group != null) {
            return group;
        }
        group = QuestingGroup.create(player);
        this.groupsById.put(group.getGroupId(), group);
        this.groupsByPlayerId.put(playerId, group);
        this.sendData(player);
        return group;
    }

    @Override
    public Quest<?> getActiveQuest(QuestingGroup group) {
        return this.getActiveQuest(group.getGroupId());
    }

    @Override
    public Quest<?> getActiveQuest(UUID groupId) {
        return this.activeQuests.get(groupId);
    }

    @Override
    public Quest<?> getActiveQuestForPlayer(PlayerEntity player) {
        return this.getActiveQuestForPlayer(player.getUUID());
    }

    @Override
    public Quest<?> getActiveQuestForPlayer(UUID playerId) {
        QuestingGroup group = this.getGroup(playerId);
        if (group == null) {
            return null;
        }
        return this.getActiveQuest(group);
    }

    @Override
    public void assignQuest(Quest<?> quest, QuestingGroup group) {
        this.activeQuests.put(group.getGroupId(), quest);
    }

    @Override
    public void unassignQuest(QuestingGroup group) {
        this.activeQuests.remove(group.getGroupId());
    }

    @Override
    public Stream<QuestArea> getActiveQuestAreas() {
        return this.activeQuests.values().stream()
                .filter(quest -> quest.getStatus() == QuestStatus.ACTIVE && quest instanceof IAreaQuest)
                .map(quest -> ((IAreaQuest) quest).getQuestArea());
    }

    @Override
    public void trigger(Trigger trigger, PlayerEntity player, Consumer<IPropertyHolder> holderBuilder) {
        Quest<?> quest = this.getActiveQuestForPlayer(player);
        if (quest == null || quest.getStatus() != QuestStatus.ACTIVE) {
            return;
        }
        IPropertyHolder holder = PropertyContext.create();
        holderBuilder.accept(holder);
        holder.setProperty(QuestProperties.PLAYER, player);
        holder.setProperty(QuestProperties.LEVEL, player.level);
        holder.setProperty(QuestProperties.ACCESS, quest.getDataAccess());
        quest.trigger(trigger, holder);
    }

    @Override
    public void tickQuests() {
        this.activeQuests.values().forEach(Quest::tickQuest);
    }

    @Override
    public void sendData(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            NetworkManager.sendClientPacket((ServerPlayerEntity) player, new S2C_SendQuestingData(this.serializeNBT()));
        }
    }

    @Override
    public void sendData() {
        S2C_SendQuestingData packet = new S2C_SendQuestingData(this.serializeNBT());
        NetworkManager.sendWorldPacket(this.world, packet);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("groups", NbtHelper.serializeMap(this.groupsById, UUID::toString, QuestingGroup::serialize));
        nbt.put("playerGroups", NbtHelper.serializeMap(this.groupsByPlayerId, UUID::toString, group -> StringNBT.valueOf(group.getGroupId().toString())));
        nbt.put("activeQuests", NbtHelper.serializeMap(this.activeQuests, UUID::toString, Quest::serialize));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        NbtHelper.deserializeMap(this.groupsById, nbt.getCompound("groups"), UUID::fromString, inbt -> new QuestingGroup((CompoundNBT) inbt));
        NbtHelper.deserializeMap(this.groupsByPlayerId, nbt.getCompound("playerGroups"), UUID::fromString, inbt -> this.groupsById.get(UUID.fromString(inbt.getAsString())));
        NbtHelper.deserializeMap(this.activeQuests, nbt.getCompound("activeQuests"), UUID::fromString, inbt -> QuestTypes.getFromNbt(this.world, (CompoundNBT) inbt));
    }
}
