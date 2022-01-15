package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.DesertEagleRenderer;
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

public class DesertEagleItem extends GunItem {

    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("deagle/reload");

    public DesertEagleItem(String name) {
        super(name, new Properties().setISTER(() -> DesertEagleRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.PISTOL)
                .caliber(AmmoType.AMMO_762MM)
                .config(ModConfig.weaponConfig.desertEagle)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 6)
                    .define(AmmoMaterials.LAPIS, 6)
                    .define(AmmoMaterials.GOLD, 9)
                    .define(AmmoMaterials.REDSTONE, 9)
                    .define(AmmoMaterials.DIAMOND, 13)
                    .define(AmmoMaterials.QUARTZ, 13)
                    .define(AmmoMaterials.EMERALD, 16)
                    .define(AmmoMaterials.AMETHYST, 20)
                    .define(AmmoMaterials.NETHERITE, 23)
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.DESERT_EAGLE_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.DESERT_EAGLE_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.DESERT_EAGLE_RIGHT;
    }
}
