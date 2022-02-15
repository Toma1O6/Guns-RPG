package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.RocketLauncherRenderer;
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

public class RocketLauncherItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("rl/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("rl/unjam");

    public RocketLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> RocketLauncherRenderer::new).durability(150));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.rocketLauncher)
                .ammo(WeaponCategory.ROCKET_LAUNCHER)
                    .define(AmmoMaterials.ROCKET)
                    .define(AmmoMaterials.TOXIN)
                    .define(AmmoMaterials.DEMOLITION)
                    .define(AmmoMaterials.NAPALM)
                    .define(AmmoMaterials.HE_ROCKET)
                .build();
    }

    @Override
    public int getUnjamTime(ItemStack stack, IPlayerData data) {
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
}
