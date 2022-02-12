package dev.toma.gunsrpg.resource.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class SkillPointReward implements ILevelReward {

    private final ITransactionValidator validator;
    private final int amount;

    public SkillPointReward(ITransactionValidator validator, int amount) {
        this.validator = validator;
        this.amount = amount;
    }

    @Override
    public void applyTo(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> validator.getData(data).awardPoints(amount));
    }

    public static class Adapter implements ILevelRewardSerializer<SkillPointReward> {

        @Override
        public SkillPointReward resolveFromJson(JsonObject object) throws JsonParseException {
            ResourceLocation target = new ResourceLocation(JSONUtils.getAsString(object, "target"));
            ITransactionValidatorFactory<?, ?> factory = TransactionValidatorRegistry.getValidatorFactory(target);
            if (factory == null) {
                throw new JsonSyntaxException("Unknown reward type: " + target);
            }
            ITransactionValidator validator = TransactionValidatorRegistry.getTransactionValidator(factory, object.get(""));
            return null;
        }
    }
}
