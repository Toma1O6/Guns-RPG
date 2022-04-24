package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.UUID;

public class MayorEntity extends CreatureEntity {

    public MayorEntity(EntityType<? extends MayorEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
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
                if (status == null) {
                    standings.registerNew(uuid);
                    status = standings.getStatusWithTrader(uuid);
                }
                status.onTraderAttacked();
            });
        }
        return false;
    }
}
