package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionProvider;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.trigger.Events;
import dev.toma.questing.common.trigger.ResponseType;
import dev.toma.questing.common.trigger.event.DeathEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class DebuffCondition extends ConditionProvider<DebuffCondition.Instance> {

    public static final Codec<DebuffCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name).optionalFieldOf("onFail", ResponseType.PASS).forGetter(ConditionProvider::getDefaultFailureResponse),
            ModUtils.registryObjectCodec(ModRegistries.DEBUFFS).fieldOf("debuff").forGetter(t -> t.debuff),
            Codec.BOOL.optionalFieldOf("status", true).forGetter(t -> t.status)
    ).apply(instance, DebuffCondition::new));
    private final DebuffType<?> debuff;
    private final boolean status;

    public DebuffCondition(ResponseType defaultFailureResponse, DebuffType<?> debuff, boolean status) {
        super(defaultFailureResponse);
        this.debuff = debuff;
        this.status = status;
    }

    @Override
    public ConditionType<?> getType() {
        return QuestRegistry.DEBUFF_CONDITION;
    }

    @Override
    public Instance createConditionInstance(World world, Quest quest) {
        return new Instance(this);
    }

    static final class Instance extends Condition {

        private static final Codec<Instance> CODEC = DebuffCondition.CODEC
                .xmap(Instance::new, t -> (DebuffCondition) t.getProvider())
                .fieldOf("provider").codec();

        public Instance(DebuffCondition provider) {
            super(provider);
        }

        @Override
        public Codec<? extends Condition> codec() {
            return CODEC;
        }

        @Override
        public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
            conditionRegisterHandler.register(Events.DEATH_EVENT, this::handleResponse);
            conditionRegisterHandler.register(Events.DAMAGE_EVENT, this::handleResponse);
        }

        private ResponseType handleResponse(DeathEvent event, Quest quest) {
            DamageSource source = event.getSource();
            Entity origin = source.getEntity();
            if (checkIfEntityIsPartyMember(origin, quest.getParty())) {
                PlayerEntity player = (PlayerEntity) origin;
                return PlayerData.get(player).map(data -> {
                    IDebuffs debuffs = data.getDebuffControl();
                    DebuffCondition condition = (DebuffCondition) this.getProvider();
                    boolean hasDebuff = debuffs.hasDebuff(condition.debuff);
                    return hasDebuff != condition.status ? condition.getDefaultFailureResponse() : ResponseType.OK;
                }).orElse(ResponseType.SKIP);
            }
            return ResponseType.SKIP;
        }
    }
}
