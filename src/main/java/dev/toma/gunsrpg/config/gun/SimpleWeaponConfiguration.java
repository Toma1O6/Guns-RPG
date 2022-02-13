package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import lib.toma.animations.Easings;

public class SimpleWeaponConfiguration extends ObjectType implements IWeaponConfig {

    private final DoubleType velocity;
    private final IJamConfig jamConfig;

    public SimpleWeaponConfiguration(IObjectSpec spec, float velocity, float jamMin, float jamMax) {
        this(spec, velocity, jamMin, jamMax, IJamConfig.DEFAULT_EASING);
    }

    public SimpleWeaponConfiguration(IObjectSpec spec, float velocity, float jamMin, float jamMax, Easings easing) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.velocity = writer.writeBoundedDouble("Projectile Velocity", velocity, 1.0, 10000.0, "Projectile velocity in m/s");
        this.jamConfig = writer.writeObject(specification -> new JamConfig(specification, jamMin, jamMax, easing), "Jam Settings");
    }

    @Override
    public float getDamage() {
        return 0.0F;
    }

    @Override
    public float getVelocity() {
        return velocity.floatValue();
    }

    @Override
    public int getGravityDelay() {
        return 0;
    }

    @Override
    public IJamConfig getJamConfig() {
        return jamConfig;
    }
}
