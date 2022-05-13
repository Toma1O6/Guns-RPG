package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public class HasAggroConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final boolean status;

    protected HasAggroConditionProvider(QuestConditionProviderType<HasAggroConditionProvider> type, boolean status) {
        super(type);
        this.status = status;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        LivingEntity entity = reader.getProperty(QuestProperties.ENTITY);
        if (entity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) entity;
            LivingEntity target = mobEntity.getTarget();
            return status == (target == player);
        }
        return true;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }

    public static final class Serializer implements IQuestConditionProviderSerializer<HasAggroConditionProvider> {

        @Override
        public HasAggroConditionProvider deserialize(QuestConditionProviderType<HasAggroConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            boolean status = JSONUtils.getAsBoolean(object, "aggro", true);
            return new HasAggroConditionProvider(conditionType, status);
        }
    }
}
