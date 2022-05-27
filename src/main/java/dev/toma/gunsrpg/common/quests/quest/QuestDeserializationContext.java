package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.QuestConditions;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public final class QuestDeserializationContext<D extends IQuestData> {

    private final QuestScheme<D> scheme;
    private final UUID traderId;
    private final int rewardTier;
    private final IQuestCondition[] conditions;
    private final QuestReward reward;
    private final QuestStatus status;
    private final CompoundNBT internalData;

    public QuestDeserializationContext(QuestScheme<D> scheme, UUID traderId, int rewardTier, IQuestCondition[] conditions, QuestReward reward, QuestStatus status, CompoundNBT internalData) {
        this.scheme = scheme;
        this.traderId = traderId;
        this.rewardTier = rewardTier;
        this.conditions = conditions;
        this.reward = reward;
        this.status = status;
        this.internalData = internalData;
    }

    public static <D extends IQuestData> QuestDeserializationContext<D> fromNbt(CompoundNBT nbt) {
        QuestScheme<D> scheme = QuestScheme.read(nbt.getCompound("scheme"));
        UUID createdBy = nbt.getUUID("createdBy");
        int rewardTier = nbt.getInt("rewardTier");
        IQuestCondition[] conditions = nbt.getList("conditions", Constants.NBT.TAG_COMPOUND).stream()
                .<IQuestCondition>map(inbt -> QuestConditions.getConditionFromNbt((CompoundNBT) inbt))
                .toArray(IQuestCondition[]::new);
        QuestReward reward = null;
        if (nbt.contains("reward")) {
            CompoundNBT rewardNbt = nbt.getCompound("reward");
            reward = new QuestReward(rewardNbt);
        }
        QuestStatus status = QuestStatus.values()[nbt.getInt("status")];
        CompoundNBT internalData = nbt.getCompound("internalData");
        return new QuestDeserializationContext<>(scheme, createdBy, rewardTier, conditions, reward, status, internalData);
    }

    public QuestScheme<D> getScheme() {
        return scheme;
    }

    public UUID getTraderId() {
        return traderId;
    }

    public int getRewardTier() {
        return rewardTier;
    }

    public IQuestCondition[] getConditions() {
        return conditions;
    }

    public QuestReward getReward() {
        return reward;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public CompoundNBT getInternalData() {
        return internalData;
    }
}
