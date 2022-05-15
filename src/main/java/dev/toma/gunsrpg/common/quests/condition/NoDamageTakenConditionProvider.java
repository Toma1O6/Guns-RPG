package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoDamageTakenConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final ITextComponent descriptor;

    protected NoDamageTakenConditionProvider(QuestConditionProviderType<? extends NoDamageTakenConditionProvider> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        float initialHealth = reader.getProperty(QuestProperties.HEALTH_STATUS);
        return player.getHealth() >= initialHealth;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
