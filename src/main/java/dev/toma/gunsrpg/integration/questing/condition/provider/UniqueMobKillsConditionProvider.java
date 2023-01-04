package dev.toma.gunsrpg.integration.questing.condition.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.condition.instance.UniqueMobKillsCondition;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.provider.AbstractDefaultConditionProvider;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueMobKillsConditionProvider extends AbstractDefaultConditionProvider<UniqueMobKillsCondition> {

    public static final Codec<UniqueMobKillsConditionProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.comapFlatMap(ResponseType::fromString, Enum::name).optionalFieldOf("onFail", ResponseType.PASS).forGetter(AbstractDefaultConditionProvider::getDefaultFailureResponse),
            ModUtils.registryObjectCodec(ForgeRegistries.ENTITIES).listOf().xmap(HashSet::new, ArrayList::new).optionalFieldOf("entityList", new HashSet<>()).forGetter(t -> t.entityTypes)
    ).apply(instance, UniqueMobKillsConditionProvider::new));

    private final HashSet<EntityType<?>> entityTypes;

    public UniqueMobKillsConditionProvider(ResponseType defaultFailureResponse) {
        super(defaultFailureResponse);
        this.entityTypes = new HashSet<>();
    }

    public UniqueMobKillsConditionProvider(ResponseType defaultFailureResponse, HashSet<EntityType<?>> entityTypes) {
        this(defaultFailureResponse);
        this.entityTypes.addAll(entityTypes);
    }

    @Override
    public ConditionType<UniqueMobKillsCondition, ?> getType() {
        return QuestRegistry.UNIQUE_MOBS_CONDITION;
    }

    @Override
    public UniqueMobKillsCondition createCondition(Quest quest) {
        return new UniqueMobKillsCondition(this);
    }
}
