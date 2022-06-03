package dev.toma.gunsrpg.common.quests;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum ActionType {

    ASSIGN("screen.quests.choose_quest"),
    CANCEL("screen.quests.cancel_quest"),
    COLLECT("screen.quests.claim_rewards");

    private final ITextComponent text;

    ActionType(String localizationKey) {
        this.text = new TranslationTextComponent(localizationKey);
    }

    public ITextComponent getText() {
        return text;
    }
}
