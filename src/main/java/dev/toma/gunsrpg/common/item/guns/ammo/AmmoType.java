package dev.toma.gunsrpg.common.item.guns.ammo;

public enum AmmoType {
    _9MM(MaterialDataList.CONTAINER_9MM),
    _45ACP(MaterialDataList.CONTAINER_45ACP),
    _556MM(MaterialDataList.CONTAINER_556MM),
    _762MM,
    _12G,
    CROSSBOW,
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
