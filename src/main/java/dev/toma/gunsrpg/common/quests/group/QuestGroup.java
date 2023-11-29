package dev.toma.gunsrpg.common.quests.group;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

public final class QuestGroup {

    public static final Marker MARKER = MarkerManager.getMarker("QuestGroups");
    public static final Pattern GROUP_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9']+(\\s[a-zA-Z0-9'])*");
    public static final IntSupplier MAX_MEMBER_COUNT = () -> GunsRPG.config.quests.groupMemberLimit;

    private final UUID identifier;
    private final Set<UUID> members = new LinkedHashSet<>();
    private final Map<UUID, String> usernames = new HashMap<>();
    private final Set<GroupInvite> pendingInvitations = new LinkedHashSet<>();

    private String groupName;
    private UUID groupLeader;

    public QuestGroup() {
        this(UUID.randomUUID());
    }

    private QuestGroup(UUID identifier) {
        this.identifier = identifier;
    }

    public static QuestGroup createWithLeader(PlayerEntity leader) {
        QuestGroup group = new QuestGroup();
        UUID uuid = leader.getUUID();
        group.addMember(leader);
        group.assignAsGroupLeader(uuid);
        group.assignGroupName(String.format("%s's Group", leader.getDisplayName().getString()));
        return group;
    }

    // Group management -------------------------------------------------------------------------------------------

    public boolean isGroupLeader(PlayerEntity player) {
        return isGroupLeader(player.getUUID());
    }

    public boolean isGroupLeader(UUID uuid) {
        return groupLeader != null && groupLeader.equals(uuid);
    }

    public void assignAsGroupLeader(UUID uuid) {
        if (!isMember(uuid)) {
            GunsRPG.log.error(MARKER, "Cannot transfer group leadership to non group member");
            return;
        }
        this.groupLeader = uuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void assignGroupName(String groupName) {
        if (!isValidGroupName(groupName)) {
            GunsRPG.log.error(MARKER, "Attempted to assign invalid group name: {}", groupName);
            return;
        }
        this.groupName = groupName;
    }

    // Member management -----------------------------------------------------------------------------------------------

    public boolean addMember(PlayerEntity member) {
        if (isMember(member) || members.size() >= MAX_MEMBER_COUNT.getAsInt()) {
            GunsRPG.log.warn(MARKER, "Unable to add member {} to group", member);
            return false;
        }
        members.add(member.getUUID());
        usernames.put(member.getUUID(), member.getDisplayName().getString());
        return true;
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public boolean isMember(PlayerEntity player) {
        return isMember(player.getUUID());
    }

    public boolean removeMember(PlayerEntity member) {
        if (isGroupLeader(member)) {
            GunsRPG.log.error(MARKER, "Cannot remove group leader from their group");
            return false;
        }
        members.remove(member.getUUID());
        usernames.remove(member.getUUID());
        return true;
    }

    // Other -----------------------------------------------------------------------------------------------------------

    public static boolean isValidGroupName(String groupName) {
        return GROUP_NAME_PATTERN.matcher(groupName).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestGroup group = (QuestGroup) o;
        return Objects.equals(identifier, group.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return groupName != null ? String.format("Group %s - ID %s, Member count %d", groupName, identifier, members.size())
                : String.format("Group - ID %s, Member count %d", identifier, members.size());
    }

    public UUID getIdentifier() {
        return identifier;
    }
}
