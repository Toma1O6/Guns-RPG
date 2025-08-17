package dev.toma.gunsrpg.config.gun;

public class RecoilParameters {

    public static final RecoilParameters DEFAULT = new RecoilParameters();

    private float maxRecoilYaw;
    private float maxRecoilPitch;
    private float maxKick;
    private float recoilDecay;

    public RecoilParameters(float maxRecoilYaw, float maxRecoilPitch, float maxKick, float recoilDecay) {
        this.maxRecoilYaw = maxRecoilYaw;
        this.maxRecoilPitch = maxRecoilPitch;
        this.maxKick = maxKick;
        this.recoilDecay = recoilDecay;
    }

    public RecoilParameters() {
        this(5.0F, 8.0F, 2.5F, 0.8F);
    }

    public RecoilParameters limit(float yaw, float pitch) {
        this.maxRecoilYaw = yaw;
        this.maxRecoilPitch = pitch;
        return this;
    }

    public RecoilParameters yaw(float yaw) {
        this.maxRecoilYaw = yaw;
        return this;
    }

    public RecoilParameters pitch(float pitch) {
        this.maxRecoilPitch = pitch;
        return this;
    }

    public RecoilParameters kick(float kick) {
        this.maxKick = kick;
        return this;
    }

    public RecoilParameters decay(float decay) {
        this.recoilDecay = decay;
        return this;
    }

    public float maxYaw() {
        return maxRecoilYaw;
    }

    public float maxPitch() {
        return maxRecoilPitch;
    }

    public float maxKick() {
        return maxKick;
    }

    public float decay() {
        return recoilDecay;
    }
}
