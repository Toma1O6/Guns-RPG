package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.GunDamageSource;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.CrossbowItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCrossbowBolt extends EntityBullet {

    public EntityCrossbowBolt(EntityType<? extends EntityCrossbowBolt> type, World world) {
        super(type, world);
    }

    public EntityCrossbowBolt(EntityType<? extends EntityCrossbowBolt> type, World worldIn, LivingEntity shooter, CrossbowItem gun, ItemStack stack) {
        super(type, worldIn, shooter, gun, stack);
        canPenetrateEntity = gun == GRPGItems.CROSSBOW && shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_PENETRATOR);
    }

    @Override
    protected void modifyPenetrationDamage() {
        damage = damage * 0.35F;
    }

    @Override
    protected void damageTargetEntity(Entity target, boolean isHeadshot) {
        target.hurt(new GunDamageSource(shooter, this, stack), damage);
    }

    @Override
    public boolean isLimitedLifetime() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double sqrDist) {
        return true;
    }
}
