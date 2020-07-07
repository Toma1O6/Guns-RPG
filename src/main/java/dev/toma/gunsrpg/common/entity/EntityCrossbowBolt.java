package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.GunDamageSource;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.CrossbowItem;
import dev.toma.gunsrpg.common.skilltree.Ability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityCrossbowBolt extends EntityBullet {

    public final List<Vec3d> previousPositions = new ArrayList<>();

    public EntityCrossbowBolt(World world) {
        super(world);
    }

    public EntityCrossbowBolt(World worldIn, EntityLivingBase shooter, CrossbowItem gun, ItemStack stack) {
        super(worldIn, shooter, gun, stack);
        canPenetrateEntity = gun == ModRegistry.GRPGItems.CROSSBOW && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Ability.CROSSBOW_PENETRATOR);
    }

    @Override
    protected void damageTargetEntity(Entity target, boolean isHeadshot) {
        target.attackEntityFrom(new GunDamageSource(shooter, this, stack), damage);
    }

    @Override
    public boolean isLimitedLifetime() {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public void onUpdate() {
        if(ticksExisted > 1 && world.isRemote && !collided) previousPositions.add(getPositionVector());
        super.onUpdate();
    }
}
