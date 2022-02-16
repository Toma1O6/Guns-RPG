package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import lib.toma.animations.Easings;

import java.text.DecimalFormat;

public class SimpleWeaponConfiguration extends ObjectType implements IWeaponConfig {

    private final DoubleType velocity;
    private final IJamConfig jamConfig;
    private final DoubleType recoilAnimationScale;

    public SimpleWeaponConfiguration(IObjectSpec spec, float velocity, float jamMin, float jamMax) {
        this(spec, velocity, jamMin, jamMax, IJamConfig.DEFAULT_EASING);
    }

    public SimpleWeaponConfiguration(IObjectSpec spec, float velocity, float jamMin, float jamMax, Easings easing) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.velocity = writer.writeBoundedDouble("Projectile Velocity", velocity, 1.0, 10000.0, "Projectile velocity in m/s");
        this.recoilAnimationScale = writer.writeBoundedDouble("Recoil animation scale", 1.0, 0.0, 1.0, "Configure how much will be weapon animation affected by recoil").setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(new DecimalFormat("0.0##"));
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

    @Override
    public float getRecoilAnimationScale() {
        return recoilAnimationScale.floatValue();
    }
}
