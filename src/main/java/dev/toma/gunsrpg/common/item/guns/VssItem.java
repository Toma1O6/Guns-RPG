package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VssRenderer;
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

public class VssItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vss/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("vss/unjam");

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
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_VSS;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 75;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 2;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 20;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
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
    public IRenderConfig left() {
        return RenderConfigs.VSS_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.VSS_RIGHT;
    }

    private Firemode switchFiremode(PlayerEntity player, Firemode firemode) {
        boolean canSwitch = firemode == Firemode.FULL_AUTO || PlayerData.hasActiveSkill(player, Skills.VSS_ADAPTIVE_CHAMBERING);
        return canSwitch ? firemode == Firemode.FULL_AUTO ? Firemode.SINGLE : Firemode.FULL_AUTO : firemode;
    }
}
