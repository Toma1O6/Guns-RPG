package dev.toma.gunsrpg.common.quests.sharing;

import dev.toma.gunsrpg.api.common.event.QuestingEvent;
import dev.toma.gunsrpg.util.helper.NbtHelper;
import dev.toma.gunsrpg.util.object.Interaction;
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

// TODO leave party on game logout
public final class QuestingGroup {

    public static final int PARTY_LIMIT = 5;
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

    // Invite management
    public Interaction<GroupInvite> invite(PlayerEntity player) {
        UUID playerId = player.getUUID();
        if (this.isMember(playerId)) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.already_member", player.getDisplayName()));
        }
        if (this.isInvited(playerId)) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.already_invited", player.getDisplayName()));
        }
        int currentPartySize = this.members.size();
        if (currentPartySize >= PARTY_LIMIT) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.full"));
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
        if (currentPartySize >= PARTY_LIMIT) {
            return Interaction.failure(new TranslationTextComponent("gunsrpg.quest.party.full"));
        }
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

    private void deleteInvite(GroupInvite invite) {
        this.activeInvites.remove(invite.getPlayerId());
    }

    // Party management
    // TODO member removal - self/kick
    // TODO party disband

    // Utils

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

    public void acceptActive(World world, Consumer<PlayerEntity> consumer) {
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

    // Serialization
    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("id", this.groupId.toString());
        tag.put("members", NbtHelper.serializeCollection(this.members, uuid -> StringNBT.valueOf(uuid.toString())));
        tag.put("usernames", NbtHelper.serializeMap(this.usernames, UUID::toString, StringNBT::valueOf));
        tag.put("invites", NbtHelper.serializeMap(this.activeInvites, UUID::toString, GroupInvite::serialize));
        return tag;
    }
}
