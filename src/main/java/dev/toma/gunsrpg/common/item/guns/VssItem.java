package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VssRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class VssItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vss/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("vss/unjam");
    private static final ResourceLocation EJECT = GunsRPG.makeResource("vss/eject");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("vss/aim"),
            GunsRPG.makeResource("vss/aim_scoped")
    };

    public VssItem(String name) {
        super(name, new Properties().setISTER(() -> VssRenderer::new).durability(750));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.DMR)
                .caliber(AmmoType.AMMO_9MM)
                .config(ModConfig.weaponConfig.vss)
                .firemodeSelector(this::switchFiremode)
                .ammo()
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 8)
                    .define(AmmoMaterials.QUARTZ, 6)
                    .define(AmmoMaterials.DIAMOND, 10)
                    .define(AmmoMaterials.AMETHYST, 12)
                    .define(AmmoMaterials.NETHERITE, 14)
                .build();
        ScopeDataRegistry.getRegistry().register(this, 25.0F, ScopeDataRegistry.ZOOM_3_0, provider -> provider.hasSkill(Skills.VSS_SCOPE));
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_VSS;
    }

    @Override
    protected float getWeaponSoundVolume(LivingEntity entity) {
        return 1.0F;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.VSS_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.VSS_FIRERATE).intValue();
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.VSS_MAG_CAPACITY).intValue();
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return 0.1F * provider.getAttributeValue(Attribs.WEAPON_NOISE);
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.VSS_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.VSS_HORIZONTAL.floatValue(provider);
    }

    @Override
    public float modifyProjectileDamage(AbstractProjectile projectile, LivingEntity entity, PlayerEntity shooter, float damage) {
        ItemStack weapon = projectile.getWeaponSource();
        if (weapon.getItem() instanceof GunItem && PlayerData.hasActiveSkill(shooter, Skills.VSS_EVERY_BULLET_COUNTS)) {
            int ammo = this.getAmmo(weapon);
            if (ammo == 0) {
                return damage * SkillUtil.EVERY_BULLET_COUNTS_DAMAGE;
            }
        }
        return damage;
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
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM[PlayerData.hasActiveSkill(player, Skills.VSS_SCOPE) ? 1 : 0];
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.VSS_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.VSS_RIGHT;
    }

    @Override
    public float getOpticalRecoilXY() {
        return 0.3F;
    }

    @Override
    public float getOpticalRecoilZ() {
        return 0.1F;
    }

    private Firemode switchFiremode(PlayerEntity player, Firemode firemode) {
        boolean canSwitch = firemode == Firemode.FULL_AUTO || PlayerData.hasActiveSkill(player, Skills.VSS_ADAPTIVE_CHAMBERING);
        return canSwitch ? firemode == Firemode.FULL_AUTO ? Firemode.SINGLE : Firemode.FULL_AUTO : firemode;
    }
}
