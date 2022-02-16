package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import lib.toma.animations.Easings;

import java.text.DecimalFormat;

public class WeaponConfiguration extends ObjectType implements IWeaponConfig {

    private final IntType gravityDelay;
    private final IntType velocity;
    private final DoubleType damage;
    private final DoubleType recoilAnimationScale;
    private final IJamConfig jamConfig;

    public WeaponConfiguration(IObjectSpec spec, float damage, int velocity, int delay, float jamMin, float jamMax) {
        this(spec, damage, velocity, delay, jamMin, jamMax, IJamConfig.DEFAULT_EASING);
    }

    public WeaponConfiguration(IObjectSpec spec, float damage, int velocity, int delay, float jamMin, float jamMax, Easings easing) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.gravityDelay = writer.writeBoundedInt("Gravity effect delay", delay, 0, Short.MAX_VALUE, "Defines how many ticks it takes before gravity", "starts taking effect on projectiles");
        this.damage = writer.writeBoundedDouble("Base projectile damage", damage, 1.0, Double.MAX_VALUE);
        this.velocity = writer.writeBoundedInt("Projectile velocity", velocity, 1, 10000, "Projectile velocity in m/s");
        this.recoilAnimationScale = writer.writeBoundedDouble("Recoil animation scale", 1.0, 0.0, 1.0, "Configure how much will be weapon animation affected by recoil").setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(new DecimalFormat("0.0##"));
        this.jamConfig = writer.writeObject(specification -> new JamConfig(specification, jamMin, jamMax, easing), "Jam Settings");
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

    @Override
    public IJamConfig getJamConfig() {
        return jamConfig;
    }

    @Override
    public float getRecoilAnimationScale() {
        return recoilAnimationScale.floatValue();
    }
}
