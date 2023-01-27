package dev.toma.gunsrpg.integration.questing.condition.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.Tiered;
import dev.toma.gunsrpg.integration.questing.condition.provider.TieredConditionProvider;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.instance.Condition;
import dev.toma.questing.common.component.condition.instance.EmptyCondition;
import dev.toma.questing.common.party.Party;
import dev.toma.questing.common.quest.ConditionRegisterHandler;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.world.World;

public class TieredCondition implements Condition, Tiered {

    public static final Codec<TieredCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TieredConditionProvider.CODEC.fieldOf("provider").forGetter(TieredCondition::getProvider),
            ConditionType.CONDITION_CODEC.fieldOf("condition").forGetter(t -> t.condition),
            Codec.INT.fieldOf("tier").forGetter(t -> t.tier)
    ).apply(instance, TieredCondition::new));
    private final TieredConditionProvider provider;
    private final Condition condition;
    private final int tier;

    public TieredCondition(TieredConditionProvider provider, Condition condition, int tier) {
        this.provider = provider;
        this.condition = condition;
        this.tier = EmptyCondition.isEmpty(condition) ? 0 : tier;
    }

    @Override
    public void onConditionConstructing(Party party, Quest quest, World world) {
        this.condition.onConditionConstructing(party, quest, world);
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public TieredConditionProvider getProvider() {
        return provider;
    }

    @Override
    public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
        this.condition.registerTriggerResponders(conditionRegisterHandler);
    }
}
