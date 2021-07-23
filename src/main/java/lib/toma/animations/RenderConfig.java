package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderConfig extends PositionConfig {

    protected Vector3f scale;
    protected Quaternion rotation;

    protected RenderConfig(Vector3d position, Vector3f scale, Quaternion rotation) {
        super(position);
        this.scale = scale;
        this.rotation = rotation;
    }

    private RenderConfig(RenderConfigBuilder builder) {
        super(builder.pos);
        this.scale = builder.scale;
        this.rotation = builder.rotation;
    }

    public static RenderConfigBuilder newDef() {
        return new RenderConfigBuilder();
    }

    public Mutable toMutable() {
        return new Mutable(pos, scale, rotation);
    }

    @Override
    public void applyTo(MatrixStack stack) {
        super.applyTo(stack);
        stack.scale(scale.x(), scale.y(), scale.z());
        stack.mulPose(rotation);
    }

    public static class Mutable extends RenderConfig {

        public Mutable() {
            this(new Vector3d(0, 0, 0), new Vector3f(1.0F, 1.0F, 1.0F), Quaternion.ONE.copy());
        }

        public Mutable(Vector3d position, Vector3f scale, Quaternion rotation) {
            super(position, scale, rotation);
        }

        public void setPosition(Vector3d position) {
            this.pos = position;
        }

        public void setScale(Vector3f scale) {
            this.scale = scale;
        }

        public void setRotation(Quaternion rotation) {
            this.rotation = rotation;
        }

        public void toDefaults() {
            setPosition(new Vector3d(0.0, 0.0, 0.0));
            setScale(new Vector3f(1.0F, 1.0F, 1.0F));
            Quaternion quat = Quaternion.ONE.copy();
            setRotation(quat);
        }

        @Override
        public Mutable toMutable() {
            return this;
        }

        public RenderConfig toImmutable() {
            return new RenderConfig(pos, scale, rotation);
        }
    }

    public static class RenderConfigBuilder {

        private Vector3d pos = Vector3d.ZERO;
        private Vector3f scale = new Vector3f(1.0F, 1.0F, 1.0F);
        private Quaternion rotation = Quaternion.ONE;

        private RenderConfigBuilder() {}

        public RenderConfigBuilder withPosition(Vector3d position) {
            this.pos = position;
            return this;
        }

        public RenderConfigBuilder withPosition(double x, double y, double z) {
            return withPosition(new Vector3d(x, y, z));
        }

        public RenderConfigBuilder withScale(Vector3f scale) {
            this.scale = scale;
            return this;
        }

        public RenderConfigBuilder withScale(float x, float y, float z) {
            return withScale(new Vector3f(x, y, z));
        }

        public RenderConfigBuilder withRotation(Quaternion rotation) {
            this.rotation = rotation;
            return this;
        }

        public RenderConfig finish() {
            return new RenderConfig(this);
        }
    }
}
