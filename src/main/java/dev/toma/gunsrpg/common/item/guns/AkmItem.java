package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AkmRenderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class AkmItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("akm/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("akm/unjam");

    public AkmItem(String name) {
        super(name, new Properties().setISTER(() -> AkmRenderer::new).durability(1100));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .caliber(AmmoType.AMMO_762MM)
                .config(ModConfig.weaponConfig.akm)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo()
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 9)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 11)
                    .define(AmmoMaterials.AMETHYST, 14)
                    .define(AmmoMaterials.NETHERITE, 17)
                .build();
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AKM_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_AKM_SILENCED : ModSounds.GUN_AKM;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return 75;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 3;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 30;
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
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
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
