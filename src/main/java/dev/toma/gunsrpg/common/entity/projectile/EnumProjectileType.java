package dev.toma.gunsrpg.common.entity.projectile;

public enum EnumProjectileType {

    BULLET,
    ARROW,
    GRENADE,
    ROCKET;

    public boolean shouldRender() {
        return this != BULLET;
    }
}
