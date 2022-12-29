package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionProvider;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.trigger.Events;
import dev.toma.questing.common.trigger.ResponseType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueMobKillsCondition extends ConditionProvider<UniqueMobKillsCondition.Instance> {

    public static final Codec<UniqueMobKillsCondition> CODEC = Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name)
            .optionalFieldOf("onFail", ResponseType.PASS).codec()
            .xmap(UniqueMobKillsCondition::new, ConditionProvider::getDefaultFailureResponse);

    public UniqueMobKillsCondition(ResponseType defaultFailureResponse) {
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

        private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UniqueMobKillsCondition.CODEC.fieldOf("provider").forGetter(t -> (UniqueMobKillsCondition) t.getProvider()),
                ModUtils.registryObjectCodec(ForgeRegistries.ENTITIES).listOf().fieldOf("entities").forGetter(t -> new ArrayList<>(t.entityTypes))
        ).apply(instance, Instance::new));
        private final Set<EntityType<?>> entityTypes;

        public Instance(UniqueMobKillsCondition provider) {
            super(provider);
            this.entityTypes = new HashSet<>();
        }

        private Instance(UniqueMobKillsCondition provider, List<EntityType<?>> list) {
            super(provider);
            this.entityTypes = new HashSet<>(list);
        }

        @Override
        public Codec<? extends Condition> codec() {
            return CODEC;
        }

        @Override
        public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
            conditionRegisterHandler.registerWithHandler(Events.DEATH_EVENT, (event, quest) -> {
                DamageSource source = event.getSource();
                Entity origin = source.getEntity();
                if (checkIfEntityIsPartyMember(origin, quest.getParty())) {
                    Entity victim = event.getEntity();
                    return this.entityTypes.contains(victim.getType()) ? this.getProvider().getDefaultFailureResponse() : ResponseType.OK;
                }
                return ResponseType.SKIP;
            }, (event, quest) -> {
                Entity victim = event.getEntity();
                EntityType<?> type = victim.getType();
                this.entityTypes.add(type);
            });
        }
    }
}
