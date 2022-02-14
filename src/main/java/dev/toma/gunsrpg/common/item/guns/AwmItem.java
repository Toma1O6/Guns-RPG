package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AwmRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AwmItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("awm/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("awm/unjam");

    public AwmItem(String name) {
        super(name, new Properties().setISTER(() -> AwmRenderer::new).durability(550));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SR)
                .config(ModConfig.weaponConfig.awm)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 5)
                    .define(AmmoMaterials.IRON, 10)
                    .define(AmmoMaterials.LAPIS, 10)
                    .define(AmmoMaterials.GOLD, 15)
                    .define(AmmoMaterials.REDSTONE, 15)
                    .define(AmmoMaterials.DIAMOND, 21)
                    .define(AmmoMaterials.QUARTZ, 21)
                    .define(AmmoMaterials.EMERALD, 30)
                    .define(AmmoMaterials.AMETHYST, 35)
                    .define(AmmoMaterials.NETHERITE, 42)
                .build();
    }

    @Override
    public int getUnjamTime(ItemStack stack, IPlayerData data) {
        return 90;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.AWM_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.AWM_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.AWM_RIGHT;
    }
}
