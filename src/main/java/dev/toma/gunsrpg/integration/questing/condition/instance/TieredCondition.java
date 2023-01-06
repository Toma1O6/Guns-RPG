package dev.toma.gunsrpg.integration.questing.condition.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.condition.provider.TieredConditionProvider;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.instance.Condition;
import dev.toma.questing.common.party.Party;
import dev.toma.questing.common.quest.ConditionRegisterHandler;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.world.World;

public class TieredCondition implements Condition {

    public static final Codec<TieredCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TieredConditionProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            ConditionType.CONDITION_CODEC.fieldOf("condition").forGetter(t -> t.condition)
    ).apply(instance, TieredCondition::new));
    private final TieredConditionProvider provider;
    private final Condition condition;

    private TieredCondition(TieredConditionProvider provider, Condition condition) {
        this.provider = provider;
        this.condition = condition;
    }

    public TieredCondition(TieredConditionProvider provider, Quest quest) {
        this(provider, provider.getNestedProvider().createCondition(quest));
    }

    @Override
    public TieredConditionProvider getProvider() {
        return provider;
    }

    @Override
    public void onConditionConstructing(Party party, Quest quest, World world) {
        this.condition.onConditionConstructing(party, quest, world);
    }

    @Override
    public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
        this.condition.registerTriggerResponders(conditionRegisterHandler);
    }
}
