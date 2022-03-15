package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.client.IModifiableProgress;
import dev.toma.gunsrpg.common.AnimationPaths;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import lib.toma.animations.api.event.AnimationEventType;
import net.minecraft.entity.player.PlayerEntity;

public final class ModAnimations {
    private ModAnimations() {}

    public static final AnimationStage DUAL_WIELD_ITEM = AnimationStage.create(GunsRPG.makeResource("item.heldfp.dual"));
    public static final AnimationStage MAGAZINE = AnimationStage.create(GunsRPG.makeResource("weapon.magazine"));
    public static final AnimationStage SLIDE = AnimationStage.create(GunsRPG.makeResource("weapon.slide"));
    public static final AnimationStage CHARGING_HANDLE = AnimationStage.create(GunsRPG.makeResource("weapon.charging_handle"));
    public static final AnimationStage BULLET = AnimationStage.create(GunsRPG.makeResource("weapon.bullet"));
    public static final AnimationStage BOLT = AnimationStage.create(GunsRPG.makeResource("weapon.bolt"));
    public static final AnimationStage BOLT_CARRIER = AnimationStage.create(GunsRPG.makeResource("weapon.bolt_carrier"));
    public static final AnimationStage BARRELS = AnimationStage.create(GunsRPG.makeResource("weapon.barrels"));
    public static final AnimationStage BULLET_2 = AnimationStage.create(GunsRPG.makeResource("weapon.bullet2"));
    public static final AnimationStage BATTERY_COVER = AnimationStage.create(GunsRPG.makeResource("stash.detector.battery_cover"));
    public static final AnimationStage BATTERY = AnimationStage.create(GunsRPG.makeResource("stash.detector.battery"));

    public static final AnimationType<AimAnimation> AIM_ANIMATION = AnimationType.create(GunsRPG.makeResource("aim_animation"));
    public static final AnimationType<SprintAnimation> SPRINT = AnimationType.create(GunsRPG.makeResource("sprinting"), SprintAnimation::new);
    public static final AnimationType<IAnimation> CHAMBER = AnimationType.create(GunsRPG.makeResource("chamber"));
    public static final AnimationType<IAnimation> HEAL = AnimationType.create(GunsRPG.makeResource("heal"));
    public static final AnimationType<IModifiableProgress> RELOAD_BULLET = AnimationType.create(GunsRPG.makeResource("reload_bullet"));
    public static final AnimationType<IModifiableProgress> RELOAD = AnimationType.create(GunsRPG.makeResource("reload"));
    public static final AnimationType<Animation> FIREMODE = AnimationType.create(GunsRPG.makeResource("firemode"), ModAnimations::createFiremodeAnimation);
    public static final AnimationType<AnimationList<BulletEjectAnimation>> BULLET_EJECTION = AnimationType.create(GunsRPG.makeResource("bullet_eject"), AnimationList::newList);
    public static final AnimationType<RecoilAnimation> RECOIL = AnimationType.create(GunsRPG.makeResource("recoil"));
    public static final AnimationType<Animation> GRENADE = AnimationType.create(GunsRPG.makeResource("grenade"));
    public static final AnimationType<Animation> UNJAM = AnimationType.create(GunsRPG.makeResource("unjam"));
    public static final AnimationType<Animation> STASH_DETECTOR = AnimationType.create(GunsRPG.makeResource("stash_detector"));

    public static final AnimationEventType<StashDetectorEvent> STASH_DETECTOR_EVENT = new AnimationEventType<>(GunsRPG.makeResource("stash_detector"), new StashDetectorEvent.Serializer(), StashDetectorEvent.StashDetectorEventDialog::new);

    public static Animation createFiremodeAnimation(PlayerEntity client) {
        AnimationEngine engine = AnimationEngine.get();
        IAnimationLoader loader = engine.loader();
        IKeyframeProvider provider = loader.getProvider(AnimationPaths.FIREMODE_PATH);
        return new Animation(provider, 5);
    }
}
