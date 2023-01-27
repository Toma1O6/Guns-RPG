package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.integration.questing.engine.MayorQuestsEngine;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.questing.Questing;
import dev.toma.questing.common.data.PartyData;
import dev.toma.questing.common.engine.QuestEngineManager;
import dev.toma.questing.common.party.PartyManager;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public class MayorEntity extends CreatureEntity {

    public static final IIntervalProvider REFRESH_LIMIT = Interval.hours(1);
    private long refreshAtWorldTime;

    public MayorEntity(EntityType<? extends MayorEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
    }

    @Override
    public void tick() {
        super.tick();
        long diff = this.getRemainingRestockTime();
        if (diff <= 0) {
            refreshAtWorldTime = level.getGameTime() + REFRESH_LIMIT.getTicks();
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
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (!player.level.isClientSide) {
            dev.toma.questing.common.data.PlayerData data = dev.toma.questing.common.data.PlayerDataProvider.getUnsafe(player);
            PartyData partyData = data.getPartyData();
            PartyManager partyManager = Questing.PARTY_MANAGER.get();
            QuestEngineManager engineManager = Questing.QUEST_MANAGER.get();
            partyManager.getPartyById(partyData.getPartyId()).ifPresent(party -> {
                MayorQuestsEngine engine = engineManager.getQuestEngine(MayorQuestsEngine.IDENTIFIER);
                if (engine.shouldRefreshQuests(party, player.level.getGameTime())) {
                    if (player.getUUID().equals(party.getOwner())) {
                        float reputation = PlayerData.get(player)
                                .map(pData -> pData.getReputation().getReputation(this.uuid))
                                .orElse(0.0F);
                        engine.generateQuests(party, reputation, this.uuid);
                    }
                }
                // TODO send data to client and open quests screen
            });
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            PlayerData.get(player).ifPresent(data -> {
                //ITraderStandings standings = data.getQuests().getTraderStandings();
                //UUID uuid = this.getUUID();
                //ITraderStatus status = standings.getStatusWithTrader(uuid);
                //status.onTraderAttacked();
            });
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putLong("plannedRefresh", refreshAtWorldTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        ListNBT list = nbt.getList("questListings", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT data = list.getCompound(i);
            UUID player = data.getUUID("player");
            ListNBT listedQuests = data.getList("quests", Constants.NBT.TAG_COMPOUND);
        }
        refreshAtWorldTime = nbt.getLong("plannedRefresh");
    }

    public long getRemainingRestockTime() {
        return refreshAtWorldTime - level.getGameTime();
    }

    public void setClientTimer(long refreshAtWorldTime) {
        this.refreshAtWorldTime = refreshAtWorldTime;
    }

    public long getCurrentRefreshTarget() {
        return refreshAtWorldTime;
    }
}
