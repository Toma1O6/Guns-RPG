package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AugRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class AugItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("aug/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("aug/unjam");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("aug/aim"),
            GunsRPG.makeResource("aug/aim_red_dot"),
    };
    private static final ResourceLocation EJECT = GunsRPG.makeResource("aug/eject");

    public AugItem(String name) {
        super(name, new Properties().setISTER(() -> AugRenderer::new).durability(1200));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .config(GunsRPG.config.weapon.aug)
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
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AUG_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_AUG_SILENCED : ModSounds.GUN_AUG;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.AUG_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 2;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.AUG_MAG_CAPACITY).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.AUG_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.AUG_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.AUG_NOISE.value(provider);
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (victim instanceof MonsterEntity && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.AUG_SOUL_TAKER)) {
            SkillUtil.applySoulTakerSkill(shooter);
        }
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
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
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM[PlayerData.hasActiveSkill(player, Skills.AUG_RED_DOT) ? 1 : 0];
    }

    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
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
