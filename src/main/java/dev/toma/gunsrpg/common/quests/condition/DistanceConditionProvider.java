package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DistanceConditionProvider extends AbstractQuestConditionProvider<DistanceConditionProvider> implements IQuestCondition {

    private final ITextComponent[] descriptors;
    private final double minDistance;
    private final double maxDistance;

    public DistanceConditionProvider(QuestConditionProviderType<? extends DistanceConditionProvider> type, double minDistance, double maxDistance) {
        super(type);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        boolean isMinDistanceDefined = minDistance > 0;
        boolean isMaxDistanceDefined = maxDistance < Float.MAX_VALUE;
        if (isMinDistanceDefined && isMaxDistanceDefined) {
            this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString(), minDistance, maxDistance));
        } else {
            this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString() + (isMinDistanceDefined ? ".min" : ".max"), isMinDistanceDefined ? minDistance : maxDistance));
        }
    }

    public static DistanceConditionProvider fromNbt(QuestConditionProviderType<DistanceConditionProvider> type, CompoundNBT nbt) {
        double min = nbt.getDouble("min");
        double max = nbt.getDouble("max");
        return new DistanceConditionProvider(type, min, max);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        LivingEntity entity = reader.getProperty(QuestProperties.ENTITY);
        double sqrDist = player.distanceToSqr(entity);
        double distance = Math.sqrt(sqrDist);
        return distance >= minDistance && distance <= maxDistance;
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return descriptors[shortDesc ? 1 : 0];
    }

    @Override
    public DistanceConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        nbt.putDouble("min", minDistance);
        nbt.putDouble("max", maxDistance);
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
