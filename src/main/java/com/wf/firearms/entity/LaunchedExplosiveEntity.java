package com.wf.firearms.entity;

import com.wf.firearms.grenade.GrenadeExplosionHelper;
import com.wf.firearms.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

/** 榴弹/火箭发射器射出的爆炸弹体（抛物线榴弹 or 直线火箭）。 */
public class LaunchedExplosiveEntity extends ThrowableItemProjectile implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<ItemStack> DISPLAY_ITEM =
            SynchedEntityData.defineId(LaunchedExplosiveEntity.class, EntityDataSerializers.ITEM_STACK);

    private int fuseTicks;
    private float blastRadius = 3f;
    private float explosionDamage = 24f;
    private boolean explodeOnImpact;
    private boolean rocketMode;
    private boolean incendiary;
    private boolean toxic;

    public LaunchedExplosiveEntity(EntityType<? extends LaunchedExplosiveEntity> type, Level level) {
        super(type, level);
    }

    public LaunchedExplosiveEntity(
            Level level,
            LivingEntity shooter,
            ItemStack visual,
            int fuseTicks,
            float blastRadius,
            float explosionDamage,
            boolean explodeOnImpact,
            boolean rocketMode,
            boolean incendiary,
            boolean toxic) {
        super(ModEntities.LAUNCHED_EXPLOSIVE.get(), shooter, level);
        ItemStack display = visual.isEmpty() ? ItemStack.EMPTY : visual.copy();
        setItem(display);
        entityData.set(DISPLAY_ITEM, display);
        this.fuseTicks = fuseTicks;
        this.blastRadius = blastRadius;
        this.explosionDamage = explosionDamage;
        this.explodeOnImpact = explodeOnImpact;
        this.rocketMode = rocketMode;
        this.incendiary = incendiary;
        this.toxic = toxic;
        setNoGravity(rocketMode);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DISPLAY_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (!explodeOnImpact && !rocketMode) {
                if (fuseTicks > 0) {
                    fuseTicks--;
                    if (fuseTicks <= 0) {
                        explode();
                        return;
                    }
                }
            }
            if (!rocketMode) {
                Vec3 motion = getDeltaMovement();
                if (motion.lengthSqr() > 1.0E-8) {
                    setDeltaMovement(motion.scale(0.99));
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
        if (explodeOnImpact || rocketMode) {
            explode();
        } else if (result.getType() == HitResult.Type.BLOCK || result.getType() == HitResult.Type.ENTITY) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.scale(0.35));
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (level().isClientSide) {
            return;
        }
        if (explodeOnImpact || rocketMode) {
            explode();
        } else {
            setDeltaMovement(getDeltaMovement().scale(0.35));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide) {
            return;
        }
        if (explodeOnImpact || rocketMode) {
            explode();
        } else {
            setDeltaMovement(getDeltaMovement().scale(0.35));
        }
    }

    private void explode() {
        if (level().isClientSide || isRemoved()) {
            return;
        }
        GrenadeExplosionHelper.explode(level(), this, getOwner(), position(), blastRadius, explosionDamage);
        if (incendiary && level() instanceof net.minecraft.server.level.ServerLevel server) {
            BlockPos pos = blockPosition();
            if (server.getBlockState(pos).isAir()) {
                server.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState());
            }
        }
        if (toxic) {
            double r = blastRadius * 1.5;
            for (LivingEntity living :
                    level().getEntitiesOfClass(
                            LivingEntity.class,
                            getBoundingBox().inflate(r),
                            e -> e != getOwner() && e.isAlive())) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 0));
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80));
            }
        }
        discard();
    }

    public ItemStack getDisplayItem() {
        ItemStack synced = entityData.get(DISPLAY_ITEM);
        return synced.isEmpty() ? getItem() : synced;
    }

    @Override
    protected Item getDefaultItem() {
        ItemStack stack = entityData.get(DISPLAY_ITEM);
        return stack.isEmpty() ? Items.TNT : stack.getItem();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Fuse", fuseTicks);
        tag.putFloat("Blast", blastRadius);
        tag.putFloat("Damage", explosionDamage);
        tag.putBoolean("Impact", explodeOnImpact);
        tag.putBoolean("Rocket", rocketMode);
        tag.putBoolean("Incendiary", incendiary);
        tag.putBoolean("Toxic", toxic);
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
        if (tag.contains("Rocket")) {
            rocketMode = tag.getBoolean("Rocket");
            setNoGravity(rocketMode);
        }
        if (tag.contains("Incendiary")) {
            incendiary = tag.getBoolean("Incendiary");
        }
        if (tag.contains("Toxic")) {
            toxic = tag.getBoolean("Toxic");
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
        buffer.writeBoolean(rocketMode);
        buffer.writeBoolean(incendiary);
        buffer.writeBoolean(toxic);
        buffer.writeItem(getItem());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        fuseTicks = buffer.readVarInt();
        blastRadius = buffer.readFloat();
        explosionDamage = buffer.readFloat();
        explodeOnImpact = buffer.readBoolean();
        rocketMode = buffer.readBoolean();
        incendiary = buffer.readBoolean();
        toxic = buffer.readBoolean();
        ItemStack visual = buffer.readItem();
        setItem(visual);
        entityData.set(DISPLAY_ITEM, visual);
        setNoGravity(rocketMode);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
