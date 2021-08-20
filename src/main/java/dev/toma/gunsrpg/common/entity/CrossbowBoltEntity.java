package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.WoodenCrossbowItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CrossbowBoltEntity extends BulletEntity {

    public CrossbowBoltEntity(EntityType<? extends CrossbowBoltEntity> type, World world) {
        super(type, world);
    }

    public CrossbowBoltEntity(EntityType<? extends CrossbowBoltEntity> type, World worldIn, LivingEntity shooter, WoodenCrossbowItem gun, ItemStack stack) {
        super(type, worldIn, shooter, gun, stack);
        canPenetrateEntity = gun == ModItems.WOODEN_CROSSBOW && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_PENETRATOR);
    }

    @Override
    protected void modifyPenetrationDamage() {
        damage = damage * 0.35F;
    }

    @Override
    protected void damageTargetEntity(Entity target, boolean isHeadshot) {
        target.hurt(ModDamageSources.dealWeaponDamage(shooter, this, stack), damage);
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
