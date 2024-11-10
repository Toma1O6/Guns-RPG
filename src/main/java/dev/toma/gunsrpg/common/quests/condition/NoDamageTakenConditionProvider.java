package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoDamageTakenConditionProvider extends AbstractQuestConditionProvider<NoDamageTakenConditionProvider> implements IQuestCondition {

    private final ITextComponent[] descriptors;

    protected NoDamageTakenConditionProvider(QuestConditionProviderType<? extends NoDamageTakenConditionProvider> type) {
        super(type);
        this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString()));
    }

    public static NoDamageTakenConditionProvider fromNbt(QuestConditionProviderType<NoDamageTakenConditionProvider> type, CompoundNBT nbt) {
        return new NoDamageTakenConditionProvider(type);
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return descriptors[shortDesc ? 1 : 0];
    }

    @Override
    public boolean isValid(QuestingGroup group, IPropertyReader reader) {
        DamageSource source = reader.getProperty(QuestProperties.DAMAGE_SOURCE);
        return source == null;
    }

    @Override
    public NoDamageTakenConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }
}
