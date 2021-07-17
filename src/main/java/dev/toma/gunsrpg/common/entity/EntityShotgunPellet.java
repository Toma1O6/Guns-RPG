package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.SGItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityShotgunPellet extends EntityBullet {

    private int effectiveRange;
    private int maxRange;
    private boolean extendedBarrel;

    public EntityShotgunPellet(EntityType<? extends EntityShotgunPellet> type, World world) {
        super(type, world);
    }

    public EntityShotgunPellet(EntityType<? extends EntityShotgunPellet> type, World worldIn, LivingEntity shooter, SGItem gun, ItemStack stack) {
        super(type, worldIn, shooter, gun, stack);
        this.extendedBarrel = shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.SHOTGUN_EXTENDED_BARREL);
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
