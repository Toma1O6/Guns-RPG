package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.*;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_OpenQuestScreen;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;

public class MayorEntity extends CreatureEntity {

    public static final int REFRESH_PRICE = 15;
    private final Map<UUID, ListedQuests> groupQuests;
    private final Map<UUID, Queue<QuestReward>> rewardStorage;
    private long refreshAtWorldTime;

    public MayorEntity(EntityType<? extends MayorEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
        this.groupQuests = new HashMap<>();
        this.rewardStorage = new HashMap<>();
    }

    public void reloadQuests(UUID groupOwner, ServerPlayerEntity owner) {
        IPlayerData data = PlayerData.getUnsafe(owner);
        ITraderStandings standings = data.getMayorReputationProvider();
        ITraderStatus status = standings.getStatusWithTrader(this.getUUID());
        ListedQuests quests = ListedQuests.generate(this.level, this.getUUID(), status.getReputation(), this.level.getPlayerByUUID(groupOwner));
        GunsRPG.log.info(QuestSystem.MARKER, "Refreshing mayor '{}' quests for group {}, triggered by {}", this, groupOwner, owner);
        this.groupQuests.put(groupOwner, quests);
    }

    public void storeReward(UUID owner, QuestReward reward) {
        Queue<QuestReward> rewards = this.rewardStorage.computeIfAbsent(owner, k -> new LinkedList<>());
        rewards.offer(reward);
    }

    public QuestReward findReward(UUID owner) {
        Queue<QuestReward> rewards = this.rewardStorage.get(owner);
        return rewards == null ? null : rewards.peek();
    }

    public QuestReward claimReward(UUID owner) {
        Queue<QuestReward> rewards = this.rewardStorage.get(owner);
        return rewards == null ? null : rewards.poll();
    }

    @Override
    public void tick() {
        super.tick();
        long diff = this.getRemainingRestockTime();
        if (diff <= 0) {
            refreshAtWorldTime = level.getGameTime() + GunsRPG.config.quests.questRefreshInterval;
            groupQuests.clear();
            GunsRPG.log.info(QuestSystem.MARKER, "Mayor {} quests expired", this);
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(0, new LookAtGoal(this, LivingEntity.class, 8.0F));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity p_82167_1_) {
    }

    @Override
    public void setLeashedTo(Entity entity, boolean bool) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            this.actuallyHurt(source, amount);
            return true;
        }
        return false;
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        IQuestingData questing = QuestingDataProvider.getData(level).orElse(null);
        QuestingGroup group = questing.getOrCreateGroup(player);
        PlayerEntity owner = level.getPlayerByUUID(group.getGroupId());
        if (owner == null)
            return ActionResultType.FAIL;
        if (player.isCrouching()) {
            questing.trigger(Trigger.ITEM_HANDOVER, player, holder -> {
                holder.setProperty(QuestProperties.USED_ITEM, player.getItemInHand(hand));
                holder.setProperty(QuestProperties.UUID, this.getUUID());
            });
        } else {
            if (!level.isClientSide) {
                IPlayerData playerData = PlayerData.getUnsafe(owner);
                UUID uuid = group.getGroupId();
                ListedQuests traderQuests = this.groupQuests.get(uuid);
                UUID traderId = this.getUUID();
                ITraderStandings standings = playerData.getMayorReputationProvider();
                ITraderStatus status = standings.getStatusWithTrader(traderId);

                UUID interactionId = player.getUUID();
                Queue<QuestReward> storage = this.rewardStorage.get(interactionId);
                QuestReward reward = storage != null ? storage.peek() : null;
                if (!this.groupQuests.containsKey(uuid) || traderQuests == null) {
                    this.reloadQuests(uuid, (ServerPlayerEntity) player);
                    traderQuests = this.groupQuests.get(uuid);
                }
                this.openQuestScreen(player, status, traderQuests, reward);
            }
        }
        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        ListNBT list = new ListNBT();
        for (Map.Entry<UUID, ListedQuests> entry : groupQuests.entrySet()) {
            UUID uuid = entry.getKey();
            ListedQuests quests = entry.getValue();
            CompoundNBT data = new CompoundNBT();
            data.putUUID("player", uuid);
            data.put("quests", quests.toNbt());
            list.add(data);
        }
        nbt.put("questListings", list);
        nbt.putLong("plannedRefresh", refreshAtWorldTime);

        ListNBT rewardStorage = new ListNBT();
        for (Map.Entry<UUID, Queue<QuestReward>> entry : this.rewardStorage.entrySet()) {
            UUID key = entry.getKey();
            Queue<QuestReward> queue = entry.getValue();
            if (queue.isEmpty())
                continue;
            CompoundNBT rewardEntry = new CompoundNBT();
            rewardEntry.putString("owner", key.toString());
            ListNBT rewardList = new ListNBT();
            for (QuestReward reward : queue) {
                rewardList.add(reward.toNbt());
            }
            rewardEntry.put("rewards", rewardList);
            rewardStorage.add(rewardEntry);
        }
        nbt.put("rewardStorage", rewardStorage);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        groupQuests.clear();
        ListNBT list = nbt.getList("questListings", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT data = list.getCompound(i);
            UUID player = data.getUUID("player");
            ListNBT listedQuests = data.getList("quests", Constants.NBT.TAG_COMPOUND);
            try {
                groupQuests.put(player, ListedQuests.loadNbt(level, listedQuests));
            } catch (Exception e){
                GunsRPG.log.error(QuestSystem.MARKER, "Failed to load quests for mayor, owner {}", player, e);
            }
        }
        refreshAtWorldTime = nbt.getLong("plannedRefresh");

        this.rewardStorage.clear();
        ListNBT rewardStorage = nbt.getList("rewardStorage", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < rewardStorage.size(); i++) {
            CompoundNBT rewardEntry = rewardStorage.getCompound(i);
            UUID owner = UUID.fromString(rewardEntry.getString("owner"));
            ListNBT rewardList = rewardEntry.getList("rewards", Constants.NBT.TAG_COMPOUND);
            Queue<QuestReward> rewards = new LinkedList<>();
            for (int j = 0; j < rewardList.size(); j++) {
                CompoundNBT rewardNBT = rewardList.getCompound(j);
                QuestReward reward = new QuestReward(rewardNBT);
                rewards.add(reward);
            }
            this.rewardStorage.put(owner, rewards);
        }
    }

    public long getRemainingRestockTime() {
        return refreshAtWorldTime - level.getGameTime();
    }

    public ListedQuests getQuestsForGroup(UUID groupId) {
        return groupQuests.get(groupId);
    }

    public void setRefreshTimer(long refreshAtWorldTime) {
        this.refreshAtWorldTime = refreshAtWorldTime;
    }

    public long getCurrentRefreshTarget() {
        return refreshAtWorldTime;
    }

    private void openRewardClaimScreen(PlayerEntity entity, QuestReward reward, int pendingCount) {

    }

    private void openQuestScreen(PlayerEntity player, ITraderStatus status, ListedQuests traderQuests, @Nullable QuestReward pendingReward) {
        ReputationStatus reputationStatus = ReputationStatus.getStatus(status.getReputation());
        NetworkManager.sendClientPacket((ServerPlayerEntity) player, new S2C_OpenQuestScreen(reputationStatus, traderQuests.toNbt(), this.getId(), this.refreshAtWorldTime, pendingReward));
    }

    public static final class ListedQuests {

        private Quest<?>[] quests;

        public ListedQuests(Quest<?>[] quests) {
            this.quests = quests;
        }

        public static ListedQuests loadNbt(World world, ListNBT nbt) {
            return new ListedQuests(nbt.stream().<Quest<?>>map(inbt -> QuestTypes.getFromNbt(world, (CompoundNBT) inbt)).toArray(Quest[]::new));
        }

        public static ListedQuests generate(World world, UUID traderId, float reputation, PlayerEntity player) {
            QuestSystem system = GunsRPG.getModLifecycle().quests();
            QuestManager manager = system.getQuestManager();
            IPlayerData data = PlayerData.getUnsafe(player);
            IAttributeProvider provider = data.getAttributes();
            int count = provider.getAttribute(Attribs.QUEST_COUNT).intValue();
            Set<QuestScheme<?>> schemes = manager.getSchemes(count, reputation, player);
            Quest<?>[] quests = schemes.stream().<Quest<?>>map(scheme -> makeQuestFromScheme(new IQuestFactory.InstanceContext<>(world, scheme, traderId, player))).toArray(Quest[]::new);
            return new ListedQuests(quests);
        }

        public Quest<?>[] getQuests() {
            return quests;
        }

        public Quest<?> getQuest(int index) {
            return this.quests[index];
        }

        public int getQuestCount() {
            return this.quests.length;
        }

        public void refreshList() {
            quests = Arrays.stream(quests).filter(quest -> quest.getStatus() == QuestStatus.CREATED).toArray(Quest[]::new);
        }

        public ListNBT toNbt() {
            ListNBT nbt = new ListNBT();
            Arrays.stream(quests).map(Quest::serialize).forEach(nbt::add);
            return nbt;
        }

        private static <D extends IQuestData, Q extends Quest<D>> Q makeQuestFromScheme(IQuestFactory.InstanceContext<D, Q> ctx) {
            QuestType<D, Q> type = ctx.getQuestType();
            return type.newQuestInstance(ctx);
        }
    }
}
