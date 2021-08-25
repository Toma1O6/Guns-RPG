package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.AnimationPaths;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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

    public static final AnimationType<AimAnimation> AIM_ANIMATION = AnimationType.create(GunsRPG.makeResource("aim_animation"));
    public static final AnimationType<SprintAnimation> SPRINT = AnimationType.create(GunsRPG.makeResource("sprinting"), SprintAnimation::new);
    public static final AnimationType<IAnimation> CHAMBER = AnimationType.create(GunsRPG.makeResource("chamber"));
    public static final AnimationType<Animation> HEAL = AnimationType.create(GunsRPG.makeResource("heal"));
    public static final AnimationType<Animation> RELOAD = AnimationType.create(GunsRPG.makeResource("reload"), ModAnimations::createReloadAnimation);
    public static final AnimationType<Animation> FIREMODE = AnimationType.create(GunsRPG.makeResource("firemode"), ModAnimations::createFiremodeAnimation);
    public static final AnimationType<AnimationList<BulletEjectAnimation>> BULLET_EJECTION = AnimationType.create(GunsRPG.makeResource("bullet_eject"), AnimationList::new);

    public static Animation createReloadAnimation(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            GunItem item = (GunItem) stack.getItem();
            ResourceLocation animationPath = item.getReloadAnimation(player);
            AnimationEngine engine = AnimationEngine.get();
            IAnimationLoader loader = engine.loader();
            return new Animation(loader.getProvider(animationPath), item.getReloadTime(player));
        }
        return null;
    }

    public static Animation createFiremodeAnimation(PlayerEntity client) {
        AnimationEngine engine = AnimationEngine.get();
        IAnimationLoader loader = engine.loader();
        IKeyframeProvider provider = loader.getProvider(AnimationPaths.FIREMODE_PATH);
        return new Animation(provider, 5);
    }
}
