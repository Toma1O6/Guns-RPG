package dev.toma.gunsrpg.common.debuffs.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

public abstract class AbstractIntervalDamageStageEvent implements DebuffStageEvent {

    private final DamageSource source;
    private final int interval;
    private final float amount;

    public AbstractIntervalDamageStageEvent(DamageSource source, int interval, float amount) {
        this.source = source;
        this.interval = interval;
        this.amount = amount;
    }

    @Override
    public void apply(PlayerEntity player) {
        if (player.level.isClientSide)
            return;
        if (canApply(player)) {
            if (interval > 0) {
                if (player.level.getGameTime() % interval != 0) {
                    return;
                }
            }
            player.hurt(source, amount);
        }
    }

    protected boolean canApply(PlayerEntity player) {
        return true;
    }

    public DamageSource getSource() {
        return source;
    }

    public int getInterval() {
        return interval;
    }

    public float getAmount() {
        return amount;
    }
}
