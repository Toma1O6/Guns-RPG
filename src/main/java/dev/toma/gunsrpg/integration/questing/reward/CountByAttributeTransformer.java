package dev.toma.gunsrpg.integration.questing.reward;

import com.google.gson.JsonObject;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.resource.crafting.OutputModifier;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.RewardTransformer;
import dev.toma.questing.reward.RewardTransformerType;
import net.minecraft.entity.player.PlayerEntity;

public class CountByAttributeTransformer implements RewardTransformer<Integer> {

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

    public static final class Serializer implements RewardTransformerType.Serializer<Integer, CountByAttributeTransformer> {

        @Override
        public CountByAttributeTransformer transformerFromJson(JsonObject data) {
            OutputModifier modifier = OutputModifier.fromJson(data);
            return new CountByAttributeTransformer(modifier);
        }
    }
}
