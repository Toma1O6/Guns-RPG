package dev.toma.gunsrpg.common.quests.group;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class GroupManager {

    private static final GroupManager INSTANCE = new GroupManager();

    private final Map<UUID, QuestGroup> questGroupMap = new HashMap<>();

    public static GroupManager getManager() {
        return INSTANCE;
    }

    public void saveGroup(QuestGroup questGroup) {
        questGroupMap.put(questGroup.getIdentifier(), questGroup);
    }

    public Optional<QuestGroup> getGroupById(UUID uuid) {
        return Optional.ofNullable(questGroupMap.get(uuid));
    }

    public void removeMember(UUID member) {
        // TODO remove
    }

    public void removeMember(PlayerEntity player) {
        // TODO remove and create separate party
    }
}
