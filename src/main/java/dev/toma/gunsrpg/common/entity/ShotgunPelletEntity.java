package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.S1897Item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShotgunPelletEntity extends BulletEntity {

    private int effectiveRange;
    private int maxRange;
    private boolean extendedBarrel;

    public ShotgunPelletEntity(EntityType<? extends ShotgunPelletEntity> type, World world) {
        super(type, world);
    }

    public ShotgunPelletEntity(EntityType<? extends ShotgunPelletEntity> type, World worldIn, LivingEntity shooter, S1897Item gun, ItemStack stack) {
        super(type, worldIn, shooter, gun, stack);
        this.extendedBarrel = shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.SHOTGUN_EXTENDED_BARREL);
        this.effectiveRange = extendedBarrel ? 10 : 6;
        this.maxRange = extendedBarrel ? 20 : 13;
    }

    @Override
    protected void damageTargetEntity(Entity target, boolean isHeadshot) {
        double distance = shooter == null ? 15.0D : this.getDistanceTo(shooter.position());
        if (distance > maxRange) {
            remove();
            return;
        } else if (distance > effectiveRange && distance <= maxRange) {
            damage /= 2;
        }
        super.damageTargetEntity(target, isHeadshot);
    }

    @Override
    public void tick() {
        setDeltaMovement(getDeltaMovement().subtract(0.0, extendedBarrel ? 0.35 : 0.8, 0.0));
        super.tick();
    }
}
