package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.common.quests.IQuestInstance;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public interface IQuestTriggerManager {

    <CTX> void doTrigger(TriggerType<CTX> type, CTX context, @Nullable PlayerEntity player);

    void questAdded(PlayerEntity player, IQuestInstance quest);

    void questRemoved(PlayerEntity player, IQuestInstance quest);

    void playerAddedToServer(PlayerEntity player);

    void playerRemovedFromServer(PlayerEntity player);
}
