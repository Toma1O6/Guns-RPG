package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VssRenderer;
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

public class VssItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vss/reload");

    public VssItem(String name) {
        super(name, new Properties().setISTER(() -> VssRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.DMR)
                .caliber(AmmoType.AMMO_9MM)
                .config(ModConfig.weaponConfig.vss)
                .ammo()
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.DIAMOND, 8)
                    .define(AmmoMaterials.QUARTZ, 8)
                    .define(AmmoMaterials.EMERALD, 10)
                    .define(AmmoMaterials.AMETHYST, 12)
                    .define(AmmoMaterials.NETHERITE, 14)
                .build();
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.VSS_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.VSS_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.VSS_RIGHT;
    }
}
