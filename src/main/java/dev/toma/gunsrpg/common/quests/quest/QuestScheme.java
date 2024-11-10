package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public final class QuestScheme<D extends IQuestData> {

    private final ResourceLocation questId;
    private final QuestType<D, ?> questType;
    private final D data;
    private final int tier;
    private final boolean isSpecialTaskQuest;
    private final DisplayInfo displayInfo;
    private final IQuestConditionProvider<?>[] questConditions;
    private final QuestConditionTierScheme conditionTierScheme;

    public QuestScheme(ResourceLocation questId, QuestType<D, ?> questType, D data, int tier, boolean isSpecialTaskQuest, DisplayInfo displayInfo, IQuestConditionProvider<?>[] questConditions, QuestConditionTierScheme conditionTierScheme) {
        this.questId = questId;
        this.questType = questType;
        this.data = data;
        this.tier = tier;
        this.isSpecialTaskQuest = isSpecialTaskQuest;
        this.displayInfo = displayInfo;
        this.questConditions = questConditions;
        this.conditionTierScheme = conditionTierScheme;
    }

    public static <D extends IQuestData> QuestScheme<D> read(CompoundNBT nbt) {
        ResourceLocation questId = new ResourceLocation(nbt.getString("questId"));
        ResourceLocation questTypeId = new ResourceLocation(nbt.getString("questType"));
        QuestType<D, ?> questType = QuestTypes.getTypeById(questTypeId);
        D data = questType.readData(nbt.getCompound("questData"));
        int tier = nbt.getInt("tier");
        boolean isSpecialTask = nbt.getBoolean("special");
        DisplayInfo displayInfo = DisplayInfo.create(questId);
        return new QuestScheme<>(questId, questType, data, tier, isSpecialTask, displayInfo, new IQuestConditionProvider[0], QuestConditionTierScheme.EMPTY_SCHEME);
    }

    public ResourceLocation getQuestId() {
        return questId;
    }

    public QuestType<D, ?> getQuestType() {
        return questType;
    }

    public D getData() {
        return data;
    }

    public int getTier() {
        return tier;
    }

    public boolean isSpecialTaskQuest() {
        return isSpecialTaskQuest;
    }

    public DisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public IQuestConditionProvider<?>[] getQuestConditions() {
        return questConditions;
    }

    public QuestConditionTierScheme getConditionTierScheme() {
        return conditionTierScheme;
    }

    // needs to serialize only client related data
    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("questId", questId.toString());
        nbt.putString("questType", questType.getId().toString());
        nbt.putInt("tier", tier);
        nbt.putBoolean("special", isSpecialTaskQuest);
        nbt.put("questData", questType.writeData(data));
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestScheme<?> that = (QuestScheme<?>) o;
        return getQuestId().equals(that.getQuestId());
    }

    @Override
    public int hashCode() {
        return getQuestId().hashCode();
    }
}
