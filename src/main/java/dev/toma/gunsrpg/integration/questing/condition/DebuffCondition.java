package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionProvider;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.trigger.TriggerResponse;
import net.minecraft.world.World;

public class DebuffCondition extends ConditionProvider<DebuffCondition.Instance> {

    public static final Codec<DebuffCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(TriggerResponse::fromString, Enum::name).optionalFieldOf("onFail", TriggerResponse.PASS).forGetter(ConditionProvider::getDefaultFailureResponse),
            ModUtils.registryObjectCodec(ModRegistries.DEBUFFS).fieldOf("debuff").forGetter(t -> t.debuff),
            Codec.BOOL.optionalFieldOf("status", true).forGetter(t -> t.status)
    ).apply(instance, DebuffCondition::new));
    private final DebuffType<?> debuff;
    private final boolean status;

    public DebuffCondition(TriggerResponse defaultFailureResponse, DebuffType<?> debuff, boolean status) {
        super(defaultFailureResponse);
        this.debuff = debuff;
        this.status = status;
    }

    @Override
    public ConditionType<?> getType() {
        return null;
    }

    @Override
    public Instance createConditionInstance(World world, Quest quest) {
        return new Instance(this);
    }

    static final class Instance extends Condition {

        public Instance(DebuffCondition provider) {
            super(provider);
        }

        @Override
        public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {

        }
    }
}
