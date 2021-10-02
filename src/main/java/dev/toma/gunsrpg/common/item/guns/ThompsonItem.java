package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.ThompsonRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class ThompsonItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("thompson/reload");

    public ThompsonItem(String name) {
        super(name, new Properties().setISTER(() -> ThompsonRenderer::new));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.UMP45_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SMG)
                .config(ModConfig.weaponConfig.thompson)
                .ammo()
                .build();
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.THOMPSON_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.THOMPSON_RIGHT;
    }
}
