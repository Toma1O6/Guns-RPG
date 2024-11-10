package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.object.Interaction;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface IQuestingData extends INBTSerializable<CompoundNBT> {

    QuestingGroup getOrCreateGroup(PlayerEntity player);

    QuestingGroup getGroup(UUID playerId);

    void removeFromGroup(UUID playerId);

    Quest<?> getActiveQuest(QuestingGroup group);

    Quest<?> getActiveQuest(UUID groupId);

    Quest<?> getActiveQuestForPlayer(PlayerEntity player);

    Quest<?> getActiveQuestForPlayer(UUID playerId);

    Collection<GroupInvite> listInvitesForPlayer(PlayerEntity player);

    Collection<GroupInvite> listInvitesForPlayer(UUID playerId);

    void addToGroup(QuestingGroup group, PlayerEntity player);

    void clearInvitesForPlayer(PlayerEntity player);

    void clearInvitesForPlayer(UUID playerId);

    Stream<QuestArea> getActiveQuestAreas();

    void assignQuest(Quest<?> quest, QuestingGroup group);

    void unassignQuest(QuestingGroup group);

    void trigger(Trigger trigger, PlayerEntity player, Consumer<IPropertyHolder> holderBuilder);

    void tickQuests();

    void handlePlayerLogIn(PlayerEntity player);

    void handlePlayerLogOut(PlayerEntity player);

    void sendData();
}
