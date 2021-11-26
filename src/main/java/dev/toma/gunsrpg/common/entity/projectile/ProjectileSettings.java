package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.common.init.ModSounds;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;

public final class ProjectileSettings {

    private final int delay;
    private final boolean supersonic;
    private final float damage;
    private final boolean canPenetrate;
    private float velocity;

    public ProjectileSettings(IWeaponConfig config) {
        this(config, false);
    }

    public ProjectileSettings(IWeaponConfig config, boolean canPenetrate) {
        this.damage = config.getDamage();
        this.velocity = config.getVelocity();
        this.delay = config.getGravityDelay();
        this.canPenetrate = canPenetrate;

        this.supersonic = isSupersonicVelocity();
    }

    private ProjectileSettings(int delay, float damage, boolean canPenetrate, float velocity) {
        this.delay = delay;
        this.damage = damage;
        this.canPenetrate = canPenetrate;
        this.velocity = velocity;
        this.supersonic = isSupersonicVelocity();
    }

    public static ProjectileSettings decode(PacketBuffer buffer) {
        int delay = buffer.readInt();
        float damage = buffer.readFloat();
        float velocity = buffer.readFloat();
        boolean canPenetrate = buffer.readBoolean();
        return new ProjectileSettings(delay, damage, canPenetrate, velocity);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(delay);
        buffer.writeFloat(damage);
        buffer.writeFloat(velocity);
        buffer.writeBoolean(canPenetrate);
    }

    public SoundEvent getPassBySound() {
        return ModSounds.BULLET_WHIZZ;
    }

    public boolean isSuperSonic() {
        return supersonic;
    }

    public float getInitialDamage() {
        return damage;
    }

    public float getActualVelocity() {
        return velocity;
    }

    public boolean shouldApplyGravity(AbstractProjectile projectile) {
        return projectile.tickCount > delay;
    }

    public void applyDrag(float dragValue) {
        this.velocity *= dragValue;
    }

    public boolean isEntityPenetrationAllowed() {
        return canPenetrate;
    }

    private boolean isSupersonicVelocity() {
        return velocity > 10.0F;
    }
}
