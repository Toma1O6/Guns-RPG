package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.ChuKoNuRenderer;
import dev.toma.gunsrpg.client.render.item.RocketLauncherRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class ChuKoNuItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("chukonu/reload");

    public ChuKoNuItem(String name) {
        super(name, new Properties().setISTER(() -> ChuKoNuRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.chukonu)
                .ammo(WeaponCategory.CROSSBOW)
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CROSSBOW_ASSEMBLY;
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
