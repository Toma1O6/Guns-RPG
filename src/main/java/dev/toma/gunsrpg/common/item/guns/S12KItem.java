package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.S12KRenderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class S12KItem extends AbstractShotgun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("s12k/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("s12k/unjam");

    public S12KItem(String name) {
        super(name, new Properties().setISTER(() -> S12KRenderer::new).durability(370));
    }

    @Override
    public int getPelletCount(LivingEntity shooter, ItemStack stack) {
        return 6;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SG)
                .config(ModConfig.weaponConfig.s12k)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 1)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 2)
                    .define(AmmoMaterials.EMERALD, 5)
                    .define(AmmoMaterials.QUARTZ, 4)
                    .define(AmmoMaterials.DIAMOND, 6)
                    .define(AmmoMaterials.AMETHYST, 8)
                    .define(AmmoMaterials.NETHERITE, 10)
                .build();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 80;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 6;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 5;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S12K_ASSEMBLY;
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
        return RenderConfigs.S12K_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.S12K_RIGHT;
    }
}
