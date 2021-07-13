package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class GrenadeEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final Vector3d AIR_DRAG_MULTIPLIER = new Vector3d(0.98, 0.98, 0.98);
    public static final Vector3d GROUND_DRAG_MULTIPLIER = new Vector3d(0.7, 0.7, 0.7);
    public static final float BOUNCE_MODIFIER = 0.25F;
    public int fuse;
    public float rotation;
    public float lastRotation;
    public int timesBounced = 0;
    private int blastRadius;
    private boolean explodesOnImpact;
    public Item item;
    public LazyOptional<ItemStack> renderStack = LazyOptional.of(() -> new ItemStack(item));

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, World world) {
        this(type, world, null, 100, 3, false, Items.AIR);
    }

    public GrenadeEntity(World world) {
        this(world, null);
    }

    public GrenadeEntity(World world, LivingEntity thrower) {
        this(GRPGEntityTypes.GRENADE.get(), world, thrower, 100, 3, false, Items.AIR);
    }

    public GrenadeEntity(EntityType<?> type, World world, LivingEntity thrower, int time, int blastRadius, boolean explodesOnImpact, Item item) {
        super(type, world);
        if(thrower != null) this.setPos(thrower.getX(), thrower.getY() + thrower.getEyeHeight(), thrower.getZ());
        this.fuse = time;
        this.blastRadius = blastRadius;
        this.explodesOnImpact = explodesOnImpact;
        this.setInitialMotion(thrower);
        this.item = item;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void doExplosion() {
        if(!level.isClientSide) {
            level.explode(null, getX(), getY() + 0.5, getZ(), blastRadius, Explosion.Mode.NONE);
            remove();
        }
    }

    @Override
    public void tick() {
        --this.fuse;
        if(fuse < 0) {
            doExplosion();
        }
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        Vector3d deltaMovement = getDeltaMovement();
        double prevMotionX = deltaMovement.x;
        double prevMotionY = deltaMovement.y;
        double prevMotionZ = deltaMovement.z;
        if(!level.isClientSide) {
            Vector3d from = this.position();
            Vector3d to = from.add(deltaMovement.x, deltaMovement.y, deltaMovement.z);
            RayTraceContext context = new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this);
            BlockRayTraceResult rayTraceResult = level.clip(context);
            if(rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                this.onCollide(from, to, rayTraceResult, context);
            }
        }
        this.move(MoverType.SELF, deltaMovement);
        deltaMovement = getDeltaMovement();
        boolean bouncedThisTick = false;
        double bounceX = deltaMovement.x;
        double bounceY = deltaMovement.y;
        double bounceZ = deltaMovement.z;
        if(deltaMovement.x != prevMotionX) {
            bounceX = -BOUNCE_MODIFIER * prevMotionX;
            bouncedThisTick = true;
        }
        if(deltaMovement.y != prevMotionY) {
            bounceY = -BOUNCE_MODIFIER * prevMotionY;
            bouncedThisTick = true;
        }
        if(deltaMovement.z != prevMotionZ) {
            bounceZ = -BOUNCE_MODIFIER * prevMotionZ;
            bouncedThisTick = true;
        }
        if (bouncedThisTick)
            onGrenadeBounce(getDeltaMovement());
        setDeltaMovement(bounceX, bounceY, bounceZ);
        if(!this.isNoGravity()) {
            setDeltaMovement(getDeltaMovement().add(0, -0.039, 0));
        }
        setDeltaMovement(getDeltaMovement().multiply(AIR_DRAG_MULTIPLIER));
        if(this.onGround) {
            setDeltaMovement(getDeltaMovement().multiply(GROUND_DRAG_MULTIPLIER));
        }
        this.lastRotation = this.rotation;
        if(level.isClientSide && !this.onGround) {
            if(!getDeltaMovement().equals(Vector3d.ZERO)) {
                this.rotation += 45F;
            }
        }

        this.onThrowableTick();
    }

    public final void onCollide(Vector3d from, Vector3d to, BlockRayTraceResult result, RayTraceContext traceContext) {
        BlockPos pos = result.getBlockPos();
        BlockState state = this.level.getBlockState(pos);
        boolean hasBrokenGlass = false;
        if(state.getMaterial() == Material.GLASS) {
            level.destroyBlock(pos, false);
            hasBrokenGlass = true;
        }
        if(hasBrokenGlass) {
            BlockRayTraceResult rayTraceResult = level.clip(traceContext);
            if(rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                this.onCollide(from, to, rayTraceResult, traceContext);
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        return true;
    }

    public void onThrowableTick() {

    }

    public void bounce() {
        if(explodesOnImpact) {
            doExplosion();
        }
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();
        addAdditionalSaveData(nbt);
        buffer.writeNbt(nbt);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        CompoundNBT nbt = additionalData.readNbt();
        readAdditionalSaveData(nbt);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("fuse", fuse);
        nbt.putInt("timesBounced", timesBounced);
        nbt.putInt("blast", blastRadius);
        nbt.putBoolean("impact", explodesOnImpact);
        nbt.putString("item", item.getRegistryName().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        fuse = nbt.getInt("fuse");
        timesBounced = nbt.getInt("timesBounced");
        blastRadius = nbt.getInt("blast");
        explodesOnImpact = nbt.getBoolean("impact");
        String registryName = nbt.getString("item");
        if (registryName == null || registryName.isEmpty())
            item = Items.AIR;
        else
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
    }

    @Override
    protected void defineSynchedData() {
    }

    private void setInitialMotion(LivingEntity thrower) {
        if (thrower == null) {
            return;
        }
        float sprintModifier = 1.25F;
        float modifier = 1.4F;
        if(thrower.isSprinting()) modifier *= sprintModifier;
        Vector3d viewVec = thrower.getLookAngle();
        setDeltaMovement(viewVec.x * modifier, viewVec.y * modifier / sprintModifier, viewVec.z * modifier);
    }

    private void onGrenadeBounce(Vector3d movement) {
        double x = sqr(movement.x);
        double y = sqr(movement.y);
        double z = sqr(movement.z);
        if(Math.sqrt(x*x+y*y+z*z) >= 0.2) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_BREAK, SoundCategory.MASTER, 1.0F, 1.8F);
        }
        this.timesBounced++;
        this.bounce();
    }

    private static double sqr(double n) {
        return n*n;
    }
}
