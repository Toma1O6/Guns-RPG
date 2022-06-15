package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ExplodeConditionProvider extends AbstractQuestConditionProvider<ExplodeConditionProvider> implements IQuestCondition {

    private final ITextComponent[] descriptors;

    public ExplodeConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
        this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(this.getLocalizationString()));
    }

    public static ExplodeConditionProvider fromNbt(QuestConditionProviderType<ExplodeConditionProvider> type, CompoundNBT nbt) {
        return new ExplodeConditionProvider(type);
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        DamageSource source = reader.getProperty(QuestProperties.DAMAGE_SOURCE);
        return source.isExplosion();
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return descriptors[shortDesc ? 1 : 0];
    }

    @Override
    public ExplodeConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }
}
