package dev.toma.gunsrpg.integration.questing.condition.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.condition.instance.TieredCondition;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.provider.AbstractDefaultConditionProvider;
import dev.toma.questing.common.component.condition.provider.ConditionProvider;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.instance.Quest;
import dev.toma.questing.utils.Codecs;

public class TieredConditionProvider extends AbstractDefaultConditionProvider<TieredCondition> {

    public static final Codec<TieredConditionProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.enumCodecComap(ResponseType.class, ResponseType::fromString, Enum::name, String::toUpperCase).optionalFieldOf("onFail", ResponseType.PASS).forGetter(TieredConditionProvider::getDefaultFailureResponse),
            ConditionType.PROVIDER_CODEC.fieldOf("condition").forGetter(t -> t.conditionProvider),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("tier", 1).forGetter(t -> t.tier)
    ).apply(instance, TieredConditionProvider::new));
    private final ConditionProvider<?> conditionProvider;
    private final int tier;

    public TieredConditionProvider(ResponseType defaultResponseType, ConditionProvider<?> provider, int tier) {
        super(defaultResponseType);
        this.conditionProvider = provider;
        this.tier = tier;
    }

    @Override
    public TieredCondition createCondition(Quest quest) {
        return new TieredCondition(this, quest);
    }

    @Override
    public ConditionType<TieredCondition, ?> getType() {
        return null;
    }

    public ConditionProvider<?> getNestedProvider() {
        return this.conditionProvider;
    }

    @Override
    public ResponseType getDefaultFailureResponse() {
        return this.conditionProvider instanceof AbstractDefaultConditionProvider ? ((AbstractDefaultConditionProvider<?>) this.conditionProvider).getDefaultFailureResponse() : super.getDefaultFailureResponse();
    }
}
