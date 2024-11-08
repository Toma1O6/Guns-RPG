package dev.toma.gunsrpg.common.quests.sharing;

import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;
import java.util.UUID;

public final class GroupInvite {

    private final UUID groupId;
    private final UUID playerId;

    public GroupInvite(UUID groupId, UUID playerId) {
        this.groupId = groupId;
        this.playerId = playerId;
    }

    public static GroupInvite fromNbt(CompoundNBT tag) {
        UUID groupId = UUID.fromString(tag.getString("groupId"));
        UUID playerId = UUID.fromString(tag.getString("playerId"));
        return new GroupInvite(groupId, playerId);
    }

    public UUID getGroupId() {
        return groupId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("groupId", groupId.toString());
        tag.putString("playerId", playerId.toString());
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupInvite)) return false;
        GroupInvite that = (GroupInvite) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, playerId);
    }
}
