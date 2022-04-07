package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.properties.IPropertySerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Arrays;

public final class TracerInfo {

    private final Vector3d[] positions;
    private final int rgb;
    private int lifeTime;

    private TracerInfo(int length, int rgb) {
        this.positions = new Vector3d[length];
        this.rgb = rgb;
    }

    public static TracerInfo of(int rgb) {
        return of(3, rgb);
    }

    public static TracerInfo of(int length, int rgb) {
        return new TracerInfo(length, rgb);
    }

    public TracerInfo copy() {
        return new TracerInfo(positions.length, rgb);
    }

    public void tick(Vector3d pos) {
        ModUtils.shift(pos, positions);
        ++lifeTime;
    }

    public Vector3d[] getPositions() {
        return positions;
    }

    public int getRgb() {
        return rgb;
    }

    public static class Serializer implements IPropertySerializer<TracerInfo> {

        @Override
        public void encode(PacketBuffer buffer, TracerInfo value) {
            buffer.writeVarInt(value.positions.length);
            buffer.writeInt(value.rgb);
        }

        @Override
        public TracerInfo decode(PacketBuffer buffer) {
            int length = buffer.readVarInt();
            int rgb = buffer.readInt();
            return new TracerInfo(length, rgb);
        }
    }
}
