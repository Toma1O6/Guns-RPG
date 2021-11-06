package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.Hk416Renderer;
import dev.toma.gunsrpg.client.render.item.WinchesterRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class WinchesterItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("winchester/reload");

    public WinchesterItem(String name) {
        super(name, new Properties().setISTER(() -> WinchesterRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.winchester)
                .ammo(WeaponCategory.SR)
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.SKS_ASSEMBLY;
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
