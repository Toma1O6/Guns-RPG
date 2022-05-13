package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.TieredCondition;
import net.minecraft.util.ResourceLocation;

public final class QuestScheme<D extends IQuestData> {

    private final ResourceLocation questId;
    private final QuestType<D> questType;
    private final D data;
    private final int tier;
    private final DisplayInfo displayInfo;
    private final IQuestConditionProvider[] questConditions;
    private final TieredCondition[] tieredConditions;

    public QuestScheme(ResourceLocation questId, QuestType<D> questType, D data, int tier, DisplayInfo displayInfo, IQuestConditionProvider[] questConditions, TieredCondition[] tieredConditions) {
        this.questId = questId;
        this.questType = questType;
        this.data = data;
        this.tier = tier;
        this.displayInfo = displayInfo;
        this.questConditions = questConditions;
        this.tieredConditions = tieredConditions;
    }

    public ResourceLocation getQuestId() {
        return questId;
    }

    public QuestType<D> getQuestType() {
        return questType;
    }

    public D getData() {
        return data;
    }

    public int getTier() {
        return tier;
    }

    public DisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public IQuestConditionProvider[] getQuestConditions() {
        return questConditions;
    }

    public TieredCondition[] getTieredConditions() {
        return tieredConditions;
    }
}
