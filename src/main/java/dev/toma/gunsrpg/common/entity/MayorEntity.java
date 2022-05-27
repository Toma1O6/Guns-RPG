package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.*;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_OpenQuestScreen;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class MayorEntity extends CreatureEntity {

    public static final IIntervalProvider REFRESH_LIMIT = Interval.hours(1);
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
            refreshAtWorldTime = level.getGameTime() + REFRESH_LIMIT.getTicks();
            playerQuests.clear();
        }
    }

    @Override
    protected void registerGoals() {
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
        Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            PlayerData.get(player).ifPresent(data -> {
                ITraderStandings standings = data.getQuests().getTraderStandings();
                UUID uuid = this.getUUID();
                ITraderStatus status = standings.getStatusWithTrader(uuid);
                status.onTraderAttacked();
            });
        } else {
            super.hurt(source, amount);
        }
        return false;
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        IPlayerData data = PlayerData.getUnsafe(player);
        IQuests quests = data.getQuests();
        if (player.isCrouching()) {
            quests.getActiveQuest().ifPresent(quest -> {
                ItemStack stack = player.getItemInHand(hand);
                IPropertyHolder holder = PropertyContext.create();
                holder.setProperty(QuestProperties.PLAYER, player);
                holder.setProperty(QuestProperties.USED_ITEM, stack);
                quest.trigger(Trigger.ITEM_HANDOVER, holder);
            });
        } else {
            if (!level.isClientSide) {
                UUID uuid = player.getUUID();
                ListedQuests traderQuests = playerQuests.get(uuid);
                UUID traderId = this.getUUID();
                ITraderStatus status = quests.getTraderStandings().getStatusWithTrader(traderId);
                if (!playerQuests.containsKey(uuid)) {
                    traderQuests = ListedQuests.generate(traderId, status.getReputation());
                    playerQuests.put(uuid, traderQuests);
                }
                ReputationStatus reputationStatus = ReputationStatus.getStatus(status.getReputation());
                NetworkManager.sendClientPacket((ServerPlayerEntity) player, new S2C_OpenQuestScreen(reputationStatus, traderQuests, this.getId(), refreshAtWorldTime));
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
            playerQuests.put(player, ListedQuests.loadNbt(listedQuests));
        }
        refreshAtWorldTime = nbt.getLong("plannedRefresh");
    }

    public long getRemainingRestockTime() {
        return refreshAtWorldTime - level.getGameTime();
    }

    public ListedQuests getQuests(UUID playerId) {
        return playerQuests.get(playerId);
    }

    public void setClientTimer(long refreshAtWorldTime) {
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

        public static ListedQuests loadNbt(ListNBT nbt) {
            return new ListedQuests(nbt.stream().<Quest<?>>map(inbt -> QuestTypes.getFromNbt((CompoundNBT) inbt)).toArray(Quest[]::new));
        }

        public static ListedQuests generate(UUID traderId, float reputation) {
            QuestSystem system = GunsRPG.getModLifecycle().quests();
            QuestManager manager = system.getQuestManager();
            Set<QuestScheme<?>> schemes = manager.getSchemes(QUEST_COUNT, reputation);
            Quest<?>[] quests = schemes.stream().<Quest<?>>map(scheme -> makeQuestFromScheme(scheme, traderId)).toArray(Quest[]::new);
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
        private static <D extends IQuestData, Q extends Quest<D>> Q makeQuestFromScheme(QuestScheme<D> scheme, UUID traderId) {
            QuestType<D, Q> type = (QuestType<D, Q>) scheme.getQuestType();
            return type.newQuestInstance(scheme, traderId);
        }
    }
}
