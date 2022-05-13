package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public class HeadshotConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final boolean requireHeadshots;

    public HeadshotConditionProvider(QuestConditionProviderType<? extends HeadshotConditionProvider> type, boolean requireHeadshots) {
        super(type);
        this.requireHeadshots = requireHeadshots;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        Entity entity = reader.getProperty(QuestProperties.DIRECT_ENTITY);
        if (entity instanceof AbstractProjectile) {
            AbstractProjectile projectile = (AbstractProjectile) entity;
            boolean headshot = projectile.getProperty(Properties.IS_HEADSHOT);
            return headshot == requireHeadshots;
        }
        return false;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }

    public static final class Serializer implements IQuestConditionProviderSerializer<HeadshotConditionProvider> {

        @Override
        public HeadshotConditionProvider deserialize(QuestConditionProviderType<HeadshotConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            boolean requireHeadshots = JSONUtils.getAsBoolean(object, "headshot", true);
            return new HeadshotConditionProvider(conditionType, requireHeadshots);
        }
    }
}
