package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Hk416Renderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class Hk416Item extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("hk416/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("hk416/unjam");
    private static final ResourceLocation EJECT = GunsRPG.makeResource("hk416/eject");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("hk416/aim"),
            GunsRPG.makeResource("hk416/aim_red_dot"),
    };

    public Hk416Item(String name) {
        super(name, new Properties().setISTER(() -> Hk416Renderer::new).durability(1050));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .config(ModConfig.weaponConfig.hk416)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo()
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 8)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 10)
                    .define(AmmoMaterials.AMETHYST, 12)
                    .define(AmmoMaterials.NETHERITE, 15)
                .build();
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.HK416_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_M416_SILENCED : ModSounds.GUN_M416;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return 70;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 2;
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
        return Skills.HK416_ASSEMBLY;
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
        return AIM[PlayerData.hasActiveSkill(player, Skills.HK416_RED_DOT) ? 1 : 0];
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.HK416_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.HK416_RIGHT;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.M16;
    }
}
