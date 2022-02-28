package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AugRenderer;
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

public class AugItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("aug/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("aug/unjam");

    public AugItem(String name) {
        super(name, new Properties().setISTER(() -> AugRenderer::new).durability(1350));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .config(ModConfig.weaponConfig.aug)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo()
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 7)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.EMERALD, 9)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 11)
                    .define(AmmoMaterials.AMETHYST, 13)
                    .define(AmmoMaterials.NETHERITE, 17)
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
        return 30;
    }

    @Override
    public int getUnjamTime(ItemStack stack, IPlayerData data) {
        return 60;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.AUG_ASSEMBLY;
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
        return RenderConfigs.AUG_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.AUG_RIGHT;
    }
}
