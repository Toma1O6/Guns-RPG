package dev.toma.gunsrpg.integration.questing.quest;

import dev.toma.questing.common.component.condition.provider.ConditionProvider;
import dev.toma.questing.common.component.distributor.RewardDistributor;
import dev.toma.questing.common.component.task.provider.TaskProvider;
import dev.toma.questing.common.quest.QuestType;
import dev.toma.questing.common.quest.provider.AbstractQuestProvider;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class MayorQuestProvider extends AbstractQuestProvider<MayorQuest> {

    public MayorQuestProvider(ResourceLocation identifier, List<ConditionProvider<?>> conditions, List<TaskProvider<?>> tasks, RewardDistributor distributor) {
        super(identifier, conditions, tasks, distributor);
    }

    @Override
    public QuestType<MayorQuest, ?> getType() {
        return null;
    }

    @Override
    public MayorQuest createQuest() {
        return new MayorQuest(this);
    }
}
