package com.wf.firearms.entity;

import com.wf.firearms.grenade.GrenadeExplosionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 手雷投掷物。不能调用雪球/鸡蛋的 {@code super.onHitBlock}（会立刻 discard），引信在服务端倒计时后爆炸。
 */
public class GrenadeEntity extends ThrowableItemProjectile implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<ItemStack> DISPLAY_ITEM =
            SynchedEntityData.defineId(GrenadeEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> RESTING =
            SynchedEntityData.defineId(GrenadeEntity.class, EntityDataSerializers.BOOLEAN);

    private int fuseTicks = 60;
    private float blastRadius = 2.5f;
    private float explosionDamage = 20f;
    private boolean explodeOnImpact;

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, Level level) {
        super(type, level);
    }

    public GrenadeEntity(
            EntityType<? extends GrenadeEntity> type,
            Level level,
            LivingEntity thrower,
            int fuseTicks,
            float blastRadius,
            float explosionDamage,
            boolean explodeOnImpact,
            Item renderItem) {
        super(type, thrower, level);
        ItemStack visual = renderItem != null && renderItem != Items.AIR ? new ItemStack(renderItem) : ItemStack.EMPTY;
        setItem(visual);
        this.fuseTicks = fuseTicks;
        this.blastRadius = blastRadius;
        this.explosionDamage = explosionDamage;
        this.explodeOnImpact = explodeOnImpact;
        entityData.set(DISPLAY_ITEM, visual);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DISPLAY_ITEM, ItemStack.EMPTY);
        entityData.define(RESTING, false);
    }

    private boolean isResting() {
        return entityData.get(RESTING);
    }

    @Override
    public void tick() {
        if (isResting()) {
            setDeltaMovement(Vec3.ZERO);
            setNoGravity(true);
        }
        if (!level().isClientSide) {
            if (fuseTicks <= 0) {
                explode();
                return;
            }
            fuseTicks--;
            if (!isResting()) {
                Vec3 motion = getDeltaMovement();
                if (motion.lengthSqr() > 1.0E-8) {
                    setDeltaMovement(motion.scale(0.985));
                }
                if (onGround() && motion.y <= 0.02) {
                    settleOnTopOfBlock(getBlockPosBelowThatAffectsMyMovement());
                }
            }
        }
        super.tick();
    }

    @Override
    protected void onHit(HitResult result) {
        if (level().isClientSide) {
            return;
        }
        if (explodeOnImpact) {
            explode();
            return;
        }
        if (result.getType() == HitResult.Type.BLOCK) {
            dampenOnBlock((BlockHitResult) result);
        } else if (result.getType() == HitResult.Type.ENTITY) {
            dampenOnEntity((EntityHitResult) result);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (level().isClientSide) {
            return;
        }
        if (explodeOnImpact) {
            explode();
            return;
        }
        dampenOnBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide) {
            return;
        }
        if (explodeOnImpact) {
            explode();
            return;
        }
        dampenOnEntity(result);
    }

    /** 碰地/碰墙：顶面落地则锁定位置；侧面仅减速弹开。 */
    private void dampenOnBlock(BlockHitResult result) {
        if (result.getDirection() == Direction.UP) {
            settleOnTopOfBlock(result.getBlockPos());
            return;
        }
        if (isResting()) {
            return;
        }
        Vec3 motion = getDeltaMovement();
        Direction face = result.getDirection();
        double mx = motion.x * 0.42;
        double my = motion.y * 0.42;
        double mz = motion.z * 0.42;
        if (face == Direction.DOWN) {
            my = Math.min(0.05, Math.abs(my) * 0.15);
        } else {
            mx += face.getStepX() * 0.14;
            my += face.getStepY() * 0.14;
            mz += face.getStepZ() * 0.14;
        }
        setDeltaMovement(mx, my, mz);
    }

    /** 落在方块顶面：贴地静止，引信倒计时期间不再下落（同步到客户端）。 */
    private void settleOnTopOfBlock(BlockPos blockOn) {
        entityData.set(RESTING, true);
        double surfaceY = blockOn.getY() + 1.001;
        setPos(getX(), surfaceY, getZ());
        setDeltaMovement(Vec3.ZERO);
        setNoGravity(true);
    }

    private void dampenOnEntity(EntityHitResult result) {
        if (isResting()) {
            return;
        }
        Vec3 motion = getDeltaMovement();
        setDeltaMovement(motion.x * 0.35, motion.y * 0.35, motion.z * 0.35);
    }

    private void explode() {
        if (level().isClientSide || isRemoved()) {
            return;
        }
        GrenadeExplosionHelper.explode(
                level(), this, getOwner(), position(), blastRadius, explosionDamage);
        discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Fuse", fuseTicks);
        tag.putFloat("Blast", blastRadius);
        tag.putFloat("Damage", explosionDamage);
        tag.putBoolean("Impact", explodeOnImpact);
        tag.putBoolean("Resting", isResting());
        ItemStack display = entityData.get(DISPLAY_ITEM);
        if (!display.isEmpty()) {
            tag.putString("Item", ForgeRegistries.ITEMS.getKey(display.getItem()).toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fuse")) {
            fuseTicks = tag.getInt("Fuse");
        }
        if (tag.contains("Blast")) {
            blastRadius = tag.getFloat("Blast");
        }
        if (tag.contains("Damage")) {
            explosionDamage = tag.getFloat("Damage");
        }
        if (tag.contains("Impact")) {
            explodeOnImpact = tag.getBoolean("Impact");
        }
        if (tag.contains("Resting")) {
            entityData.set(RESTING, tag.getBoolean("Resting"));
            if (isResting()) {
                setDeltaMovement(Vec3.ZERO);
                setNoGravity(true);
            }
        }
        if (tag.contains("Item")) {
            var id = net.minecraft.resources.ResourceLocation.tryParse(tag.getString("Item"));
            if (id != null) {
                Item item = ForgeRegistries.ITEMS.getValue(id);
                if (item != null) {
                    ItemStack visual = new ItemStack(item);
                    setItem(visual);
                    entityData.set(DISPLAY_ITEM, visual);
                }
            }
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(fuseTicks);
        buffer.writeFloat(blastRadius);
        buffer.writeFloat(explosionDamage);
        buffer.writeBoolean(explodeOnImpact);
        buffer.writeBoolean(isResting());
        buffer.writeItem(getItem());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        fuseTicks = buffer.readVarInt();
        blastRadius = buffer.readFloat();
        explosionDamage = buffer.readFloat();
        explodeOnImpact = buffer.readBoolean();
        entityData.set(RESTING, buffer.readBoolean());
        ItemStack visual = buffer.readItem();
        setItem(visual);
        entityData.set(DISPLAY_ITEM, visual);
        if (isResting()) {
            setDeltaMovement(Vec3.ZERO);
            setNoGravity(true);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        ItemStack stack = entityData.get(DISPLAY_ITEM);
        return stack.isEmpty() ? Items.TNT : stack.getItem();
    }

    public ItemStack getDisplayItem() {
        ItemStack synced = entityData.get(DISPLAY_ITEM);
        return synced.isEmpty() ? getItem() : synced;
    }
}
