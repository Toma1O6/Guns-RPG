package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.Entity;

public final class PenetrationData {

    private Entity lastHit;

    public Entity getLastHit() {
        return lastHit;
    }

    public boolean collide(Entity entity) {
        boolean flag = lastHit != null;
        this.lastHit = entity;
        return flag;
    }
}
