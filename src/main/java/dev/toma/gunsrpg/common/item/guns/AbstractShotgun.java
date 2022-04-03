package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
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
    protected AbstractProjectile makeProjectile(World level, LivingEntity shooter) {
        return new Pellet(ModEntities.PELLET.get(), level, shooter);
    }

    @Override
    protected void handleShootProjectileAction(World world, LivingEntity entity, ItemStack stack, IShootProps props) {
        int pellets = this.getPelletCount(entity, stack);
        for (int i = 0; i < pellets; i++) {
            shootProjectile(world, entity, stack, props);
        }
    }

    @Override
    protected float getInaccuracy(IShootProps props, LivingEntity entity) {
        return 2.4F;
    }
}
