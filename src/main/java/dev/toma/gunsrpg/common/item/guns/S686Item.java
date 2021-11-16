package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.S686Renderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class S686Item extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("s686/reload");

    public S686Item(String name) {
        super(name, new Properties().setISTER(() -> S686Renderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SG)
                .config(ModConfig.weaponConfig.s686)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 2)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 3)
                    .define(AmmoMaterials.DIAMOND, 5)
                    .define(AmmoMaterials.QUARTZ, 5)
                    .define(AmmoMaterials.EMERALD, 6)
                    .define(AmmoMaterials.AMETHYST, 8)
                    .define(AmmoMaterials.NETHERITE, 10)
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
