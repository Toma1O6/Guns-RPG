package dev.toma.gunsrpg.common.item.guns.builder;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.common.item.guns.GunItem;

public class StatBuilder {

    private final GunBuilder parent;

    private float damage;
    private float velocity;
    private float gravity;
    private int effectStart;

    protected StatBuilder(GunBuilder parentBuilder) {
        this.parent = parentBuilder;
    }

    public StatBuilder withDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public StatBuilder withVelocity(float velocity) {
        this.velocity = velocity;
        return this;
    }

    public StatBuilder withGravity(float gravity, int effectStart) {
        this.gravity = gravity;
        this.effectStart = effectStart;
        return this;
    }

    public GunBuilder createStats() {
        Preconditions.checkState(damage > 0F, "Couldn't create gun stats: Damage must be > 0.0!");
        Preconditions.checkState(velocity > 0F, "Couldn't create gun stats: Velocity must be > 0.0!");
        Preconditions.checkState(gravity > 0F, "Couldn't create gun stats: Gravity must be > 0.0!");
        Preconditions.checkState(effectStart >= 0, "Couldn't create gun stats: Gravity effect start cannot be < 0!");
        GunItem.GunStats stats = new GunItem.GunStats(this);
        this.parent.setStats(stats);
        return this.parent;
    }

    public float getDamage() {
        return damage;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getGravity() {
        return gravity;
    }

    public int getEffectStart() {
        return effectStart;
    }
}
