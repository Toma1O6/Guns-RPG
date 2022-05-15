package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ExplodeConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final ITextComponent descriptor;

    public ExplodeConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        DamageSource source = reader.getProperty(QuestProperties.DAMAGE_SOURCE);
        return source.isExplosion();
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
