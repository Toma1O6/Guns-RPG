package dev.toma.gunsrpg.util.properties;

import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;

public final class Properties {

    public static final PropertyKey<Integer> LOOT_LEVEL                 = PropertyKey.newKey("loot_level", 1);
    public static final PropertyKey<Boolean> IS_HEADSHOT                = PropertyKey.newKey("is_headshot", false);
    public static final PropertyKey<PenetrationData> PENETRATION        = PropertyKey.newKey("penetration");
    public static final PropertyKey<Integer> EXPLOSION_POWER            = PropertyKey.newKey("explosion_power", 0);
    public static final PropertyKey<Boolean> FUELED                     = PropertyKey.newSynchronizedKey("fueled", false, PrimitiveSerializers.BOOLEAN);
    public static final PropertyKey<Integer> TRACER                     = PropertyKey.newSynchronizedKey("tracer", null, PrimitiveSerializers.INT);
    public static final PropertyKey<Integer> ENTITY_ID                  = PropertyKey.newSynchronizedKey("entity_id", null, PrimitiveSerializers.INT);
}
