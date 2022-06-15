package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class NoConditionProvider extends AbstractQuestConditionProvider<NoConditionProvider> implements IQuestCondition {

    public static final NoConditionProvider NO_CONDITION = new NoConditionProvider(QuestConditions.NO_CONDITION_TYPE);
    private static final ITextComponent PLACEHOLDER_TEXT = new StringTextComponent("This will be never displayed").withStyle(TextFormatting.ITALIC);

    private NoConditionProvider(QuestConditionProviderType<NoConditionProvider> type) {
        super(type);
    }

    @Override
    public ITextComponent getDescriptor(boolean shortDesc) {
        return PLACEHOLDER_TEXT;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        return true;
    }

    @Override
    public NoConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }
}
