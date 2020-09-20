package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.GRPGSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityFlare extends Entity implements IEntityAdditionalSpawnData {

    private int startHeight;
    private int timeWaiting;

    public EntityFlare(World world) {
        super(world);
        setSize(0.2F, 0.2F);
        setInvisible(true);
    }

    public EntityFlare(World world, EntityPlayer owner) {
        this(world);
        setPosition(owner.posX, owner.posY, owner.posZ);
        startHeight = (int) posY;
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeInt(startHeight);
        buf.writeInt(timeWaiting);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        startHeight = buf.readInt();
        timeWaiting = buf.readInt();
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("startHeight", startHeight);
        compound.setInteger("timeWaiting", timeWaiting);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        startHeight = compound.getInteger("startHeight");
        timeWaiting = compound.getInteger("timeWaiting");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Vec3d start = this.getPositionVector();
        Vec3d end = start.add(new Vec3d(motionX, motionY, motionZ));
        RayTraceResult traceResult = world.rayTraceBlocks(start, end, false, true, false);
        if(traceResult != null && traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            setDead();
        }
        if(world.isRemote) {
            world.spawnParticle(EnumParticleTypes.CLOUD, true, posX, posY, posZ, 0, -0.25, 0);
            world.spawnParticle(EnumParticleTypes.CLOUD, true, posX, posY, posZ, 0, -0.15, 0);
            world.spawnParticle(EnumParticleTypes.CLOUD, true, posX, posY, posZ, 0, -0.05, 0);
        }
        boolean reachedHeight = posY >= startHeight + 125;
        if(motionY > 0) motionY = Math.max(0, motionY - 0.025);
        if(!reachedHeight) {
            motionY = 0.55D;
        } else {
            if(++timeWaiting >= 100) {
                if(!world.isRemote) {
                    setDead();
                    EntityAirdrop airdrop = new EntityAirdrop(world);
                    airdrop.setPosition(posX, posY - 10, posZ);
                    world.spawnEntity(airdrop);
                    world.playSound(null, posX, posY - 130, posZ, GRPGSounds.PLANE_FLY_BY, SoundCategory.MASTER, 7.0F, 1.0F);
                }
            }
        }
        move(MoverType.SELF, motionX, motionY, motionZ);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }
}
