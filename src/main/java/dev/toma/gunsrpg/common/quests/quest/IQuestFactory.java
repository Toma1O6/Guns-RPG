package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.util.function.TriFunction;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IQuestFactory<D extends IQuestData, Q extends Quest<D>> {

    Q makeQuestInstance(World world, QuestScheme<D> scheme, UUID traderId);

    Q questFromContext(QuestDeserializationContext<D> context);

    static <D extends IQuestData, Q extends Quest<D>> IQuestFactory<D, Q> of(TriFunction<World, QuestScheme<D>, UUID, Q> instanceFactory, Function<QuestDeserializationContext<D>, Q> deserializer) {
        return new IQuestFactory<D, Q>() {
            @Override
            public Q makeQuestInstance(World world, QuestScheme<D> scheme, UUID traderId) {
                return instanceFactory.apply(world, scheme, traderId);
            }

            @Override
            public Q questFromContext(QuestDeserializationContext<D> context) {
                return deserializer.apply(context);
            }
        };
    }
}
