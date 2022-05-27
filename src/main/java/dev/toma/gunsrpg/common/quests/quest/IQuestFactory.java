package dev.toma.gunsrpg.common.quests.quest;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IQuestFactory<D extends IQuestData, Q extends Quest<D>> {

    Q makeQuestInstance(QuestScheme<D> scheme, UUID traderId);

    Q questFromContext(QuestDeserializationContext<D> context);

    static <D extends IQuestData, Q extends Quest<D>> IQuestFactory<D, Q> of(BiFunction<QuestScheme<D>, UUID, Q> instanceFactory, Function<QuestDeserializationContext<D>, Q> deserializer) {
        return new IQuestFactory<D, Q>() {
            @Override
            public Q makeQuestInstance(QuestScheme<D> scheme, UUID traderId) {
                return instanceFactory.apply(scheme, traderId);
            }

            @Override
            public Q questFromContext(QuestDeserializationContext<D> context) {
                return deserializer.apply(context);
            }
        };
    }
}
