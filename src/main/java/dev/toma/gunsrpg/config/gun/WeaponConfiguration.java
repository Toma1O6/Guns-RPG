package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IWeaponConfig;

public class WeaponConfiguration extends ObjectType implements IWeaponConfig {

    private final IntType gravityDelay;
    private final IntType velocity;
    private final DoubleType damage;

    protected WeaponConfiguration(IObjectSpec spec, float damage, int velocity, int delay) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.gravityDelay = writer.writeBoundedInt("Gravity effect delay", delay, 0, Short.MAX_VALUE, "Defines how many ticks it takes before gravity", "starts taking effect on projectiles");
        this.damage = writer.writeBoundedDouble("Base projectile damage", damage, 1.0, Double.MAX_VALUE);
        this.velocity = writer.writeBoundedInt("Projectile velocity", velocity, 1, 10000, "Projectile velocity in m/s");
    }

    public static WeaponConfiguration basic(IObjectSpec spec, float damage, int velocity, int gravityDelay) {
        return new WeaponConfiguration(spec, damage, velocity, gravityDelay);
    }

    @Override
    public float getDamage() {
        return (float) ((double) damage.get());
    }

    @Override
    public float getVelocity() {
        int ms = velocity.get();
        return ms / 20.0F;
    }

    @Override
    public int getGravityDelay() {
        return gravityDelay.get();
    }
}
