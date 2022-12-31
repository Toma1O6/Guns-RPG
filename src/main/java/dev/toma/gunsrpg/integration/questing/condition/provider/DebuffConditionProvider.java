package dev.toma.gunsrpg.integration.questing.condition.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.condition.instance.DebuffCondition;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.provider.AbstractDefaultConditionProvider;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.Quest;

public class DebuffConditionProvider extends AbstractDefaultConditionProvider<DebuffCondition> {

    public static final Codec<DebuffConditionProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name).optionalFieldOf("onFail", ResponseType.PASS).forGetter(AbstractDefaultConditionProvider::getDefaultFailureResponse),
            ModUtils.registryObjectCodec(ModRegistries.DEBUFFS).fieldOf("debuff").forGetter(t -> t.debuff),
            Codec.BOOL.optionalFieldOf("status", true).forGetter(t -> t.status)
    ).apply(instance, DebuffConditionProvider::new));
    private final DebuffType<?> debuff;
    private final boolean status;

    public DebuffConditionProvider(ResponseType defaultFailureResponse, DebuffType<?> debuff, boolean status) {
        super(defaultFailureResponse);
        this.debuff = debuff;
        this.status = status;
    }

    @Override
    public DebuffCondition createCondition(Quest quest) {
        return new DebuffCondition(this);
    }

    @Override
    public ConditionType<DebuffCondition, ?> getType() {
        return QuestRegistry.DEBUFF_CONDITION;
    }

    public DebuffType<?> getDebuff() {
        return debuff;
    }

    public boolean mustBeActive() {
        return status;
    }
}
