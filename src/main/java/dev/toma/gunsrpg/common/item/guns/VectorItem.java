package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VectorRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class VectorItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vector/reload");

    public VectorItem(String name) {
        super(name, new Properties().setISTER(() -> VectorRenderer::new));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.UMP45_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SMG)
                .config(ModConfig.weaponConfig.vector)
                .ammo()
                .build();
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.VECTOR_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.VECTOR_RIGHT;
    }
}
