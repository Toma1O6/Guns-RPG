package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Mk14EbrRenderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Mk14EbrItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("mk14/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("mk14/unjam");

    public Mk14EbrItem(String name) {
        super(name, new Properties().setISTER(() -> Mk14EbrRenderer::new).durability(750));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.mk14)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo(WeaponCategory.DMR)
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 5)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 8)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.EMERALD, 11)
                    .define(AmmoMaterials.QUARTZ, 8)
                    .define(AmmoMaterials.DIAMOND, 15)
                    .define(AmmoMaterials.AMETHYST, 18)
                    .define(AmmoMaterials.NETHERITE, 22)
                .build();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 75;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 2;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 20;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.MK14EBR_ASSEMBLY;
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
        return RenderConfigs.MK14_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.MK14_RIGHT;
    }
}
