package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.GrenadeLauncherRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class GrenadeLauncherItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("grenade_launcher/reload");

    public GrenadeLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> GrenadeLauncherRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.mk14)
                .ammo(WeaponCategory.GRENADE_LAUNCHER)
                    .define(AmmoMaterials.GRENADE)
                    .define(AmmoMaterials.TEAR_GAS)
                    .define(AmmoMaterials.STICKY)
                    .define(AmmoMaterials.HE_GRENADE)
                    .define(AmmoMaterials.IMPACT)
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.GRENADE_LAUNCHER_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.M203_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.M203_RIGHT;
    }
}
