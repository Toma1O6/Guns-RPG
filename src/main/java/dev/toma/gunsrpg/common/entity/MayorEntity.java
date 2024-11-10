package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.*;
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

import java.util.*;

public class MayorEntity extends CreatureEntity {

    private final Map<UUID, ListedQuests> playerQuests;
    private long refreshAtWorldTime;

    public MayorEntity(EntityType<? extends MayorEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
        this.playerQuests = new HashMap<>();
    }

    @Override
    public void tick() {
        super.tick();
        long diff = this.getRemainingRestockTime();
        if (diff <= 0) {
            refreshAtWorldTime = level.getGameTime() + GunsRPG.config.quests.questRefreshInterval;
            playerQuests.clear();
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
                ListedQuests traderQuests = playerQuests.get(uuid);
                UUID traderId = this.getUUID();
                ITraderStandings standings = playerData.getMayorReputationProvider();
                ITraderStatus status = standings.getStatusWithTrader(traderId);
                if (!playerQuests.containsKey(uuid) || traderQuests == null) {
                    traderQuests = ListedQuests.generate(level, traderId, status.getReputation());
                    playerQuests.put(uuid, traderQuests);
                }
                ReputationStatus reputationStatus = ReputationStatus.getStatus(status.getReputation());
                NetworkManager.sendClientPacket((ServerPlayerEntity) player, new S2C_OpenQuestScreen(reputationStatus, traderQuests.toNbt(), this.getId(), refreshAtWorldTime));
            }
        }
        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        ListNBT list = new ListNBT();
        for (Map.Entry<UUID, ListedQuests> entry : playerQuests.entrySet()) {
            UUID uuid = entry.getKey();
            ListedQuests quests = entry.getValue();
            CompoundNBT data = new CompoundNBT();
            data.putUUID("player", uuid);
            data.put("quests", quests.toNbt());
            list.add(data);
        }
        nbt.put("questListings", list);
        nbt.putLong("plannedRefresh", refreshAtWorldTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        playerQuests.clear();
        ListNBT list = nbt.getList("questListings", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT data = list.getCompound(i);
            UUID player = data.getUUID("player");
            ListNBT listedQuests = data.getList("quests", Constants.NBT.TAG_COMPOUND);
            playerQuests.put(player, ListedQuests.loadNbt(level, listedQuests));
        }
        refreshAtWorldTime = nbt.getLong("plannedRefresh");
    }

    public long getRemainingRestockTime() {
        return refreshAtWorldTime - level.getGameTime();
    }

    public ListedQuests getQuests(UUID playerId) {
        return playerQuests.get(playerId);
    }

    public void setRefreshTimer(long refreshAtWorldTime) {
        this.refreshAtWorldTime = refreshAtWorldTime;
    }

    public long getCurrentRefreshTarget() {
        return refreshAtWorldTime;
    }

    public static final class ListedQuests {

        public static final int QUEST_COUNT = 5;
        private Quest<?>[] quests;

        public ListedQuests(Quest<?>[] quests) {
            this.quests = quests;
        }

        public static ListedQuests loadNbt(World world, ListNBT nbt) {
            return new ListedQuests(nbt.stream().<Quest<?>>map(inbt -> QuestTypes.getFromNbt(world, (CompoundNBT) inbt)).toArray(Quest[]::new));
        }

        public static ListedQuests generate(World world, UUID traderId, float reputation) {
            QuestSystem system = GunsRPG.getModLifecycle().quests();
            QuestManager manager = system.getQuestManager();
            Set<QuestScheme<?>> schemes = manager.getSchemes(QUEST_COUNT, reputation);
            Quest<?>[] quests = schemes.stream().<Quest<?>>map(scheme -> makeQuestFromScheme(world, scheme, traderId)).toArray(Quest[]::new);
            return new ListedQuests(quests);
        }

        public Quest<?>[] getQuests() {
            return quests;
        }

        public void filterActive() {
            quests = Arrays.stream(quests).filter(quest -> quest.getStatus() == QuestStatus.CREATED).toArray(Quest[]::new);
        }

        public ListNBT toNbt() {
            ListNBT nbt = new ListNBT();
            Arrays.stream(quests).map(Quest::serialize).forEach(nbt::add);
            return nbt;
        }

        @SuppressWarnings("unchecked")
        private static <D extends IQuestData, Q extends Quest<D>> Q makeQuestFromScheme(World world, QuestScheme<D> scheme, UUID traderId) {
            QuestType<D, Q> type = (QuestType<D, Q>) scheme.getQuestType();
            return type.newQuestInstance(world, scheme, traderId);
        }
    }
}
