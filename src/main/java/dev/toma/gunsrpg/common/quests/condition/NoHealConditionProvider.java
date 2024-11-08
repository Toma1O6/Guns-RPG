package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoHealConditionProvider extends AbstractQuestConditionProvider<NoHealConditionProvider> implements IQuestCondition {

    private final ITextComponent[] descriptors;

    public NoHealConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
        this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString()));
    }

    public static NoHealConditionProvider fromNbt(QuestConditionProviderType<NoHealConditionProvider> type, CompoundNBT nbt) {
        return new NoHealConditionProvider(type);
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return descriptors[shortDesc ? 1 : 0];
    }

    @Override
    public boolean isValid(QuestingGroup group, IPropertyReader reader) {
        Quest.PlayerDataAccess access = reader.getProperty(QuestProperties.ACCESS);
        PlayerEntity player = reader.getProperty(QuestProperties.PLAYER);
        float healthStat = access.get(player, QuestProperties.HEALTH_STATUS);
        float health = player.getHealth();
        return health <= healthStat;
    }

    @Override
    public NoHealConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }
}
