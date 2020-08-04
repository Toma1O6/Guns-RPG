package dev.toma.gunsrpg.common.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityGrenade extends Entity implements IEntityAdditionalSpawnData {

    public static final float AIR_DRAG_MODIFIER = 0.98F;
    public static final float GROUND_DRAG_MODIFIER = 0.7F;
    public static final float BOUNCE_MODIFIER = 0.25F;
    public int fuse;
    public float rotation;
    public float lastRotation;
    public int timesBounced = 0;
    private int blastRadius;
    private boolean explodesOnImpact;
    public ItemStack stack;

    public EntityGrenade(World world) {
        this(world, null);
    }

    public EntityGrenade(World world, EntityLivingBase thrower) {
        this(world, thrower, 100, 3, false, ItemStack.EMPTY);
    }

    public EntityGrenade(World world, EntityLivingBase thrower, int time, int blastRadius, boolean explodesOnImpact, ItemStack stack) {
        super(world);
        this.setSize(0.2F, 0.2F);
        if(thrower != null) this.setPosition(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ);
        this.fuse = time;
        this.blastRadius = blastRadius;
        this.explodesOnImpact = explodesOnImpact;
        this.setInitialMotion(thrower);
        this.stack = stack;
    }

    public void doExplosion() {
        if(!this.world.isRemote) {
            world.createExplosion(null, posX, posY + 0.5, posZ, blastRadius, false);
            setDead();
        }
    }

    @Override
    public void onUpdate() {
        --this.fuse;
        if(fuse < 0) {
            doExplosion();
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        double prevMotionX = motionX;
        double prevMotionY = motionY;
        double prevMotionZ = motionZ;
        if(!world.isRemote) {
            Vec3d from = this.getPositionVector();
            Vec3d to = from.addVector(motionX, motionY, motionZ);
            RayTraceResult rayTraceResult = this.world.rayTraceBlocks(from, to, false, true, false);
            if(rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.onCollide(from, to, rayTraceResult);
            }
        }
        this.move(MoverType.SELF, motionX, motionY, motionZ);
        if(motionX != prevMotionX) {
            this.motionX = -BOUNCE_MODIFIER * prevMotionX;
            this.onGrenadeBounce();
        }
        if(motionY != prevMotionY) {
            this.motionY = -BOUNCE_MODIFIER * prevMotionY;
            this.onGrenadeBounce();
        }
        if(motionZ != prevMotionZ) {
            this.motionZ = -BOUNCE_MODIFIER * prevMotionZ;
            this.onGrenadeBounce();
        }
        if(!this.hasNoGravity()) {
            this.motionY -= 0.039D;
        }
        this.motionX *= AIR_DRAG_MODIFIER;
        this.motionY *= AIR_DRAG_MODIFIER;
        this.motionZ *= AIR_DRAG_MODIFIER;
        if(this.onGround) {
            this.motionX *= GROUND_DRAG_MODIFIER;
            this.motionY *= GROUND_DRAG_MODIFIER;
            this.motionZ *= GROUND_DRAG_MODIFIER;
        }
        this.lastRotation = this.rotation;
        if(world.isRemote && !this.onGround) {
            if(this.motionX != 0 && this.motionY != 0 && this.motionZ != 0) {
                this.rotation += 45F;
            }
        }

        this.onThrowableTick();
    }

    public final void onCollide(Vec3d from, Vec3d to, RayTraceResult result) {
        BlockPos pos = result.getBlockPos();
        IBlockState state = this.world.getBlockState(pos);
        boolean hasBrokenGlass = false;
        if(state.getMaterial() == Material.GLASS) {
            world.destroyBlock(pos, false);
            hasBrokenGlass = true;
        }
        if(hasBrokenGlass) {
            RayTraceResult rayTraceResult = this.world.rayTraceBlocks(from, to, false, true, false);
            if(rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.onCollide(from, to, rayTraceResult);
            }
        }
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
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
    public void writeSpawnData(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        this.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("fuse", this.fuse);
        compound.setInteger("timesBounced", this.timesBounced);
        compound.setInteger("blast", blastRadius);
        compound.setBoolean("impact", explodesOnImpact);
        NBTTagCompound stack = new NBTTagCompound();
        this.stack.writeToNBT(stack);
        compound.setTag("itemstack", stack);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.fuse = compound.getInteger("fuse");
        this.timesBounced = compound.getInteger("timesBounced");
        this.blastRadius = compound.getInteger("blast");
        this.explodesOnImpact = compound.getBoolean("impact");
        if(compound.hasKey("itemstack")) {
            this.stack = new ItemStack(compound.getCompoundTag("itemstack"));
        }
    }

    private void setInitialMotion(EntityLivingBase thrower) {
        if (thrower == null) {
            return;
        }
        float sprintModifier = 1.25F;
        float modifier = 1.4F;
        if(thrower.isSprinting()) modifier *= sprintModifier;
        Vec3d viewVec = thrower.getLookVec();
        this.motionX = viewVec.x * modifier;
        this.motionY = viewVec.y * modifier / sprintModifier;
        this.motionZ = viewVec.z * modifier;
    }

    private void onGrenadeBounce() {
        if(Math.sqrt(motionX*motionX+motionY*motionY+motionZ*motionZ) >= 0.2) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.MASTER, 1.0F, 1.8F);
        }
        this.timesBounced++;
        this.bounce();
    }
}
