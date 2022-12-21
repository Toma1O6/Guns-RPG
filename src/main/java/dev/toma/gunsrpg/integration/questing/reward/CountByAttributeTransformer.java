package dev.toma.gunsrpg.integration.questing.reward;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.resource.crafting.OutputModifier;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.reward.RewardTransformer;
import dev.toma.questing.common.reward.RewardTransformerType;
import net.minecraft.entity.player.PlayerEntity;

public class CountByAttributeTransformer implements RewardTransformer<Integer> {

    public static final Codec<CountByAttributeTransformer> CODEC = OutputModifier.CODEC
            .xmap(CountByAttributeTransformer::new, t -> t.modifier)
            .fieldOf("modifier")
            .codec();
    private final OutputModifier modifier;

    public CountByAttributeTransformer(OutputModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public Integer adjust(Integer originalValue, PlayerEntity player, Quest quest) {
        return PlayerData.get(player).map(data -> {
            IAttributeProvider provider = data.getAttributes();
            return (int) Math.round(this.modifier.getModifiedValue(provider, originalValue));
        }).orElse(originalValue);
    }

    @Override
    public RewardTransformerType<?, ?> getType() {
        return QuestRegistry.COUNT_BY_ATTRIBUTE_TRANSFORMER;
    }
}
