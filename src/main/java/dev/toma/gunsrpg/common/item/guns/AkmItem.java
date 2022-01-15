package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AkmRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class AkmItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("akm/reload");

    public AkmItem(String name) {
        super(name, new Properties().setISTER(() -> AkmRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .caliber(AmmoType.AMMO_762MM)
                .config(ModConfig.weaponConfig.akm)
                .ammo()
                    .define(AmmoMaterials.WOOD)
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
        return Skills.AKM_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.AKM_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.AKM_RIGHT;
    }
}
