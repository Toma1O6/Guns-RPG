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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class HasAggroConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final boolean status;
    private final ITextComponent[] descriptors;

    protected HasAggroConditionProvider(QuestConditionProviderType<HasAggroConditionProvider> type, boolean status) {
        super(type);
        this.status = status;
        this.descriptors = new ITextComponent[] { new TranslationTextComponent(this.getLocalizationString() + ".false"), new TranslationTextComponent(this.getLocalizationString() + ".true") };
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptors[status ? 1 : 0];
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
