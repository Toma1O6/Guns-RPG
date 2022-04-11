package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.GuidedProjectileTargetHandler;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.RocketLauncherRenderer;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.IEntityTrackingGun;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static dev.toma.gunsrpg.util.properties.Properties.*;

public class RocketLauncherItem extends GunItem implements IEntityTrackingGun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("rl/reload");
    private static final ResourceLocation LOAD_SINGLE = GunsRPG.makeResource("rl/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("rl/unjam");
    private static final Firemode.ConditionalSelector SELECTOR = Firemode.ConditionalSelector.builder()
            .addTransition(Firemode.SINGLE, RocketLauncherItem::canUseBarrageMode, Firemode.BARRAGE)
            .addTransition(Firemode.SINGLE, RocketLauncherItem::canUseHomingMode, Firemode.HOMING)
            .addTransition(Firemode.HOMING, RocketLauncherItem::canUseBarrageMode, Firemode.BARRAGE)
            .addTransition(Firemode.HOMING, (player, data) -> true, Firemode.SINGLE)
            .addTransition(Firemode.BARRAGE, (player, data) -> true, Firemode.SINGLE)
            .build();

    public RocketLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> RocketLauncherRenderer::new).durability(150));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.rocketLauncher)
                .firemodeSelector(SELECTOR)
                .ammo(WeaponCategory.ROCKET_LAUNCHER)
                    .define(AmmoMaterials.ROCKET)
                    .define(AmmoMaterials.TOXIN)
                    .define(AmmoMaterials.DEMOLITION)
                    .define(AmmoMaterials.NAPALM)
                    .define(AmmoMaterials.HE_ROCKET)
                .build();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void preShootEvent(PropertyContext context) {
        context.setProperty(ENTITY_ID, GuidedProjectileTargetHandler.getSelectedEntity());
    }

    @Override
    public boolean canBeGuided(PlayerEntity player) {
        IPlayerData data = PlayerData.getUnsafe(player);
        IAimInfo aimInfo = data.getAimInfo();
        Firemode firemode = this.getFiremode(player.getMainHandItem());
        return firemode == Firemode.HOMING && aimInfo.isAiming();
    }

    @Override
    public int getMaxRange() {
        return 96;
    }

    @Override
    public int getLockTime() {
        return 40;
    }

    @Override
    public int getRgb(boolean locked) {
        return locked ? 0xFF0000 : 0xFFFF00;
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.RL_SHOT;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 4;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.RL_RELOAD.intValue(provider);
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(25, player, this, player.getMainHandItem(), LOAD_SINGLE);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 12;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 65;
    }

    @Override
    protected float getInaccuracy(IShootProps props, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            ItemStack stack = entity.getMainHandItem();
            Firemode firemode = getFiremode(stack);
            if (firemode == Firemode.BARRAGE) {
                return 0.8F;
            }
        }
        return props.getInaccuracy();
    }

    @Override
    protected void prepareForShooting(AbstractProjectile projectile, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            PlayerData.get(player).ifPresent(data -> {
                ISkillProvider provider = data.getSkillProvider();
                if (provider.hasSkill(Skills.ROCKET_LAUNCHER_ROCKET_FUEL)) {
                    projectile.setProperty(FUELED, true);
                }
                if (provider.hasSkill(Skills.ROCKET_LAUNCHER_DEMOLITION_EXPERT)) {
                    projectile.setProperty(EXPLOSION_POWER, 1);
                }
            });
        }
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.ROCKET_LAUNCHER_ASSEMBLY;
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
        return RenderConfigs.ROCKET_LAUNCHER_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.ROCKET_LAUNCHER_RIGHT;
    }

    private static boolean canUseBarrageMode(PlayerEntity player, IPlayerData data) {
        ItemStack stack = player.getMainHandItem();
        GunItem gunItem = (GunItem) stack.getItem();
        boolean fullMag = gunItem.getAmmo(stack) == gunItem.getMaxAmmo(data.getAttributes());
        return fullMag && data.getSkillProvider().hasSkill(Skills.ROCKET_LAUNCHER_ROCKET_BARRAGE);
    }

    private static boolean canUseHomingMode(PlayerEntity player, IPlayerData data) {
        return data.getSkillProvider().hasSkill(Skills.ROCKET_LAUNCHER_HOMING_MISSILE);
    }

    private Firemode switchFiremode(PlayerEntity player, Firemode firemode) {
        IPlayerData data = PlayerData.getUnsafe(player);
        ISkillProvider skillProvider = data.getSkillProvider();
        ItemStack stack = player.getMainHandItem();
        boolean fullMagazine = isFullMag(data.getAttributes(), stack);
        boolean canSwitch = firemode == Firemode.BARRAGE || (fullMagazine && skillProvider.hasSkill(Skills.ROCKET_LAUNCHER_ROCKET_BARRAGE));
        boolean homing = skillProvider.hasSkill(Skills.ROCKET_LAUNCHER_HOMING_MISSILE);
        return canSwitch ? firemode == Firemode.BARRAGE ? Firemode.SINGLE : Firemode.BARRAGE : firemode;
    }

    private boolean isFullMag(IAttributeProvider provider, ItemStack stack) {
        int capacity = this.getMaxAmmo(provider);
        return getAmmo(stack) < capacity;
    }
}
