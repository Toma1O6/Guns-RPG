package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoDamageTakenConditionProvider extends AbstractQuestConditionProvider<NoDamageTakenConditionProvider> implements IQuestCondition {

    private final ITextComponent descriptor;

    protected NoDamageTakenConditionProvider(QuestConditionProviderType<? extends NoDamageTakenConditionProvider> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    public static NoDamageTakenConditionProvider fromNbt(QuestConditionProviderType<NoDamageTakenConditionProvider> type, CompoundNBT nbt) {
        return new NoDamageTakenConditionProvider(type);
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
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
