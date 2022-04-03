package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.Bolt;
import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractCrossbow extends GunItem {

    public AbstractCrossbow(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public void shootProjectile(World level, LivingEntity shooter, ItemStack stack, IShootProps props) {
        Bolt bolt = new Bolt(ModEntities.BOLT.get(), level, shooter);
        IWeaponConfig config = this.getWeaponConfig();
        float damage = this.getWeaponDamage(stack, shooter) * props.getDamageMultiplier();
        float velocity = this.getInitialVelocity(config, shooter);
        int delay = config.getGravityDelay();
        bolt.setup(damage, velocity, delay);
        bolt.fire(shooter.xRot, shooter.yRot, props.getInaccuracy());
        this.prepareForShooting(bolt, shooter);
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            PlayerData.get(player).ifPresent(data -> {
                PenetrationData penetrationData = getPenetrationData(data);
                if (penetrationData != null) {
                    bolt.setPenetrationData(penetrationData);
                }
            });
        }
        level.addFreshEntity(bolt);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return 0.0F;
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return 0.0F;
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return 0.0F;
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
    }

    protected float getInitialVelocity(IWeaponConfig config, LivingEntity livingEntity) {
        return config.getVelocity();
    }
}
