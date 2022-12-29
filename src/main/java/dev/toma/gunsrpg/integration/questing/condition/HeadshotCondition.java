package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionProvider;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.trigger.Events;
import dev.toma.questing.common.trigger.ResponseType;
import dev.toma.questing.common.trigger.event.DeathEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class HeadshotCondition extends ConditionProvider<HeadshotCondition.Instance> {

    public static final Codec<HeadshotCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name).optionalFieldOf("onFail", ResponseType.PASS).forGetter(ConditionProvider::getDefaultFailureResponse),
            Codec.BOOL.optionalFieldOf("headshot", true).forGetter(t -> t.headshot)
    ).apply(instance, HeadshotCondition::new));
    private final boolean headshot;

    public HeadshotCondition(ResponseType defaultFailureResponse, boolean headshot) {
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

        private static final Codec<Instance> CODEC = HeadshotCondition.CODEC
                .xmap(Instance::new, t -> (HeadshotCondition) t.getProvider())
                .fieldOf("provider").codec();

        public Instance(HeadshotCondition provider) {
            super(provider);
        }

        @Override
        public Codec<? extends Condition> codec() {
            return CODEC;
        }

        @Override
        public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
            conditionRegisterHandler.register(Events.DAMAGE_EVENT, this::getResponse);
            conditionRegisterHandler.register(Events.DEATH_EVENT, this::getResponse);
        }

        private ResponseType getResponse(DeathEvent event, Quest quest) {
            DamageSource source = event.getSource();
            Entity origin = source.getEntity();
            Entity projectile = source.getDirectEntity();
            if (checkIfEntityIsPartyMember(origin, quest.getParty())) {
                if (!(projectile instanceof AbstractProjectile)) {
                    return this.getProvider().getDefaultFailureResponse();
                }
                AbstractProjectile abstractProjectile = (AbstractProjectile) projectile;
                boolean isHeadshot = abstractProjectile.getProperty(Properties.IS_HEADSHOT);
                boolean requiredStatus = ((HeadshotCondition) this.getProvider()).headshot;
                return isHeadshot != requiredStatus ? this.getProvider().getDefaultFailureResponse() : ResponseType.OK;
            }
            return ResponseType.SKIP;
        }
    }
}
