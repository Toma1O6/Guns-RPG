package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoDamageGivenConditionProvider extends AbstractQuestConditionProvider<NoDamageGivenConditionProvider> implements IQuestCondition {

    private final ITextComponent descriptor;

    public NoDamageGivenConditionProvider(QuestConditionProviderType<? extends NoDamageGivenConditionProvider> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    public static NoDamageGivenConditionProvider fromNbt(QuestConditionProviderType<NoDamageGivenConditionProvider> type, CompoundNBT nbt) {
        return new NoDamageGivenConditionProvider(type);
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
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
