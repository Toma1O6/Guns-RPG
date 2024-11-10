package dev.toma.gunsrpg.common.quests.sharing;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.event.QuestingEvent;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.config.QuestConfig;
import dev.toma.gunsrpg.util.helper.NbtHelper;
import dev.toma.gunsrpg.util.object.Interaction;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class QuestingGroup {

    private final UUID groupId;
    private final Set<UUID> members = new LinkedHashSet<>();
    private final Map<UUID, String> usernames = new HashMap<>();
    private final Map<UUID, GroupInvite> activeInvites = new HashMap<>();

    public QuestingGroup(UUID owner) {
        this.groupId = owner;
    }

    public QuestingGroup(CompoundNBT tag) {
        this.groupId = UUID.fromString(tag.getString("id"));
        NbtHelper.deserializeCollection(this.members, tag.getList("members", Constants.NBT.TAG_STRING), inbt -> UUID.fromString(inbt.getAsString()));
        NbtHelper.deserializeMap(this.usernames, tag.getCompound("usernames"), UUID::fromString, INBT::getAsString);
        NbtHelper.deserializeMap(this.activeInvites, tag.getCompound("invites"), UUID::fromString, inbt -> GroupInvite.fromNbt((CompoundNBT) inbt));
    }

    public static QuestingGroup create(PlayerEntity player) {
        QuestingGroup group = new QuestingGroup(player.getUUID());
        group.members.add(player.getUUID());
        group.usernames.put(player.getUUID(), player.getDisplayName().getString());
        return group;
    }

    public Interaction<GroupInvite> invite(PlayerEntity player) {
        UUID playerId = player.getUUID();
        if (this.isMember(playerId)) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.already_member", player.getDisplayName()));
        }
        if (this.isInvited(playerId)) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.already_invited", player.getDisplayName()));
        }
        IQuestingData questing = QuestingDataProvider.getQuesting(player.level);
        QuestingGroup group = questing.getOrCreateGroup(player);
        QuestConfig config = GunsRPG.config.quests;
        if (!config.allowInvitePlayersInGroup && group.getMemberCount() > 1) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.cannot_invite"));
        }
        int currentPartySize = this.members.size();
        if (currentPartySize >= this.getMaxMemberCount()) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.full"));
        }
        Quest<?> quest = questing.getActiveQuest(this);
        if (quest != null && quest.isStarted()) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.cannot_join"));
        }
        QuestingEvent.OnInviteSending event = new QuestingEvent.OnInviteSending(player.level, this, this.groupId, player.getUUID());
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getInteractionResult().isFailure()) {
            return event.getInteractionResult().failed();
        }
        GroupInvite invite = new GroupInvite(this.groupId, playerId);
        this.activeInvites.put(playerId, invite);
        return Interaction.success(invite);
    }

    public Interaction<Void> onInviteAccepted(GroupInvite invite, PlayerEntity player) {
        if (!this.activeInvites.containsKey(invite.getPlayerId())) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.not_invited"));
        }
        if (this.isMember(invite.getPlayerId())) {
            this.deleteInvite(invite);
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.already_member", player.getDisplayName()));
        }
        int currentPartySize = this.members.size();
        if (currentPartySize >= this.getMaxMemberCount()) {
            this.deleteInvite(invite);
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.full"));
        }
        IQuestingData questing = QuestingDataProvider.getQuesting(player.level);
        Quest<?> quest = questing.getActiveQuest(this);
        if (quest != null && quest.isStarted()) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.cannot_join"));
        }

        Quest<?> inviteeActiveQuest = questing.getActiveQuestForPlayer(player);
        if (inviteeActiveQuest != null) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.already_questing"));
        }
        QuestingEvent.OnInviteAccept event = new QuestingEvent.OnInviteAccept(player.level, this, invite, player);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getInteractionResult().isFailure()) {
            return event.getInteractionResult().failed();
        }
        questing.clearInvitesForPlayer(player);
        questing.addToGroup(this, player);
        UUID playerId = player.getUUID();
        String displayName = player.getDisplayName().getString();
        this.usernames.put(playerId, displayName);
        this.members.add(playerId);
        this.deleteInvite(invite);
        return Interaction.success(null);
    }

    public Interaction<Void> onInviteRejected(GroupInvite invite) {
        this.deleteInvite(invite);
        return Interaction.success(null);
    }

    public Interaction<Void> onInviteCancelled(GroupInvite invite) {
        this.deleteInvite(invite);
        return Interaction.success(null);
    }

    public Stream<GroupInvite> listInvitesForPlayer(UUID playerId) {
        GroupInvite invite = this.activeInvites.get(playerId);
        return invite != null ? Stream.of(invite) : Stream.empty();
    }

    public void deleteInviteFor(UUID playerId) {
        this.activeInvites.remove(playerId);
    }

    public GroupInvite getInvite(UUID inviteeId) {
        return this.activeInvites.get(inviteeId);
    }

    private void deleteInvite(GroupInvite invite) {
        this.deleteInviteFor(invite.getPlayerId());
    }

    public void removeMember(UUID memberId) {
        if (this.isLeader(memberId)) {
            throw new UnsupportedOperationException("Cannot remove team leader from party!");
        }
        this.members.remove(memberId);
        this.usernames.remove(memberId);
    }

    public Quest<?> getActiveQuest(World world) {
        IQuestingData questingData = QuestingDataProvider.getData(world).orElse(null);
        return questingData.getActiveQuest(this.groupId);
    }

    public boolean isLeader(UUID playerId) {
        return this.groupId.equals(playerId);
    }

    public boolean isMember(UUID playerId) {
        return members.contains(playerId);
    }

    public boolean isInvited(UUID playerId) {
        return activeInvites.containsKey(playerId);
    }

    public UUID getGroupId() {
        return groupId;
    }

    public Collection<UUID> getMembers() {
        return this.members;
    }

    public int getMemberCount() {
        return this.members.size();
    }

    public void accept(World world, Consumer<PlayerEntity> consumer) {
        this.members.stream().map(world::getPlayerByUUID).filter(Objects::nonNull).forEach(consumer);
    }

    public boolean predicate(World world, Predicate<PlayerEntity> predicate) {
        return this.members.stream()
                .map(world::getPlayerByUUID)
                .filter(Objects::nonNull)
                .allMatch(predicate);
    }

    public <T> T mapActive(World world, T identity, Function<PlayerEntity, T> function, BinaryOperator<T> acc) {
        return this.members.stream()
                .map(world::getPlayerByUUID)
                .filter(Objects::nonNull)
                .map(function)
                .reduce(identity, acc);
    }

    public String getName(UUID playerId) {
        return this.usernames.getOrDefault(playerId, "N/A");
    }

    public String getName() {
        return this.getName(this.groupId);
    }

    public int getMaxMemberCount() {
        return GunsRPG.config.quests.maxPartySize;
    }


    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("id", this.groupId.toString());
        tag.put("members", NbtHelper.serializeCollection(this.members, uuid -> StringNBT.valueOf(uuid.toString())));
        tag.put("usernames", NbtHelper.serializeMap(this.usernames, UUID::toString, StringNBT::valueOf));
        tag.put("invites", NbtHelper.serializeMap(this.activeInvites, UUID::toString, GroupInvite::serialize));
        return tag;
    }
}
