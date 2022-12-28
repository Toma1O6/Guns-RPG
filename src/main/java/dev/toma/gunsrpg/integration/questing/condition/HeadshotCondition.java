package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionProvider;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.trigger.TriggerResponse;
import net.minecraft.world.World;

public class HeadshotCondition extends ConditionProvider<HeadshotCondition.Instance> {

    public static final Codec<HeadshotCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(TriggerResponse::fromString, Enum::name).optionalFieldOf("onFail", TriggerResponse.PASS).forGetter(ConditionProvider::getDefaultFailureResponse),
            Codec.BOOL.optionalFieldOf("headshot", true).forGetter(t -> t.headshot)
    ).apply(instance, HeadshotCondition::new));
    private final boolean headshot;

    public HeadshotCondition(TriggerResponse defaultFailureResponse, boolean headshot) {
        super(defaultFailureResponse);
        this.headshot = headshot;
    }

    @Override
    public ConditionType<?> getType() {
        return QuestRegistry.HEADSHOT_CONDITION;
    }

    @Override
    public Instance createConditionInstance(World world, Quest quest) {
        return new Instance(this);
    }

    static final class Instance extends Condition {

        public Instance(HeadshotCondition provider) {
            super(provider);
        }

        @Override
        public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {

        }
    }
}
