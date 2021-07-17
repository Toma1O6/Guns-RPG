package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;

public class WeaponConfiguration extends ObjectType implements IWeaponConfiguration {

    private final IntType gravityDelay;
    private final IntType firerate;
    private final IntType velocity;
    private final DoubleType damage;
    private final DoubleType horizontalRecoil;
    private final DoubleType verticalRecoil;

    public static WeaponConfiguration basic(IObjectSpec spec, float damage, int velocity, int gravityDelay, float vertical, float horizontal, int rate) {
        return new WeaponConfiguration(spec, damage, velocity, vertical, horizontal, gravityDelay, rate);
    }

    public static WeaponConfiguration withUpgrade(IObjectSpec spec, float damage, int velocity, int gravityDelay, float vertical, float horizontal, int rate, int upgradedRate) {
        return new WeaponConfiguration.WeaponConfigurationUpgraded(spec, damage, velocity, vertical, horizontal, gravityDelay, rate, upgradedRate);
    }

    protected WeaponConfiguration(IObjectSpec spec, float damage, int velocity, float verticalRecoil, float horizontalRecoil, int delay, int firerate) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.gravityDelay = writer.writeBoundedInt("Gravity effect delay", delay, 0, Short.MAX_VALUE, "Defines how many ticks it takes before gravity", "starts taking effect on projectiles");
        this.firerate = writer.writeBoundedInt("Shoot delay", firerate, 1, 1000, "Delay in ticks");
        this.damage = writer.writeBoundedDouble("Base projectile damage", damage, 1.0, Double.MAX_VALUE);
        this.velocity = writer.writeBoundedInt("Projectile velocity", velocity, 1, 10000, "Projectile velocity in m/s");
        this.horizontalRecoil = writer.writeBoundedDouble("Horizontal recoil", horizontalRecoil, 0.0, 15.0, "Yaw offset in degrees");
        this.verticalRecoil = writer.writeBoundedDouble("Vertical recoil", verticalRecoil, 0.0, 15.0, "Pitch offset in degrees");
    }

    @Override
    public float getDamage() {
        return (float) ((double) damage.get());
    }

    @Override
    public float getVerticalRecoil() {
        return verticalRecoil.floatValue();
    }

    @Override
    public float getHorizontalRecoil() {
        return horizontalRecoil.floatValue();
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
    public int getFirerate() {
        return firerate.get();
    }

    @Override
    public int getUpgradedFirerate() {
        return getFirerate();
    }

    private static class WeaponConfigurationUpgraded extends WeaponConfiguration {

        private final IntType upgradedFirerate;

        private WeaponConfigurationUpgraded(IObjectSpec spec, float damage, int velocity, float verticalRecoil, float horizontalRecoil, int delay, int firerate, int upgradedFirerate) {
            super(spec, damage, velocity, verticalRecoil, horizontalRecoil, delay, firerate);
            IConfigWriter writer = spec.getWriter();
            this.upgradedFirerate = writer.writeBoundedInt("Shoot delay with upgrade", upgradedFirerate, 1, 1000, "Delay in ticks", "Applied only on weapons with skill which affects firerate");
        }

        @Override
        public int getUpgradedFirerate() {
            return upgradedFirerate.get();
        }
    }
}
