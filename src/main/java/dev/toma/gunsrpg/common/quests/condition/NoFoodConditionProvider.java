package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoFoodConditionProvider extends AbstractQuestConditionProvider<NoFoodConditionProvider> implements IQuestCondition {

    private final ITextComponent[] descriptors;

    public NoFoodConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
        this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString()));
    }

    public static NoFoodConditionProvider fromNbt(QuestConditionProviderType<NoFoodConditionProvider> type, CompoundNBT nbt) {
        return new NoFoodConditionProvider(type);
    }

    @Override
    public boolean isValid(QuestingGroup group, IPropertyReader reader) {
        Quest.PlayerDataAccess access = reader.getProperty(QuestProperties.ACCESS);
        PlayerEntity player = reader.getProperty(QuestProperties.PLAYER);
        int foodStat = access.get(player, QuestProperties.FOOD_STATUS);
        FoodStats stats = player.getFoodData();
        return stats.getFoodLevel() <= foodStat;
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return descriptors[shortDesc ? 1 : 0];
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
