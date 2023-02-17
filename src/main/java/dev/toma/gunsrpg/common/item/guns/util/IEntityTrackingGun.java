package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.properties.IPropertySerializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public interface IEntityTrackingGun extends IAdditionalShootData {

    boolean canBeGuided(PlayerEntity player);

    int getMaxRange();

    int getLockTime();

    class GuidenanceProperties {

        public static final GuidenanceProperties PLAYER = new GuidenanceProperties(7.0F, 7.0F, 4.0F, 75.0F);
        public static final GuidenanceProperties GUNNER_AIR = new GuidenanceProperties(6.0F, 6.0F, 8.0F, 90.0F);
        public static final GuidenanceProperties GUNNER_GROUND = new GuidenanceProperties(0.5F, 1.3F, 0.0F, 90.0F);
        public static final GuidenanceProperties TURRET = new GuidenanceProperties(8.5F, 8.5F, 16.0F, 90.0F);
        public static final IPropertySerializer<GuidenanceProperties> SERIALIZER = new Serializer();

        private final float maxPitch;
        private final float maxYaw;
        private final float maxProximityDistanceSqr;
        private final float maxGuidenanceAngle;

        public GuidenanceProperties(float maxPitch, float maxYaw, float maxProximityDistanceSqr, float maxGuidenanceAngle) {
            this.maxPitch = maxPitch;
            this.maxYaw = maxYaw;
            this.maxProximityDistanceSqr = maxProximityDistanceSqr;
            this.maxGuidenanceAngle = maxGuidenanceAngle;
        }

        public float getMaxPitch() {
            return maxPitch;
        }

        public float getMaxYaw() {
            return maxYaw;
        }

        public float getMaxProximityDistanceSqr() {
            return maxProximityDistanceSqr;
        }

        public float getMaxGuidenanceAngle() {
            return maxGuidenanceAngle;
        }

        private static final class Serializer implements IPropertySerializer<GuidenanceProperties> {

            @Override
            public void encode(PacketBuffer buffer, GuidenanceProperties value) {
                buffer.writeFloat(value.maxPitch);
                buffer.writeFloat(value.maxYaw);
                buffer.writeFloat(value.maxProximityDistanceSqr);
                buffer.writeFloat(value.maxGuidenanceAngle);
            }

            @Override
            public GuidenanceProperties decode(PacketBuffer buffer) {
                return new GuidenanceProperties(
                        buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readFloat()
                );
            }
        }
    }
}
