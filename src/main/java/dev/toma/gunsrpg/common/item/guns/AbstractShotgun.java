package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.entity.projectile.Pellet;
import dev.toma.gunsrpg.common.init.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AbstractShotgun extends GunItem {

    public AbstractShotgun(String name, Properties properties) {
        super(name, properties);
    }

    public abstract int getPelletCount(LivingEntity shooter, ItemStack stack);

    @Override
    public final void shootProjectile(World level, LivingEntity shooter, ItemStack stack, IShootProps props) {
        int numPellets = this.getPelletCount(shooter, stack);
        float spread = this.getPelletSpreadBase(shooter);
        for (int i = 0; i < numPellets; i++) {
            shootPellet(level, shooter, stack, props, spread);
        }
    }

    protected void shootPellet(World level, LivingEntity shooter, ItemStack stack, IShootProps props, float baseSpread) {
        Pellet pellet = new Pellet(ModEntities.PELLET.get(), level, shooter);
        IWeaponConfig config = this.getWeaponConfig();
        float damage = this.getWeaponDamage(stack);
        float velocity = config.getVelocity();
        int delay = config.getGravityDelay();
        pellet.setup(damage, velocity, delay);
        pellet.fire(shooter.xRot, shooter.yRot, baseSpread + props.getInaccuracy());
        level.addFreshEntity(pellet);
    }

    protected float getPelletSpreadBase(LivingEntity shooter) {
        return 2.4F;
    }
}
