package dev.toma.gunsrpg.util.properties;

import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.entity.projectile.TracerInfo;

public final class Properties {

    public static final PropertyKey<Integer> LOOT_LEVEL                 = PropertyKey.newKey("loot_level", 1);
    public static final PropertyKey<Boolean> IS_HEADSHOT                = PropertyKey.newKey("is_headshot", false);
    public static final PropertyKey<PenetrationData> PENETRATION        = PropertyKey.newKey("penetration");
    public static final PropertyKey<TracerInfo> TRACER                  = PropertyKey.newSynchronizedKey("tracer", null, new TracerInfo.Serializer());
}
