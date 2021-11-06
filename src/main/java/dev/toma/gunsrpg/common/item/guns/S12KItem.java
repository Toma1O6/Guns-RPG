package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.S12KRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class S12KItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("s12k/reload");

    public S12KItem(String name) {
        super(name, new Properties().setISTER(() -> S12KRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SG)
                .config(ModConfig.weaponConfig.s12k)
                .ammo()
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S1897_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return super.left();
    }

    @Override
    public IRenderConfig right() {
        return super.right();
    }
}
