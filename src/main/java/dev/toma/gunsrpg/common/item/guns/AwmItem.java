package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AwmRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
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
import net.minecraft.util.SoundEvent;

public class AwmItem extends AbstractBoltActionGun {

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
                    .define(AmmoMaterials.LAPIS, 8)
                    .define(AmmoMaterials.GOLD, 15)
                    .define(AmmoMaterials.REDSTONE, 12)
                    .define(AmmoMaterials.EMERALD, 21)
                    .define(AmmoMaterials.QUARTZ, 16)
                    .define(AmmoMaterials.DIAMOND, 29)
                    .define(AmmoMaterials.AMETHYST, 35)
                    .define(AmmoMaterials.NETHERITE, 42)
                .build();
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AWM_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_AWM_SILENCED : ModSounds.GUN_AWM;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return 100;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 36;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 5;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
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
