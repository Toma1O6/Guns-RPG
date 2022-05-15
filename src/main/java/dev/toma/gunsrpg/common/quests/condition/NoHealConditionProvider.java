package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoHealConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final ITextComponent descriptor;

    public NoHealConditionProvider(QuestConditionProviderType<?> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        float healthStat = reader.getProperty(QuestProperties.HEALTH_STATUS);
        float health = player.getHealth();
        return health <= healthStat;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
