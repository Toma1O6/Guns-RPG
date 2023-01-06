package dev.toma.gunsrpg.integration.questing.condition.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.condition.provider.UniqueMobKillsConditionProvider;
import dev.toma.questing.common.component.condition.instance.Condition;
import dev.toma.questing.common.component.trigger.Events;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.ConditionRegisterHandler;
import dev.toma.questing.utils.Codecs;
import dev.toma.questing.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UniqueMobKillsCondition implements Condition {

    public static final Codec<UniqueMobKillsCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UniqueMobKillsConditionProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            Codecs.forgeRegistryCodec(ForgeRegistries.ENTITIES).listOf()
                    .xmap(list -> (Set<EntityType<?>>) new HashSet<>(list), ArrayList::new)
                    .fieldOf("entityTypes").forGetter(t -> t.types)
    ).apply(instance, UniqueMobKillsCondition::new));
    private final UniqueMobKillsConditionProvider provider;
    private final Set<EntityType<?>> types;

    public UniqueMobKillsCondition(UniqueMobKillsConditionProvider provider) {
        this(provider, new HashSet<>());
    }

    public UniqueMobKillsCondition(UniqueMobKillsConditionProvider provider, Set<EntityType<?>> types) {
        this.provider = provider;
        this.types = types;
    }

    @Override
    public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
        conditionRegisterHandler.registerWithHandler(Events.DEATH_EVENT, (event, level, quest) -> {
            DamageSource source = event.getSource();
            Entity origin = source.getEntity();
            if (Utils.checkIfEntityIsPartyMember(origin, quest.getParty())) {
                Entity victim = event.getEntity();
                return this.types.contains(victim.getType()) ? this.provider.getDefaultFailureResponse() : ResponseType.OK;
            }
            return ResponseType.SKIP;
        }, (event, level, quest) -> {
            Entity victim = event.getEntity();
            EntityType<?> type = victim.getType();
            this.types.add(type);
        });
    }

    @Override
    public UniqueMobKillsConditionProvider getProvider() {
        return provider;
    }
}
