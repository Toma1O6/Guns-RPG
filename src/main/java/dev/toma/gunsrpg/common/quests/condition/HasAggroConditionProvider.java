package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class HasAggroConditionProvider extends AbstractQuestConditionProvider<HasAggroConditionProvider> implements IQuestCondition {

    private final boolean status;
    private final ITextComponent[] descriptors;

    protected HasAggroConditionProvider(QuestConditionProviderType<HasAggroConditionProvider> type, boolean status) {
        super(type);
        this.status = status;
        this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString() + ".false"), new TranslationTextComponent(this.getLocalizationString() + ".true"));
    }

    public static HasAggroConditionProvider fromNbt(QuestConditionProviderType<HasAggroConditionProvider> type, CompoundNBT data) {
        return new HasAggroConditionProvider(type, data.getBoolean("status"));
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        int i = status ? 1 : 0;
        if (shortDesc) {
            i |= 0b10;
        }
        return descriptors[i];
    }

    @Override
    public boolean isValid(QuestingGroup group, IPropertyReader reader) {
        LivingEntity entity = reader.getProperty(QuestProperties.ENTITY);
        if (entity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) entity;
            LivingEntity target = mobEntity.getTarget();
            return this.status && target != null ? group.isMember(target.getUUID()) : target == null;
        }
        return true;
    }

    @Override
    public HasAggroConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        nbt.putBoolean("status", status);
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
