package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.AkmRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class AugItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("aug/reload");

    public AugItem(String name) {
        super(name, new Properties().setISTER(() -> AkmRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .config(ModConfig.weaponConfig.akm)
                .ammo()
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 7)
                    .define(AmmoMaterials.REDSTONE, 7)
                    .define(AmmoMaterials.DIAMOND, 9)
                    .define(AmmoMaterials.QUARTZ, 9)
                    .define(AmmoMaterials.EMERALD, 11)
                    .define(AmmoMaterials.AMETHYST, 13)
                    .define(AmmoMaterials.NETHERITE, 17)
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
