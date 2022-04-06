package dev.toma.gunsrpg.util.properties;

import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;

public final class Properties {

    public static final PropertyKey<Integer> LOOT_LEVEL                 = PropertyKey.newKey(1);
    public static final PropertyKey<Boolean> IS_HEADSHOT                = PropertyKey.newKey(false);
    public static final PropertyKey<PenetrationData> PENETRATION        = PropertyKey.newKey();
}
