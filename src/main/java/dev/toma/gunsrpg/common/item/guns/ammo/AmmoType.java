package dev.toma.gunsrpg.common.item.guns.ammo;

public enum AmmoType {
    AMMO_9MM(MaterialDataList.CONTAINER_9MM),
    AMMO_45ACP(MaterialDataList.CONTAINER_45ACP),
    AMMO_556MM(MaterialDataList.CONTAINER_556MM),
    AMMO_762MM(MaterialDataList.CONTAINER_762MM),
    AMMO_12G(MaterialDataList.CONTAINER_12G),
    BOLT(MaterialDataList.CONTAINER_BOLTS),
    GRENADE,
    ROCKET;

    final IMaterialDataContainer container;

    AmmoType() {
        this(null);
    }

    AmmoType(IMaterialDataContainer container) {
        this.container = container;
    }

    public boolean isExplosive() {
        return this == GRENADE || this == ROCKET;
    }
}
