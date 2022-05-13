package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public class DistanceConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final double minDistance;
    private final double maxDistance;

    public DistanceConditionProvider(QuestConditionProviderType<? extends DistanceConditionProvider> type, double minDistance, double maxDistance) {
        super(type);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        LivingEntity entity = reader.getProperty(QuestProperties.ENTITY);
        double sqrDist = player.distanceToSqr(entity);
        double distance = Math.sqrt(sqrDist);
        return distance >= minDistance && distance <= maxDistance;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }

    public static final class Serializer implements IQuestConditionProviderSerializer<DistanceConditionProvider> {

        @Override
        public DistanceConditionProvider deserialize(QuestConditionProviderType<DistanceConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            double min = JSONUtils.getAsFloat(object, "min", 0.0F);
            double max = JSONUtils.getAsFloat(object, "max", Float.MAX_VALUE);
            if (min > max) {
                throw new JsonSyntaxException("Min distance cannot be larger than max distance!");
            }
            return new DistanceConditionProvider(conditionType, min, max);
        }
    }
}
