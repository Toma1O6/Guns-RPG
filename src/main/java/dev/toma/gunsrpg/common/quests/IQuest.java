package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Set;

public interface IQuest {

    ResourceLocation getID();

    Set<IQuestCondition> getConditions();

    int getRewardTier();

    ITextComponent getName();

    ITextComponent getDescription();
}
