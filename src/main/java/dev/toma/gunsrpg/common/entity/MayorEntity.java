package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
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
