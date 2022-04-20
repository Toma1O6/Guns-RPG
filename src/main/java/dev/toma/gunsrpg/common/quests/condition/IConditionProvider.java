package dev.toma.gunsrpg.common.quests.condition;

import net.minecraft.util.ResourceLocation;

public interface IConditionProvider {

    IQuestConditionProvider getProvider(ResourceLocation id);
}
