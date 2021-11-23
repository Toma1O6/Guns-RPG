package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.GrenadeLauncherRenderer;
import dev.toma.gunsrpg.client.render.item.RocketLauncherRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class RocketLauncherItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("rocket_launcher/reload");

    public RocketLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> RocketLauncherRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.mk14)
                .ammo(WeaponCategory.SR)
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.KAR98K_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.ROCKET_LAUNCHER_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.ROCKET_LAUNCHER_RIGHT;
    }
}
