package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;

import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleProviderSerializer<P extends IQuestConditionProvider<?>> implements IQuestConditionProviderSerializer<P> {

    private final Function<QuestConditionProviderType<P>, P> function;

    private SimpleProviderSerializer(Function<QuestConditionProviderType<P>, P> function) {
        this.function = function;
    }

    public static <P extends IQuestConditionProvider<?>> IQuestConditionProviderSerializer<P> withConstantResult(Supplier<P> constant) {
        return withResultOf(type -> constant.get());
    }

    public static <P extends IQuestConditionProvider<?>> IQuestConditionProviderSerializer<P> withResultOf(Function<QuestConditionProviderType<P>, P> function) {
        return new SimpleProviderSerializer<>(function);
    }

    @Override
    public P deserialize(QuestConditionProviderType<P> conditionType, JsonElement data) {
        return function.apply(conditionType);
    }
}
