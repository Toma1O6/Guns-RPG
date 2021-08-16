package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.AnimationPaths;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import lib.toma.animations.api.Animation;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationLoader;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.AnimationType;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class ModAnimations {
    private ModAnimations() {}

    public static final AnimationStage DUAL_WIELD_ITEM = new AnimationStage(GunsRPG.makeResource("item.heldfp.dual"));

    public static final AnimationType<AimAnimation> AIM_ANIMATION = new AnimationType<>(GunsRPG.makeResource("aim_animation"));
    public static final AnimationType<SprintAnimation> SPRINT = new AnimationType<>(GunsRPG.makeResource("sprinting"), SprintAnimation::new);
    public static final AnimationType<IAnimation> CHAMBER = new AnimationType<>(GunsRPG.makeResource("chamber"));
    public static final AnimationType<Animation> HEAL = new AnimationType<>(GunsRPG.makeResource("heal"));
    public static final AnimationType<Animation> RELOAD = new AnimationType<>(GunsRPG.makeResource("reload"), ModAnimations::createReloadAnimation);
    public static final AnimationType<Animation> FIREMODE = new AnimationType<>(GunsRPG.makeResource("firemode"), ModAnimations::createFiremodeAnimation);

    public static void init() {
        // loads class
    }

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
