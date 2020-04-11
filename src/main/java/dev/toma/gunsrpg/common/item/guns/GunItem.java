package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.builder.GunBuilder;
import dev.toma.gunsrpg.common.item.guns.builder.StatBuilder;
import dev.toma.gunsrpg.util.function.BulletSpawner;

public class GunItem extends GRPGItem {

    protected final BulletSpawner bulletSpawner;
    protected final GunStats stats;

    public GunItem(String name, GunBuilder builder) {
        super(name);
        this.bulletSpawner = builder.bulletSpawner;
        this.stats = builder.stats;
    }

    public BulletSpawner getBulletSpawner() {
        return bulletSpawner;
    }

    public GunStats getStats() {
        return stats;
    }

    public static class GunStats {

        public final float baseDamage;
        public final float velocity;
        public final float gravity;
        public final int gravityEffect;

        public GunStats(StatBuilder builder) {
            this.baseDamage = builder.getDamage();
            this.velocity = builder.getVelocity();
            this.gravity = builder.getGravity();
            this.gravityEffect = builder.getEffectStart();
        }
    }
}
