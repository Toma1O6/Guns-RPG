package dev.toma.gunsrpg.common.quests.quest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditions;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardList;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerListener;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class Quest<D extends IQuestData> {

    private final Multimap<Trigger, ITriggerListener> triggerListeners = ArrayListMultimap.create();
    private final QuestScheme<D> scheme;
    private final IQuestCondition[] conditions;
    private final int rewardTier;
    private final UUID uuid;

    private PlayerEntity player;
    private QuestReward reward;
    private boolean active;
    private QuestStatus status = QuestStatus.CREATED;

    public Quest(QuestScheme<D> scheme, UUID traderId) {
        this.scheme = scheme;
        this.uuid = traderId;
        QuestConditionTierScheme tierScheme = scheme.getConditionTierScheme();
        QuestConditionTierScheme.Result result = tierScheme.getModifiedConditions();
        this.rewardTier = scheme.getTier() + result.getTierModifier();
        IQuestCondition[] tieredConditions = result.getConditions();
        IQuestConditionProvider<?>[] schemeConditions = scheme.getQuestConditions();
        IQuestCondition[] allConditions = new IQuestCondition[tieredConditions.length + schemeConditions.length];
        System.arraycopy(tieredConditions, 0, allConditions, 0, tieredConditions.length);
        for (int i = 0; i < schemeConditions.length; i++) {
            IQuestCondition condition = schemeConditions[i].makeConditionInstance();
            allConditions[tieredConditions.length + i] = condition;
        }
        this.conditions = allConditions;
    }

    protected Quest(QuestScheme<D> scheme, UUID traderId, int rewardTier, IQuestCondition[] conditions) {
        this.scheme = scheme;
        this.uuid = traderId;
        this.conditions = conditions;
        this.rewardTier = rewardTier;
    }

    public static <D extends IQuestData> Quest<D> fromNbt(CompoundNBT nbt) {
        QuestScheme<D> scheme = QuestScheme.read(nbt.getCompound("scheme"));
        UUID createdBy = nbt.getUUID("createdBy");
        int rewardTier = nbt.getInt("rewardTier");
        IQuestCondition[] conditions = nbt.getList("conditions", Constants.NBT.TAG_COMPOUND).stream()
                .<IQuestCondition>map(inbt -> QuestConditions.getConditionFromNbt((CompoundNBT) inbt))
                .toArray(IQuestCondition[]::new);
        Quest<D> quest = new Quest<>(scheme, createdBy, rewardTier, conditions);
        if (nbt.contains("reward")) {
            CompoundNBT rewardNbt = nbt.getCompound("reward");
            quest.reward = new QuestReward(rewardNbt);
        }
        quest.status = QuestStatus.values()[nbt.getInt("status")];
        return quest;
    }

    public UUID getQuestInitiator() {
        return uuid;
    }

    public D getActiveData() {
        return scheme.getData();
    }

    public void assign(PlayerEntity player) {
        this.active = true;
        this.player = player;
        if (reward == null) {
            IPlayerData data = PlayerData.getUnsafe(player);
            IAttributeProvider provider = data.getAttributes();
            int rewardChoices = provider.getAttribute(Attribs.QUEST_VISIBLE_REWARD).intValue();
            QuestSystem system = GunsRPG.getModLifecycle().quests();
            QuestRewardManager manager = system.getRewardManager();
            QuestRewardList rewardList = manager.getTieredRewards(this.rewardTier);
            QuestReward.Options options = new QuestReward.Options().items(1).choiceCount(rewardChoices).setUnique();
            this.reward = QuestReward.generate(rewardList, options, player);
        }
    }

    public void onCompleted(PlayerEntity player) {
        active = false;
    }

    public void onFailed(PlayerEntity player) {
        active = false;
    }

    public void trigger(Trigger trigger, IPropertyReader reader) {
        Collection<ITriggerListener> listeners = triggerListeners.get(trigger);
        if (listeners != null) {
            boolean failedChecks = false;
            for (ITriggerListener listener : listeners) {
                TriggerResponseStatus status = listener.handleTriggerEvent(trigger, reader);
                if (status == TriggerResponseStatus.FAIL) {
                    failedChecks = true;
                    break;
                }
            }
            if (!failedChecks) {
                // TODO should be handled by quest logic rather than in data structure
                //questData.handleProgress(trigger, reader);
            }
        }
    }

    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("scheme", scheme.serialize());
        ListNBT conditionList = new ListNBT();
        Arrays.stream(conditions).map(QuestConditions::saveConditionToNbt).forEach(conditionList::add);
        nbt.put("conditions", conditionList);
        nbt.putUUID("createdBy", uuid);
        nbt.putInt("rewardTier", rewardTier);
        if (reward != null) {
            nbt.put("reward", reward.toNbt());
        }
        nbt.putInt("status", status.ordinal());
        return nbt;
    }

    public QuestScheme<D> getScheme() {
        return scheme;
    }

    public IQuestCondition[] getConditions() {
        return conditions;
    }

    public int getRewardTier() {
        return rewardTier;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public QuestReward getReward() {
        return reward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest<?> quest = (Quest<?>) o;
        return getScheme().equals(quest.getScheme());
    }

    @Override
    public int hashCode() {
        return getScheme().hashCode();
    }

    public interface ITriggerRegistration {
        void addEntry(Trigger trigger, ITriggerListener listener);
    }
}
