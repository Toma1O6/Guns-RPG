package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class Pebble extends Bullet implements IEntityAdditionalSpawnData {

    private ItemStack ammoSource;

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
        applyGravity();
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int p_180426_9_, boolean p_180426_10_) {
        this.setPos(x, y, z);
        this.setRot(yRot, xRot);
    }

    @Override
    protected void aggroNearby(PlayerEntity player) {
    }

    public ItemStack getAmmoSource() {
        return ammoSource;
    }

    public void setAmmoSource(ItemStack ammoSource) {
        this.ammoSource = ammoSource;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        super.writeSpawnData(buffer);
        buffer.writeItemStack(ammoSource.isEmpty() ? ItemStack.EMPTY : ammoSource, true);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        super.readSpawnData(buffer);
        ammoSource = buffer.readItem();
    }
}
