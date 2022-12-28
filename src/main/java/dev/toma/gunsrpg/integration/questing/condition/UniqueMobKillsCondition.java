package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionProvider;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.trigger.TriggerResponse;
import net.minecraft.world.World;

public class UniqueMobKillsCondition extends ConditionProvider<UniqueMobKillsCondition.Instance> {

    public static final Codec<UniqueMobKillsCondition> CODEC = Codec.STRING.comapFlatMap(TriggerResponse::fromString, Enum::name)
            .optionalFieldOf("onFail", TriggerResponse.PASS).codec()
            .xmap(UniqueMobKillsCondition::new, ConditionProvider::getDefaultFailureResponse);

    public UniqueMobKillsCondition(TriggerResponse defaultFailureResponse) {
        super(defaultFailureResponse);
    }

    @Override
    public ConditionType<?> getType() {
        return QuestRegistry.UNIQUE_MOBS_CONDITION;
    }

    @Override
    public Instance createConditionInstance(World world, Quest quest) {
        return new Instance(this);
    }

    static final class Instance extends Condition {

        public Instance(UniqueMobKillsCondition provider) {
            super(provider);
        }

        @Override
        public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {

        }
    }
}
