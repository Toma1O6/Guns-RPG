package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Pebble extends Bullet {

    public Pebble(EntityType<? extends Pebble> type, World world) {
        super(type, world);
    }

    public Pebble(EntityType<? extends Pebble> type, World world, LivingEntity shooter) {
        super(type, world, shooter);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public void postTick() {
        setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.05, 0.0));
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int p_180426_9_, boolean p_180426_10_) {
        this.setPos(x, y, z);
        this.setRot(yRot, xRot);
    }

    @Override
    protected void aggroNearby(PlayerEntity player) {
    }
}
