package dev.toma.gunsrpg.common.quests.condition;

import java.util.Random;

public class TieredCondition implements IQuestConditionProvider {

    private static final Random RANDOM = new Random();
    private final int tierModifier;
    private final float applicationPropability;
    private final IQuestConditionProvider provider;

    public TieredCondition(int tierModifier, float applicationPropability, IQuestConditionProvider provider) {
        this.tierModifier = tierModifier;
        this.applicationPropability = applicationPropability;
        this.provider = provider;
    }

    @Override
    public IQuestCondition getCondition() {
        return applicationPropability == 1.0F || RANDOM.nextFloat() < applicationPropability ? provider.getCondition() : NoConditionProvider.NO_CONDITION;
    }

    @Override
    public QuestConditionProviderType<?> getType() {
        return provider.getType();
    }

    public int getTierModifier() {
        return tierModifier;
    }
}
