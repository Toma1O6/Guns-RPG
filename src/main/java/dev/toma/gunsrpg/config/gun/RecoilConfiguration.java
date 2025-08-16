package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.config.Configurable;

public class RecoilConfiguration {

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 15.0F)
    @Configurable.Synchronized
    @Configurable.Gui.NumberFormat("0.0#")
    @Configurable.Comment("Max weapon recoil yaw angle")
    public float maxRecoilYaw;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 30.0F)
    @Configurable.Synchronized
    @Configurable.Gui.NumberFormat("0.0#")
    @Configurable.Comment("Max weapon recoil pitch angle")
    public float maxRecoilPitch;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 5.0F)
    @Configurable.Synchronized
    @Configurable.Gui.NumberFormat("0.0#")
    @Configurable.Comment("Max weapon Z axis offset")
    public float maxKick;

    @Configurable
    @Configurable.DecimalRange(min = 0.1, max = 0.95)
    @Configurable.Synchronized
    @Configurable.Gui.NumberFormat("0.0##")
    @Configurable.Comment("Exponential decay input value for weapon recovery")
    public float recoilDecay;

    public RecoilConfiguration(float maxRecoilYaw, float maxRecoilPitch, float maxKick, float recoilDecay) {
        this.maxRecoilYaw = maxRecoilYaw;
        this.maxRecoilPitch = maxRecoilPitch;
        this.maxKick = maxKick;
        this.recoilDecay = recoilDecay;
    }

    public RecoilConfiguration() {
        this(5.0F, 8.0F, 2.5F, 0.8F);
    }
}
