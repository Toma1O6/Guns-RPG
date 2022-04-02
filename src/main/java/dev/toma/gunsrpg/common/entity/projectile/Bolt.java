package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Bolt extends Bullet {

    public Bolt(EntityType<? extends Bolt> type, World world) {
        super(type, world);
    }

    public Bolt(EntityType<? extends Bolt> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void postTick() {
        applyGravity(0.1F);
    }

    // TODO possibly spawn dummy unpickable arrow at hit location

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int p_180426_9_, boolean p_180426_10_) {
        this.setPos(x, y, z);
        this.setRot(yRot, xRot);
    }

    @Override
    protected boolean canPenetrateGlass() {
        return false;
    }

    @Override
    protected void aggroNearby(PlayerEntity player) {
    }
}
