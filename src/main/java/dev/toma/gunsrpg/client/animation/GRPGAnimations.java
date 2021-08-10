package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.GunsRPG;
import lib.toma.animations.pipeline.AnimationType;
import lib.toma.animations.pipeline.IAnimation;

public final class GRPGAnimations {
    private GRPGAnimations() {}

    public static final AnimationType<AimAnimation> AIM_ANIMATION = new AnimationType<>(GunsRPG.makeResource("aim_animation"));
    public static final AnimationType<SprintAnimation> SPRINT = new AnimationType<>(GunsRPG.makeResource("sprinting"), SprintAnimation::new);
    public static final AnimationType<IAnimation> CHAMBER = new AnimationType<>(GunsRPG.makeResource("chamber"));
}
