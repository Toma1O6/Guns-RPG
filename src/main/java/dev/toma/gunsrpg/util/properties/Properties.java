package dev.toma.gunsrpg.util.properties;

import dev.toma.gunsrpg.common.entity.projectile.IReaction;
import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.item.guns.util.IEntityTrackingGun;

public final class Properties {

    public static final PropertyKey<Integer> LOOT_LEVEL                 = PropertyKey.newKey("loot_level", 0);
    public static final PropertyKey<Boolean> IS_HEADSHOT                = PropertyKey.newKey("is_headshot", false);
    public static final PropertyKey<PenetrationData> PENETRATION        = PropertyKey.newKey("penetration");
    public static final PropertyKey<IReaction> REACTION                 = PropertyKey.newKey("reaction");
    public static final PropertyKey<Integer> EXPLOSION_POWER            = PropertyKey.newKey("explosion_power", 0);
    public static final PropertyKey<Boolean> FUELED                     = PropertyKey.newSynchronizedKey("fueled", false, PrimitiveSerializers.BOOLEAN);
    public static final PropertyKey<Boolean> IMPACT                     = PropertyKey.newSynchronizedKey("impact", false, PrimitiveSerializers.BOOLEAN);
    public static final PropertyKey<Boolean> STICKY                     = PropertyKey.newSynchronizedKey("sticky", false, PrimitiveSerializers.BOOLEAN);
    public static final PropertyKey<Integer> TRACER                     = PropertyKey.newSynchronizedKey("tracer", null, PrimitiveSerializers.INT);
    public static final PropertyKey<Integer> ENTITY_ID                  = PropertyKey.newSynchronizedKey("entity_id", -1, PrimitiveSerializers.INT);
    public static final PropertyKey<IEntityTrackingGun.GuidenanceProperties> GUIDENANCE = PropertyKey.newSynchronizedKey("guidenance", IEntityTrackingGun.GuidenanceProperties.PLAYER, IEntityTrackingGun.GuidenanceProperties.SERIALIZER);
}
