package dev.toma.gunsrpg.common.item.guns.builder;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.function.BulletSpawner;

public class GunBuilder {

    private GunBuilder() {
    }

    private String name;
    public BulletSpawner bulletSpawner;
    public GunItem.GunStats stats;

    public static GunBuilder create() {
        return new GunBuilder();
    }

    public GunBuilder makeBullet(BulletSpawner spawner) {
        this.bulletSpawner = spawner;
        return this;
    }

    public StatBuilder statBuilder() {
        return new StatBuilder(this);
    }

    public GunItem build(String name) {
        Preconditions.checkNotNull(stats, "Not building: Gun stats are NULL");
        Preconditions.checkState(name != null && !name.isEmpty(), "Not building: Invalid registry name");
        return new GunItem(name, this);
    }

    protected void setStats(GunItem.GunStats stats) {
        this.stats = stats;
    }
}
