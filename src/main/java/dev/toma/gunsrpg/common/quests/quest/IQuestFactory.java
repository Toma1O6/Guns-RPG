package dev.toma.gunsrpg.common.quests.quest;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.function.Function;

public interface IQuestFactory<D extends IQuestData, Q extends Quest<D>> {

    Q makeQuestInstance(InstanceContext<D, Q> ctx);

    Q questFromContext(QuestDeserializationContext<D> context);

    static <D extends IQuestData, Q extends Quest<D>> IQuestFactory<D, Q> of(Function<InstanceContext<D, Q>, Q> instanceFactory, Function<QuestDeserializationContext<D>, Q> deserializer) {
        return new IQuestFactory<D, Q>() {
            @Override
            public Q makeQuestInstance(InstanceContext<D, Q> ctx) {
                return instanceFactory.apply(ctx);
            }

            @Override
            public Q questFromContext(QuestDeserializationContext<D> context) {
                return deserializer.apply(context);
            }
        };
    }

    final class InstanceContext<D extends IQuestData, Q extends Quest<D>> {
        private final World world;
        private final QuestScheme<D> scheme;
        private final UUID traderId;
        private final PlayerEntity player;

        public InstanceContext(World world, QuestScheme<D> scheme, UUID traderId, PlayerEntity player) {
            this.world = world;
            this.scheme = scheme;
            this.traderId = traderId;
            this.player = player;
        }

        @SuppressWarnings("unchecked")
        public QuestType<D, Q> getQuestType() {
            return (QuestType<D, Q>) this.scheme.getQuestType();
        }

        public World getWorld() {
            return world;
        }

        public QuestScheme<D> getScheme() {
            return scheme;
        }

        public UUID getTraderId() {
            return traderId;
        }

        public PlayerEntity getPartyLeader() {
            return player;
        }
    }
}
