package dev.toma.gunsrpg.integration.questing.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.questing.common.condition.AbstractDefaultCondition;
import dev.toma.questing.common.condition.Condition;
import dev.toma.questing.common.condition.ConditionRegisterHandler;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.trigger.Events;
import dev.toma.questing.common.trigger.ResponseType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueMobKillsCondition extends AbstractDefaultCondition {

    public static final Codec<UniqueMobKillsCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name).optionalFieldOf("onFail", ResponseType.PASS).forGetter(AbstractDefaultCondition::getDefaultFailureResponse),
            ModUtils.registryObjectCodec(ForgeRegistries.ENTITIES).listOf().xmap(HashSet::new, ArrayList::new).optionalFieldOf("entityList", new HashSet<>()).forGetter(t -> t.entityTypes)
    ).apply(instance, UniqueMobKillsCondition::new));

    private final HashSet<EntityType<?>> entityTypes;

    public UniqueMobKillsCondition(ResponseType defaultFailureResponse) {
        super(defaultFailureResponse);
        this.entityTypes = new HashSet<>();
    }

    public UniqueMobKillsCondition(ResponseType defaultFailureResponse, HashSet<EntityType<?>> entityTypes) {
        this(defaultFailureResponse);
        this.entityTypes.addAll(entityTypes);
    }

    @Override
    public ConditionType<?> getType() {
        return QuestRegistry.UNIQUE_MOBS_CONDITION;
    }

    @Override
    public Condition copy() {
        return new UniqueMobKillsCondition(this.getDefaultFailureResponse(), new HashSet<>(this.entityTypes));
    }

    @Override
    public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
        conditionRegisterHandler.registerWithHandler(Events.DEATH_EVENT, (event, quest) -> {
            DamageSource source = event.getSource();
            Entity origin = source.getEntity();
            if (Condition.checkIfEntityIsPartyMember(origin, quest.getParty())) {
                Entity victim = event.getEntity();
                return this.entityTypes.contains(victim.getType()) ? this.getDefaultFailureResponse() : ResponseType.OK;
            }
            return ResponseType.SKIP;
        }, (event, quest) -> {
            Entity victim = event.getEntity();
            EntityType<?> type = victim.getType();
            this.entityTypes.add(type);
        });
    }
}
