package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoFoodConditionProvider extends AbstractQuestConditionProvider<NoFoodConditionProvider> implements IQuestCondition {

    private final ITextComponent descriptor;

    public NoFoodConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    public static NoFoodConditionProvider fromNbt(QuestConditionProviderType<NoFoodConditionProvider> type, CompoundNBT nbt) {
        return new NoFoodConditionProvider(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        int foodStat = reader.getProperty(QuestProperties.FOOD_STATUS);
        FoodStats stats = player.getFoodData();
        return stats.getFoodLevel() <= foodStat;
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public NoFoodConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }
}
