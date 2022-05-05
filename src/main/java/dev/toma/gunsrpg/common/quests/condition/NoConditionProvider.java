package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;

public class NoConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    public static final NoConditionProvider NO_CONDITION = new NoConditionProvider(QuestConditions.NO_CONDITION_TYPE);

    private NoConditionProvider(QuestConditionProviderType<NoConditionProvider> type) {
        super(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        return true;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }

    public static final class Serializer implements IQuestConditionProviderSerializer<NoConditionProvider> {

        @Override
        public NoConditionProvider deserialize(QuestConditionProviderType<NoConditionProvider> type, JsonElement data) {
            return NO_CONDITION;
        }
    }
}
