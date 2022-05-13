package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;

public class NoDamageTakenConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    protected NoDamageTakenConditionProvider(QuestConditionProviderType<? extends NoDamageTakenConditionProvider> type) {
        super(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        float initialHealth = reader.getProperty(QuestProperties.HEALTH_STATUS);
        return player.getHealth() >= initialHealth;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
