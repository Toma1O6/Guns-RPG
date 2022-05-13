package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;

public class NoHealConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    public NoHealConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        float healthStat = reader.getProperty(QuestProperties.HEALTH_STATUS);
        float health = player.getHealth();
        return health <= healthStat;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
