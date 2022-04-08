package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.RocketLauncherRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
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

public class RocketLauncherItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("rl/reload");
    private static final ResourceLocation LOAD_SINGLE = GunsRPG.makeResource("rl/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("rl/unjam");

    public RocketLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> RocketLauncherRenderer::new).durability(150));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.rocketLauncher)
                .firemodeSelector(this::switchFiremode)
                .ammo(WeaponCategory.ROCKET_LAUNCHER)
                    .define(AmmoMaterials.ROCKET)
                    .define(AmmoMaterials.TOXIN)
                    .define(AmmoMaterials.DEMOLITION)
                    .define(AmmoMaterials.NAPALM)
                    .define(AmmoMaterials.HE_ROCKET)
                .build();
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
        return 40;
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

    private Firemode switchFiremode(PlayerEntity player, Firemode firemode) {
        IPlayerData data = PlayerData.getUnsafe(player);
        ISkillProvider skillProvider = data.getSkillProvider();
        ItemStack stack = player.getMainHandItem();
        boolean fullMagazine = isFullMag(data.getAttributes(), stack);
        boolean canSwitch = firemode == Firemode.BARRAGE || (fullMagazine && skillProvider.hasSkill(Skills.ROCKET_LAUNCHER_ROCKET_BARRAGE));
        return canSwitch ? firemode == Firemode.BARRAGE ? Firemode.SINGLE : Firemode.BARRAGE : firemode;
    }

    private boolean isFullMag(IAttributeProvider provider, ItemStack stack) {
        int capacity = this.getMaxAmmo(provider);
        return getAmmo(stack) < capacity;
    }
}
