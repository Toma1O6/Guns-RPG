package dev.toma.gunsrpg.common.quests.quest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.client.render.infobar.IDataModel;
import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.client.render.infobar.TextElement;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionProviderType;
import dev.toma.gunsrpg.common.quests.condition.QuestConditions;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardList;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.*;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import dev.toma.gunsrpg.util.properties.PropertyKey;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
import java.util.function.Function;

public abstract class Quest<D extends IQuestData> {

    private static final ITextComponent QUEST_COMPLETED = new TranslationTextComponent("quest.completed").withStyle(TextFormatting.GREEN);
    private static final ITextComponent QUEST_FAILED = new TranslationTextComponent("quest.failed").withStyle(TextFormatting.RED);
    protected final World level;
    private final Multimap<Trigger, TriggerContext> triggerListeners = ArrayListMultimap.create();
    private final QuestScheme<D> scheme;
    private final IQuestCondition[] conditions;
    private final int rewardTier;
    private final UUID mayorId;
    private final DataHolder<IDataModel> displayModelHolder = new DataHolder<>(this::buildDataModel);
    private final Map<UUID, IPropertyHolder> memberInitialProperties = new HashMap<>();
    private final PlayerDataAccess access;
    protected final ISyncRequestDispatcher requestTemplateFactory;

    protected QuestingGroup group;
    protected QuestReward reward;
    protected QuestStatus status = QuestStatus.CREATED;

    public Quest(World level, QuestScheme<D> scheme, UUID traderId) {
        this.level = level;
        this.scheme = scheme;
        this.mayorId = traderId;
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
        this.access = this::getPlayerProperty;
        this.requestTemplateFactory = () -> QuestingDataProvider.getData(level).ifPresent(IQuestingData::sendData);

        registerAllTriggers();
    }

    public Quest(QuestDeserializationContext<D> context) {
        this.level = context.getWorld();
        this.scheme = context.getScheme();
        this.mayorId = context.getTraderId();
        this.conditions = context.getConditions();
        this.rewardTier = context.getRewardTier();
        this.status = context.getStatus();
        QuestReward reward = context.getReward();
        if (reward != null) {
            this.reward = reward;
        }
        this.access = this::getPlayerProperty;
        this.requestTemplateFactory = () -> QuestingDataProvider.getData(level).ifPresent(IQuestingData::sendData);

        registerAllTriggers();
    }

    public abstract void registerTriggers(ITriggerRegistration registration);

    public abstract Object[] getDescriptionArguments();

    public D getActiveData() {
        return scheme.getData();
    }

    public void assign(QuestingGroup group, World world) {
        this.group = group;
        this.group.accept(world, this::storeStatusProperties);
        if (reward == null) {
            UUID partyOwner = this.group.getGroupId();
            PlayerEntity player = world.getPlayerByUUID(partyOwner);
            if (player == null) {
                GunsRPG.log.error("Party owner not found, quest cannot be assigned!");
                return;
            }
            IPlayerData data = PlayerData.getUnsafe(player);
            IAttributeProvider provider = data.getAttributes();
            int rewardChoices = provider.getAttribute(Attribs.QUEST_VISIBLE_REWARD).intValue();
            QuestSystem system = GunsRPG.getModLifecycle().quests();
            QuestRewardManager manager = system.getRewardManager();
            QuestRewardList rewardList = manager.getTieredRewards(this.rewardTier);
            QuestReward.Options options = new QuestReward.Options().items(1).choiceCount(rewardChoices).setUnique();
            this.reward = QuestReward.generate(rewardList, options, player);
            this.onAssigned(group);
        }
    }

    public void playerJoined(PlayerEntity player) {
        this.storeStatusProperties(player);
    }

    public UUID getMayorUUID() {
        return mayorId;
    }

    public void tickQuest() {
        this.group.accept(this.level, this::storeStatusProperties);
    }

    protected void onAssigned(QuestingGroup group) {
    }

    protected void storeStatusProperties(PlayerEntity player) {
        IPropertyHolder holder = this.memberInitialProperties.computeIfAbsent(player.getUUID(), key -> PropertyContext.create());
        float savedHealth = holder.getProperty(QuestProperties.HEALTH_STATUS);
        int savedFoodLevel = holder.getProperty(QuestProperties.FOOD_STATUS);
        float health = player.getHealth();
        int foodLevel = player.getFoodData().getFoodLevel();
        if (savedHealth != health || savedFoodLevel != foodLevel) {
            holder.setProperty(QuestProperties.HEALTH_STATUS, health);
            holder.setProperty(QuestProperties.FOOD_STATUS, foodLevel);
        }
    }

    public void onCompleted() {
        if (!level.isClientSide) {
            this.group.accept(this.level, member -> {
                ServerPlayerEntity player = (ServerPlayerEntity) member;
                player.connection.send(new SPlaySoundEffectPacket(SoundEvents.PLAYER_LEVELUP, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), 0.75F, 1.0F));
                player.sendMessage(QUEST_COMPLETED, ChatType.GAME_INFO, Util.NIL_UUID);
                PlayerData.get(member).ifPresent(data -> {
                    ITraderStandings standings = data.getMayorReputationProvider();
                    standings.questFinished(this.mayorId, this);
                });
            });
            this.requestTemplateFactory.sendSyncRequest();
        }
    }

    public void onFailed() {
        if (!level.isClientSide) {
            this.group.accept(this.level, member -> {
                ServerPlayerEntity player = (ServerPlayerEntity) member;
                player.connection.send(new SPlaySoundEffectPacket(ModSounds.USE_AVENGE_ME_FRIENDS, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), 0.75F, 1.0F));
                player.sendMessage(QUEST_FAILED, ChatType.GAME_INFO, Util.NIL_UUID);
                PlayerData.get(member).ifPresent(data -> {
                    ITraderStandings standings = data.getMayorReputationProvider();
                    standings.questFailed(this.mayorId, this);
                });
            });
            QuestingDataProvider.getData(this.level).ifPresent(questing -> {
                questing.unassignQuest(this.group);
                questing.sendData();
            });
        }
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
                    onCompleted();
                    break;
                case FAILED:
                    onFailed();
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
        nbt.putUUID("createdBy", mayorId);
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

    public PlayerDataAccess getDataAccess() {
        return this.access;
    }

    public boolean isStarted() {
        return true;
    }

    public final boolean isAssigned() {
        return this.group != null;
    }

    public final QuestingGroup getGroup() {
        return this.group;
    }

    public final boolean isOwner(UUID playerId) {
        return this.isAssigned() && this.group.isLeader(playerId);
    }

    @OnlyIn(Dist.CLIENT)
    public IDataModel getDisplayModel(UUID clientId) {
        return displayModelHolder.getOrBuild(clientId);
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

    protected final boolean allowTargetMultipliers() {
        return Arrays.stream(this.conditions).allMatch(IQuestCondition::allowTargetMultipliers);
    }

    protected boolean overrideFailureFromCondition() {
        return false;
    }

    protected void writeQuestData(CompoundNBT nbt) {

    }

    protected void readQuestData(CompoundNBT nbt) {

    }

    protected void trySyncClient(World world) {
        if (group == null) return;
        QuestingDataProvider.getData(world).ifPresent(IQuestingData::sendData);
    }

    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeaderWithObjective(this);
    }

    private IDataModel buildDataModel(UUID clientId) {
        QuestDisplayDataModel dataModel = new QuestDisplayDataModel(clientId);
        if (this.getStatus() == QuestStatus.COMPLETED) {
            dataModel.addElement(new TextElement(new TranslationTextComponent("quest.task.completed").withStyle(TextFormatting.GREEN).withStyle(TextFormatting.BOLD)));
            dataModel.addElement(new TextElement(new TranslationTextComponent("quest.task.claim_reward")));
        } else {
            this.fillDataModel(dataModel);
        }
        return dataModel;
    }

    private void registerAllTriggers() {
        for (IQuestCondition condition : conditions) {
            QuestConditionProviderType<?> conditionType = condition.getProviderType().getType();
            Set<Trigger> triggerSet = conditionType.getTriggerSet();
            for (Trigger trigger : triggerSet) {
                ITriggerResponder responder = (trig, reader) -> handleQuestCondition(conditionType, condition, reader);
                ITriggerHandler handler = condition instanceof ITriggerHandler ? (ITriggerHandler) condition : ITriggerHandler.NONE;
                TriggerContext context = TriggerContext.make(responder, handler);
                triggerListeners.put(trigger, context);
            }
        }
        this.registerTriggers((trigger, responder, handler) -> triggerListeners.put(trigger, TriggerContext.make(responder, handler)));
    }

    private TriggerResponseStatus handleQuestCondition(QuestConditionProviderType<?> type, IQuestCondition condition, IPropertyReader reader) {
        boolean wasValid = condition.isValid(group, reader);
        return wasValid ? TriggerResponseStatus.OK : type.shouldFailQuest() && !overrideFailureFromCondition() ? TriggerResponseStatus.FAIL : TriggerResponseStatus.PASS;
    }

    @Override
    public String toString() {
        return this.scheme.getQuestId().toString();
    }

    private <T> T getPlayerProperty(PlayerEntity player, PropertyKey<T> key) {
        return memberInitialProperties.getOrDefault(player.getUUID(), PropertyContext.empty()).getProperty(key);
    }

    public interface ITriggerRegistration {
        void addEntry(Trigger trigger, ITriggerResponder listener, ITriggerHandler handler);
    }

    @FunctionalInterface
    public interface PlayerDataAccess {
        <T> T get(PlayerEntity player, PropertyKey<T> key);
    }

    public static class DataHolder<T> {

        private final Function<UUID, T> constructor;
        private T instance;

        public DataHolder(Function<UUID, T> constructor) {
            this.constructor = constructor;
        }

        public T getOrBuild(UUID uuid) {
            if (this.instance == null) {
                this.instance = constructor.apply(uuid);
            }
            return this.instance;
        }
    }
}
