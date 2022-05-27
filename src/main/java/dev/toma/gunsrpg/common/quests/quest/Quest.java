package dev.toma.gunsrpg.common.quests.quest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.infobar.IDataModel;
import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionProviderType;
import dev.toma.gunsrpg.common.quests.condition.QuestConditions;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardList;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.common.quests.trigger.*;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public abstract class Quest<D extends IQuestData> {

    private final Multimap<Trigger, TriggerContext> triggerListeners = ArrayListMultimap.create();
    private final QuestScheme<D> scheme;
    private final IQuestCondition[] conditions;
    private final int rewardTier;
    private final UUID uuid;
    @OnlyIn(Dist.CLIENT)
    private final LazyOptional<IDataModel> displayModel = LazyOptional.of(this::buildDataModel);

    protected PlayerEntity player;
    protected QuestReward reward;
    protected QuestStatus status = QuestStatus.CREATED;

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

        registerAllTriggers();
    }

    public Quest(QuestDeserializationContext<D> context) {
        this.scheme = context.getScheme();
        this.uuid = context.getTraderId();
        this.conditions = context.getConditions();
        this.rewardTier = context.getRewardTier();
        QuestReward reward = context.getReward();
        if (reward != null) {
            this.reward = reward;
        }
        CompoundNBT nbt = context.getInternalData();
        readQuestData(nbt);

        registerAllTriggers();
    }

    public abstract void registerTriggers(ITriggerRegistration registration);

    public UUID getQuestInitiator() {
        return uuid;
    }

    public D getActiveData() {
        return scheme.getData();
    }

    public void assign(PlayerEntity player) {
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

    }

    public void onFailed(PlayerEntity player) {

    }

    public void trigger(Trigger trigger, IPropertyReader reader) {
        Collection<TriggerContext> contexts = triggerListeners.get(trigger);
        if (contexts != null) {
            TriggerResponseStatus response = TriggerResponseStatus.OK;
            for (TriggerContext context : contexts) {
                TriggerResponseStatus status = context.getResponse(trigger, reader);
                if (status.ordinal() > response.ordinal()) {
                    response = status;
                }
            }
            switch (response) {
                case OK:
                    for (TriggerContext context : contexts) {
                        context.handleSuccess(trigger, reader);
                    }
                    break;
                case FAIL:
                    setStatus(QuestStatus.FAILED);
                    break;
            }
            switch (status) {
                case COMPLETED:
                    break;
                case FAILED:
                    break;
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
        CompoundNBT internalData = new CompoundNBT();
        this.writeQuestData(internalData);
        nbt.put("internalData", internalData);
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

    public LazyOptional<IDataModel> getDisplayModel() {
        return displayModel;
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

    protected void writeQuestData(CompoundNBT nbt) {

    }

    protected void readQuestData(CompoundNBT nbt) {

    }

    protected void trySyncClient() {
        if (player == null) return;
        PlayerData.get(player).ifPresent(data -> data.sync(DataFlags.QUESTS));
    }

    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this);
    }

    private IDataModel buildDataModel() {
        QuestDisplayDataModel dataModel = new QuestDisplayDataModel();
        this.fillDataModel(dataModel);
        return dataModel;
    }

    private void registerAllTriggers() {
        for (IQuestCondition condition : conditions) {
            QuestConditionProviderType<?> conditionType = condition.getProviderType().getType();
            Set<Trigger> triggerSet = conditionType.getTriggerSet();
            for (Trigger trigger : triggerSet) {
                ITriggerResponder responder = (trig, reader) -> handleQuestCondition(conditionType, condition, reader);
                TriggerContext context = TriggerContext.make(responder, ITriggerHandler.PASS);
                triggerListeners.put(trigger, context);
            }
        }
        this.registerTriggers((trigger, responder, handler) -> triggerListeners.put(trigger, TriggerContext.make(responder, handler)));
    }

    private TriggerResponseStatus handleQuestCondition(QuestConditionProviderType<?> type, IQuestCondition condition, IPropertyReader reader) {
        boolean wasValid = condition.isValid(player, reader);
        return wasValid ? TriggerResponseStatus.OK : type.shouldFailQuest() ? TriggerResponseStatus.FAIL : TriggerResponseStatus.PASS;
    }

    public interface ITriggerRegistration {
        void addEntry(Trigger trigger, ITriggerResponder listener, ITriggerHandler handler);
    }
}
