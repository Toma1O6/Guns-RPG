package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class UniqueMobKillsConditionProvider extends AbstractQuestConditionProvider {

    public UniqueMobKillsConditionProvider(QuestConditionProviderType<? extends UniqueMobKillsConditionProvider> type) {
        super(type);
    }

    @Override
    public IQuestCondition getCondition() {
        return new ConditionTracker();
    }

    private static final class ConditionTracker implements IQuestCondition {

        private final Set<EntityType<?>> killedMobs = new HashSet<>();

        @Override
        public boolean isValid(PlayerEntity player, IPropertyReader reader) {
            Entity entity = reader.getProperty(QuestProperties.ENTITY);
            if (entity instanceof MonsterEntity) {
                EntityType<?> type = entity.getType();
                if (!killedMobs.contains(type)) {
                    killedMobs.add(type);
                    return true;
                }
            }
            return false;
        }
    }
}
