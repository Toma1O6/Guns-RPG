package dev.toma.gunsrpg.integration.questing.quest;

import dev.toma.questing.common.component.condition.provider.ConditionProvider;
import dev.toma.questing.common.component.distributor.RewardDistributor;
import dev.toma.questing.common.component.task.provider.TaskProvider;
import dev.toma.questing.common.quest.QuestType;
import dev.toma.questing.common.quest.provider.AbstractQuestProvider;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class TieredQuestProvider extends AbstractQuestProvider<TieredQuest> {

    public TieredQuestProvider(ResourceLocation identifier, List<ConditionProvider<?>> conditions, List<TaskProvider<?>> tasks, RewardDistributor distributor) {
        super(identifier, conditions, tasks, distributor);
    }

    @Override
    public QuestType<TieredQuest, ?> getType() {
        return null;
    }

    @Override
    public TieredQuest createQuest() {
        return new TieredQuest(this);
    }
}
