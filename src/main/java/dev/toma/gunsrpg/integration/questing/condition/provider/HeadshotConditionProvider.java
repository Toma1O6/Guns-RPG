package dev.toma.gunsrpg.integration.questing.condition.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.condition.instance.HeadshotCondition;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.provider.AbstractDefaultConditionProvider;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.instance.Quest;

public class HeadshotConditionProvider extends AbstractDefaultConditionProvider<HeadshotCondition> {

    public static final Codec<HeadshotConditionProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name).optionalFieldOf("onFail", ResponseType.PASS).forGetter(AbstractDefaultConditionProvider::getDefaultFailureResponse),
            Codec.BOOL.optionalFieldOf("headshot", true).forGetter(t -> t.headshot)
    ).apply(instance, HeadshotConditionProvider::new));
    private final boolean headshot;

    public HeadshotConditionProvider(ResponseType defaultFailureResponse, boolean headshot) {
        super(defaultFailureResponse);
        this.headshot = headshot;
    }

    @Override
    public HeadshotCondition createCondition(Quest quest) {
        return new HeadshotCondition(this);
    }

    @Override
    public ConditionType<HeadshotCondition, ?> getType() {
        return QuestRegistry.HEADSHOT_CONDITION;
    }

    public boolean mustBeHeadshot() {
        return this.headshot;
    }
}
