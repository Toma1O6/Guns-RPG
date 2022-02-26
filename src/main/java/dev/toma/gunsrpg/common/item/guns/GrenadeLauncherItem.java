package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.GrenadeLauncherRenderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GrenadeLauncherItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("gl/reload");
    private static final ResourceLocation LOAD_BULLET = GunsRPG.makeResource("gl/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("gl/unjam");

    public GrenadeLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> GrenadeLauncherRenderer::new).durability(200));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.grenadeLauncher)
                .ammo(WeaponCategory.GRENADE_LAUNCHER)
                    .define(AmmoMaterials.GRENADE)
                    .define(AmmoMaterials.TEAR_GAS)
                    .define(AmmoMaterials.STICKY)
                    .define(AmmoMaterials.HE_GRENADE)
                    .define(AmmoMaterials.IMPACT)
                .build();
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 4;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 30;
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(30, player, this, player.getMainHandItem(), LOAD_BULLET);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 5;
    }

    @Override
    public int getUnjamTime(ItemStack stack, IPlayerData data) {
        return 75;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.GRENADE_LAUNCHER_ASSEMBLY;
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
        return RenderConfigs.M203_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.M203_RIGHT;
    }
}
