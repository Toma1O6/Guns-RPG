package dev.toma.gunsrpg.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class CPacketParticle implements IMessage {

    private SpawnType spawnType;
    private EnumParticleTypes particleType;
    private Vec3d pos;
    private Vec3d speed;
    private int data;
    private int amount;
    private int modifier;

    public static CPacketParticle createPacket(SpawnType type, EnumParticleTypes particleType, Vec3d pos, Vec3d speed, int data, int amount, int modifier) {
        return new CPacketParticle(type, particleType, pos, speed, data, amount, modifier);
    }

    public static CPacketParticle singleParticle(EnumParticleTypes particleType, double x, double y, double z, double speedX, double speedY, double speedZ, int data) {
        return new CPacketParticle(SpawnType.SINGLE, particleType, new Vec3d(x, y, z), new Vec3d(speedX, speedY, speedZ), data, 0, 0);
    }

    public static CPacketParticle multipleParticles(EnumParticleTypes particleType, double x, double y, double z, int data, int count, int modifier) {
        return new CPacketParticle(SpawnType.MULTIPLE, particleType, new Vec3d(x, y, z), new Vec3d(0, 0, 0), data, count, modifier);
    }

    public static CPacketParticle bullet(Vec3d vec3d, int blockData, EnumFacing facing) {
        return new CPacketParticle(SpawnType.BULLET, EnumParticleTypes.BLOCK_CRACK, vec3d, Vec3d.ZERO, blockData, facing.getIndex(), 0);
    }

    public CPacketParticle() {

    }

    private CPacketParticle(SpawnType type, EnumParticleTypes particleType, Vec3d pos, Vec3d speed, int data, int amount, int modifier) {
        this.spawnType = type;
        this.particleType = particleType;
        this.pos = pos;
        this.speed = speed;
        this.data = data;
        this.amount = amount;
        this.modifier = modifier;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(spawnType.ordinal());
        buf.writeInt(particleType.ordinal());
        this.writeVec(buf, pos);
        this.writeVec(buf, speed);
        buf.writeInt(data);
        buf.writeInt(amount);
        buf.writeInt(modifier);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        spawnType = SpawnType.values()[buf.readInt()];
        particleType = EnumParticleTypes.values()[buf.readInt()];
        pos = readVec(buf);
        speed = readVec(buf);
        data = buf.readInt();
        amount = buf.readInt();
        modifier = buf.readInt();
    }

    private void writeVec(ByteBuf buf, Vec3d vec3d) {
        buf.writeDouble(vec3d.x);
        buf.writeDouble(vec3d.y);
        buf.writeDouble(vec3d.z);
    }

    private Vec3d readVec(ByteBuf buf) {
        return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public enum SpawnType {
        SINGLE,
        MULTIPLE,
        BULLET
    }

    public static class Handler implements IMessageHandler<CPacketParticle, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketParticle message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                World world = mc.world;
                Vec3d pos = message.pos;
                Vec3d mot = message.speed;
                switch (message.spawnType) {
                    case SINGLE:
                        world.spawnParticle(message.particleType, pos.x, pos.y, pos.z, mot.x, mot.y, mot.z, message.data);
                        break;

                    case MULTIPLE: {
                        Random r = world.rand;
                        int mod = message.modifier;
                        boolean f = mod != 0;
                        for (int i = 0; i < message.amount; i++) {
                            double xs = f ? r.nextDouble() / mod - r.nextDouble() / mod : r.nextDouble() - r.nextDouble();
                            double ys = f ? r.nextDouble() / mod - r.nextDouble() / mod : r.nextDouble() - r.nextDouble();
                            double zs = f ? r.nextDouble() / mod - r.nextDouble() / mod : r.nextDouble() - r.nextDouble();
                            world.spawnParticle(message.particleType, pos.x, pos.y, pos.z, xs, ys, zs, message.data);
                        }
                        break;
                    }

                    case BULLET:
                        Random random = world.rand;
                        double mod = 5;
                        boolean f = mod != 0;
                        EnumFacing facing = EnumFacing.values()[message.amount];
                        for (int i = 0; i < 10; i++) {
                            Vec3i vec3i = facing.getDirectionVec();
                            double xs = vec3i.getX();
                            double ys = vec3i.getY();
                            double zs = vec3i.getZ();
                            world.spawnParticle(message.particleType, true, pos.x, pos.y, pos.z, xs, ys, zs, message.data);
                        }
                        break;
                }
            });
            return null;
        }
    }
}
