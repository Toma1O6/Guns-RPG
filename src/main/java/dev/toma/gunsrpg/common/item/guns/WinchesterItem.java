package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.WinchesterRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static dev.toma.gunsrpg.util.properties.Properties.LOOT_LEVEL;

public class WinchesterItem extends AbstractBoltActionGun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("winchester/reload");
    private static final ResourceLocation BULLET = GunsRPG.makeResource("winchester/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("winchester/unjam");
    private static final ResourceLocation BOLT = GunsRPG.makeResource("winchester/bolt");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("winchester/aim"),
            GunsRPG.makeResource("winchester/aim_scoped")
    };
    private static final PenetrationData.Factory FACTORY = new PenetrationData.Factory(0.3f);

    public WinchesterItem(String name) {
        super(name, new Properties().setISTER(() -> WinchesterRenderer::new).durability(400));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(GunsRPG.config.weapon.winchester)
                .caliber(AmmoType.AMMO_45ACP)
                .ammo(WeaponCategory.SR)
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 6)
                    .define(AmmoMaterials.LAPIS, 5)
                    .define(AmmoMaterials.GOLD, 9)
                    .define(AmmoMaterials.REDSTONE, 7)
                    .define(AmmoMaterials.EMERALD, 13)
                    .define(AmmoMaterials.QUARTZ, 11)
                    .define(AmmoMaterials.DIAMOND, 16)
                    .define(AmmoMaterials.AMETHYST, 19)
                    .define(AmmoMaterials.NETHERITE, 23)
                .build();
        ScopeDataRegistry.getRegistry().register(this, 20.0F, ScopeDataRegistry.ZOOM_3_5, provider -> provider.hasSkill(Skills.WINCHESTER_SCOPE));
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_WIN94;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.WINCHESTER_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.WINCHESTER_FIRERATE).intValue();
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.WINCHESTER_MAG_CAPACITY).intValue();
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 60;
    }

    @Override
    protected void prepareForShooting(AbstractProjectile projectile, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.WINCHESTER_HUNTER)) {
            projectile.setProperty(LOOT_LEVEL, SkillUtil.HUNTER_LOOTING_LEVEL);
        }
    }

    @Override
    public float modifyProjectileDamage(AbstractProjectile projectile, LivingEntity entity, PlayerEntity shooter, float damage) {
        if (PlayerData.hasActiveSkill(shooter, Skills.WINCHESTER_COLD_BLOODED)) {
            float healthStatus = entity.getHealth() / entity.getMaxHealth();
            if (healthStatus == 1.0F) {
                return damage * SkillUtil.COLD_BLOODED_DAMAGE;
            }
        }
        return damage;
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.WINCHESTER_HUNTER) && victim instanceof MonsterEntity) {
            shooter.heal(2.0F);
        }
    }

    @Override
    public PenetrationData getPenetrationData(IPlayerData data) {
        return data.getSkillProvider().hasSkill(Skills.WINCHESTER_PENETRATOR) ? FACTORY.make() : null;
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(12, player, this, player.getMainHandItem(), BULLET);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.WINCHESTER_ASSEMBLY;
    }

    @Override
    protected boolean shouldStopAimingAfterShooting() {
        return false;
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
        return BOLT;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM[PlayerData.hasActiveSkill(player, Skills.WINCHESTER_SCOPE) ? 1 : 0];
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.WINCHESTER_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.WINCHESTER_RIGHT;
    }
}
