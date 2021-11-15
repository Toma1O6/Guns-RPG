package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.ChuKoNuRenderer;
import dev.toma.gunsrpg.client.render.item.RocketLauncherRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
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
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.DIAMOND, 9)
                    .define(AmmoMaterials.QUARTZ, 9)
                    .define(AmmoMaterials.EMERALD, 11)
                    .define(AmmoMaterials.AMETHYST, 14)
                    .define(AmmoMaterials.NETHERITE, 17)
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
