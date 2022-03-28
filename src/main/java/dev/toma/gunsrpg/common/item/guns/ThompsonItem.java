package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.ThompsonRenderer;
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

public class ThompsonItem extends GunItem {

    private static final ResourceLocation AIM = GunsRPG.makeResource("thompson/aim");
    private static final ResourceLocation AIM_RED_DOT = GunsRPG.makeResource("thompson/aim_red_dot");
    private static final ResourceLocation EJECT = GunsRPG.makeResource("thompson/eject");
    private static final ResourceLocation RELOAD = GunsRPG.makeResource("thompson/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("thompson/unjam");

    public ThompsonItem(String name) {
        super(name, new Properties().setISTER(() -> ThompsonRenderer::new).durability(850));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.THOMPSON_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SMG)
                .config(ModConfig.weaponConfig.thompson)
                .caliber(AmmoType.AMMO_556MM)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 8)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 10)
                    .define(AmmoMaterials.AMETHYST, 12)
                    .define(AmmoMaterials.NETHERITE, 14)
                .build();
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.THOMPSON_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_TOMMY_GUN_SILENCED : ModSounds.GUN_TOMMY_GUN;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
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
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.THOMPSON_RED_DOT) ? AIM_RED_DOT : AIM;
    }

    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
    }

    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.THOMPSON_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.THOMPSON_RIGHT;
    }
}
