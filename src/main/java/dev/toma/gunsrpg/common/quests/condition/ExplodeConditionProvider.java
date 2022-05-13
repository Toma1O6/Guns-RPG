package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

public class ExplodeConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    public ExplodeConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        DamageSource source = reader.getProperty(QuestProperties.DAMAGE_SOURCE);
        return source.isExplosion();
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
