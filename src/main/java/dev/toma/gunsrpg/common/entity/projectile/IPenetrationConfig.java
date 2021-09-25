package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.Entity;

public interface IPenetrationConfig {

    IPenetrationConfig NONE = new None();

    boolean isSupported();

    boolean hasPenetrated();

    void onHit(Entity entity, Projectile projectile);

    Entity getLastHit();

    static IPenetrationConfig none() {
        return NONE;
    }

    static IPenetrationConfig newHandler(float multiplier) {
        return new Handler(multiplier);
    }

    class None implements IPenetrationConfig {

        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public boolean hasPenetrated() {
            return false;
        }

        @Override
        public void onHit(Entity entity, Projectile projectile) {
        }

        @Override
        public Entity getLastHit() {
            return null;
        }
    }

    class Handler implements IPenetrationConfig {

        private final float damageMultiplier;
        private Entity lastHit;

        private Handler(float damageMultiplier) {
            this.damageMultiplier = damageMultiplier;
        }

        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public boolean hasPenetrated() {
            return lastHit != null;
        }

        @Override
        public void onHit(Entity entity, Projectile projectile) {
            lastHit = entity;
            projectile.mulDamage(damageMultiplier);
        }

        @Override
        public Entity getLastHit() {
            return lastHit;
        }
    }
}
