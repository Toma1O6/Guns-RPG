package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;

public class NoFoodConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    public NoFoodConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        float foodStat = reader.getProperty(QuestProperties.FOOD_STATUS);
        FoodStats stats = player.getFoodData();
        return stats.getFoodLevel() <= foodStat;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
