package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoDamageGivenConditionProvider extends AbstractQuestConditionProvider<NoDamageGivenConditionProvider> implements IQuestCondition {

    private final ITextComponent[] descriptors;

    public NoDamageGivenConditionProvider(QuestConditionProviderType<? extends NoDamageGivenConditionProvider> type) {
        super(type);
        this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString()));
    }

    public static NoDamageGivenConditionProvider fromNbt(QuestConditionProviderType<NoDamageGivenConditionProvider> type, CompoundNBT nbt) {
        return new NoDamageGivenConditionProvider(type);
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return descriptors[shortDesc ? 1 : 0];
    }

    @Override
    public boolean isValid(QuestingGroup group, IPropertyReader reader) {
        return false;
    }

    @Override
    public NoDamageGivenConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }
}
