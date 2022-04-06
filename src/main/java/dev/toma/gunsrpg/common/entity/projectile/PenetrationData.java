package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.Entity;

public final class PenetrationData {

    private Entity lastHit;
    private float multiplier;

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public Entity getLastHit() {
        return lastHit;
    }

    public boolean collide(Entity entity) {
        boolean flag = lastHit != null;
        this.lastHit = entity;
        return flag;
    }

    public static final class Factory {

        private final float multiplier;

        public Factory(float multiplier) {
            this.multiplier = multiplier;
        }

        public PenetrationData make() {
            PenetrationData data = new PenetrationData();
            data.setMultiplier(multiplier);
            return data;
        }
    }
}
