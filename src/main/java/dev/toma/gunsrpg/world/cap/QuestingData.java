package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.quest.QuestTypes;
import dev.toma.gunsrpg.common.quests.quest.area.IAreaQuest;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SendQuestingData;
import dev.toma.gunsrpg.util.helper.NbtHelper;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
        GunsRPG.log.debug(QuestSystem.MARKER, "Created new group for player {}, groupId {}", player.getName().getString(), group.getGroupId());
        this.sendData();
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
        quest.assign(group, this.world);
        GunsRPG.log.debug(QuestSystem.MARKER, "Assigned active quest {} to group {}", quest.getScheme().getDisplayInfo().getName().getString(), group.getGroupId());
    }

    @Override
    public void unassignQuest(QuestingGroup group) {
        this.activeQuests.remove(group.getGroupId());
        GunsRPG.log.debug(QuestSystem.MARKER, "Unassigned active quest {}", group.getGroupId());
    }

    @Override
    public Collection<GroupInvite> listInvitesForPlayer(UUID playerId) {
        return this.groupsById.values().stream()
                .flatMap(group -> group.listInvitesForPlayer(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<GroupInvite> listInvitesForPlayer(PlayerEntity player) {
        return this.listInvitesForPlayer(player.getUUID());
    }

    @Override
    public void clearInvitesForPlayer(UUID playerId) {
        GunsRPG.log.debug(QuestSystem.MARKER, "Clearing all invites for player {}", playerId);
        this.groupsById.values().forEach(group -> group.deleteInviteFor(playerId));
    }

    @Override
    public void clearInvitesForPlayer(PlayerEntity player) {
        this.clearInvitesForPlayer(player.getUUID());
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
    public void triggerAll(Trigger trigger, PlayerEntity source, Consumer<IPropertyHolder> holderBuilder, Predicate<Quest<?>> filter) {
        this.activeQuests.values().forEach(quest -> {
            if (quest.getStatus() != QuestStatus.ACTIVE || !filter.test(quest)) {
                return;
            }
            IPropertyHolder holder = PropertyContext.create();
            holderBuilder.accept(holder);
            holder.setProperty(QuestProperties.PLAYER, source);
            holder.setProperty(QuestProperties.LEVEL, source.level);
            holder.setProperty(QuestProperties.ACCESS, quest.getDataAccess());
            quest.trigger(trigger, holder);
        });
    }

    @Override
    public Stream<Quest<?>> getActiveQuests() {
        return activeQuests.values().stream()
                .filter(quest -> quest.isAssigned() && quest.getStatus() == QuestStatus.ACTIVE);
    }

    @Override
    public void tickQuests() {
        this.activeQuests.values().forEach(quest -> {
            if (quest.getStatus() != QuestStatus.PAUSED) {
                quest.tickQuest();
            }
        });
        if (this.world.isClientSide())
            return;
        if (this.world.getGameTime() % 30L == 0L) {
            this.groupsById.values().forEach(group -> group.updateHealthData((ServerWorld) this.world));
            this.sendData();
        }
    }

    @Override
    public void addToGroup(QuestingGroup group, PlayerEntity player) {
        this.removeFromGroup(player.getUUID());
        this.groupsByPlayerId.put(player.getUUID(), group);
        GunsRPG.log.debug(QuestSystem.MARKER, "Player group link updated to groupId {} for playerId {}", group.getGroupId(), player.getUUID());
    }

    @Override
    public void removeFromGroup(UUID playerId) {
        GunsRPG.log.debug(QuestSystem.MARKER, "Attempting to remove player from their active group");
        QuestingGroup group = this.getGroup(playerId);
        if (group == null)
            return;
        if (group.isLeader(playerId)) {
            Quest<?> activeQuest = this.getActiveQuest(group);
            List<UUID> remainingMembers = new ArrayList<>(group.getMembers());
            remainingMembers.remove(playerId);
            if (activeQuest != null) {
                GunsRPG.log.debug(QuestSystem.MARKER, "Handling active quest as failed for party {}", playerId);
                if (activeQuest.getStatus() == QuestStatus.ACTIVE) {
                    activeQuest.setStatus(QuestStatus.FAILED);
                    activeQuest.onFailed();
                }
                this.unassignQuest(group);
            }
            deleteGroup(group);
            remainingMembers.stream().map(this.world::getPlayerByUUID)
                    .filter(Objects::nonNull)
                    .forEach(player -> {
                        this.getOrCreateGroup(player);
                        player.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.group_disbanded"), Util.NIL_UUID);
                    });
            this.sendData();
        } else if (group.isMember(playerId)) {
            Quest<?> activeQuest = this.getActiveQuest(group);
            if (activeQuest != null && activeQuest.getStatus() == QuestStatus.ACTIVE && activeQuest.isStarted()) {
                PlayerEntity player = this.world.getPlayerByUUID(playerId);
                if (player != null) {
                    PlayerData.get(player).ifPresent(data -> {
                        ITraderStandings standings = data.getMayorReputationProvider();
                        standings.questFailed(activeQuest.getMayorUUID(), activeQuest);
                    });
                }
            }
            GunsRPG.log.debug(QuestSystem.MARKER, "Removing player {} group link ID {}", playerId, group.getGroupId());
            String name = group.getName(playerId);
            group.removeMember(playerId);
            group.accept(this.world, member -> member.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.member_left", name), Util.NIL_UUID));
            PlayerEntity player = world.getPlayerByUUID(playerId);
            if (player != null) {
                player.sendMessage(new TranslationTextComponent("gunsrpg.quest.party.party_left"), Util.NIL_UUID);
            }
            this.groupsByPlayerId.remove(playerId);
            this.sendData();
        }
    }

    @Override
    public void handlePlayerLogIn(PlayerEntity player) {
        this.getOrCreateGroup(player);
        Quest<?> activeQuest = this.getActiveQuestForPlayer(player);
        if (activeQuest != null && (activeQuest.getStatus() == QuestStatus.ACTIVE || activeQuest.getStatus() == QuestStatus.PAUSED)) {
            activeQuest.playerJoined(player);
            activeQuest.setStatus(QuestStatus.ACTIVE);
        }
        this.sendData();
    }

    @Override
    public void handlePlayerLogOut(PlayerEntity player) {
        GunsRPG.log.debug(QuestSystem.MARKER, "Handling player {} disconnect", player.getUUID());
        QuestingGroup group = this.getOrCreateGroup(player);
        Quest<?> activeQuest = this.getActiveQuestForPlayer(player);
        if (!group.isLeader(player.getUUID()) || group.getMemberCount() > 1) {
            this.removeFromGroup(player.getUUID());
        } else if (activeQuest != null && activeQuest.getStatus() == QuestStatus.ACTIVE) {
            activeQuest.setStatus(QuestStatus.PAUSED);
            sendData();
        }
    }

    @Override
    public void sendData() {
        if (world.isClientSide())
            return;
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
        Set<UUID> invalid = new HashSet<>();
        this.activeQuests.forEach((partyId, quest) -> {
            QuestingGroup group = this.groupsById.get(partyId);
            if (group != null) {
                quest.assign(group, world);
            } else {
                invalid.add(partyId);
            }
        });
        invalid.forEach(this.activeQuests::remove);
    }

    private void deleteGroup(QuestingGroup group) {
        GunsRPG.log.debug(QuestSystem.MARKER, "Removing group {}", group.getGroupId());
        group.getMembers().forEach(this.groupsByPlayerId::remove);
        this.groupsById.remove(group.getGroupId());
    }
}
