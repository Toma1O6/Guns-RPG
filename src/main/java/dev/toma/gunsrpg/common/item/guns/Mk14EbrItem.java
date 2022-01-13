package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Mk14EbrRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class Mk14EbrItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("mk14ebr/reload");

    public Mk14EbrItem(String name) {
        super(name, new Properties().setISTER(() -> Mk14EbrRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.mk14)
                .ammo(WeaponCategory.DMR)
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 5)
                    .define(AmmoMaterials.LAPIS, 5)
                    .define(AmmoMaterials.GOLD, 8)
                    .define(AmmoMaterials.REDSTONE, 8)
                    .define(AmmoMaterials.DIAMOND, 11)
                    .define(AmmoMaterials.QUARTZ, 11)
                    .define(AmmoMaterials.EMERALD, 15)
                    .define(AmmoMaterials.AMETHYST, 18)
                    .define(AmmoMaterials.NETHERITE, 22)
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
        return RenderConfigs.MK14_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.MK14_RIGHT;
    }
}
