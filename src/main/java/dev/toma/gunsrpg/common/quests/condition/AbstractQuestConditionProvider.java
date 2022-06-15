package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractQuestConditionProvider<C extends IQuestCondition> implements IQuestConditionProvider<C> {

    private final QuestConditionProviderType<?> type;

    protected AbstractQuestConditionProvider(QuestConditionProviderType<?> type) {
        this.type = type;
    }

    public String getLocalizationString() {
        return "quest.condition." + ModUtils.convertToLocalization(type.getId());
    }

    public static ITextComponent[] expandWithShortLocalizations(ITextComponent... descriptors) {
        ITextComponent[] results = new ITextComponent[descriptors.length * 2];
        System.arraycopy(descriptors, 0, results, 0, descriptors.length);
        for (int i = 0; i < descriptors.length; i++) {
            int index = descriptors.length + i;
            ITextComponent component;
            ITextComponent descriptor = descriptors[i];
            if (descriptor instanceof TranslationTextComponent) {
                TranslationTextComponent translationTextComponent = (TranslationTextComponent) descriptor;
                component = new TranslationTextComponent(translationTextComponent.getKey(), translationTextComponent.getArgs());
            } else {
                component = new StringTextComponent(descriptor.getString());
            }
            results[index] = component;
        }
        return results;
    }

    @Override
    public QuestConditionProviderType<?> getType() {
        return type;
    }
}
