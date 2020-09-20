package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.SGItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityShotgunPellet extends EntityBullet {

    private int effectiveRange;
    private int maxRange;
    private boolean extendedBarrel;

    public EntityShotgunPellet(World world) {
        super(world);
    }

    public EntityShotgunPellet(World worldIn, EntityLivingBase shooter, SGItem gun, ItemStack stack) {
        super(worldIn, shooter, gun, stack);
        this.extendedBarrel = shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Skills.SHOTGUN_EXTENDED_BARREL);
        this.effectiveRange = extendedBarrel ? 10 : 6;
        this.maxRange = extendedBarrel ? 20 : 13;
    }

    @Override
    protected void damageTargetEntity(Entity target, boolean isHeadshot) {
        double distance = shooter == null ? 15.0D : this.getDistanceTo(new Vec3d(shooter.posX, shooter.posY, shooter.posZ));
        if(distance > maxRange) {
            setDead();
            return;
        } else if(distance > effectiveRange && distance <= maxRange) {
            damage /= 2;
        }
        super.damageTargetEntity(target, isHeadshot);
    }

    @Override
    public void onUpdate() {
        motionY -= extendedBarrel ? 0.35F : 0.8F;
        super.onUpdate();
    }
}
