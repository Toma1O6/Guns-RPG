package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;

public class NoDamageGivenConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    public NoDamageGivenConditionProvider(QuestConditionProviderType<? extends NoDamageGivenConditionProvider> type) {
        super(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        return false;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
