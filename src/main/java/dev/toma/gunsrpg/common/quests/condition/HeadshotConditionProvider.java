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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class HeadshotConditionProvider extends AbstractQuestConditionProvider<HeadshotConditionProvider> implements IQuestCondition {

    private final boolean requireHeadshots;
    private final ITextComponent[] descriptors;

    public HeadshotConditionProvider(QuestConditionProviderType<? extends HeadshotConditionProvider> type, boolean requireHeadshots) {
        super(type);
        this.requireHeadshots = requireHeadshots;
        this.descriptors = new ITextComponent[] {
                new TranslationTextComponent(this.getLocalizationString() + ".false"),
                new TranslationTextComponent(this.getLocalizationString() + ".true")
        };
    }

    public static HeadshotConditionProvider fromNbt(QuestConditionProviderType<HeadshotConditionProvider> type, CompoundNBT nbt) {
        boolean requireHeadshots = nbt.getBoolean("headshots");
        return new HeadshotConditionProvider(type, requireHeadshots);
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptors[requireHeadshots ? 1 : 0];
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
    public HeadshotConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        nbt.putBoolean("headshots", requireHeadshots);
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
