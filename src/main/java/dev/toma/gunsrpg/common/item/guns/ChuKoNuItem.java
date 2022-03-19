package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.ChuKoNuRenderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
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

public class ChuKoNuItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("chukonu/reload");
    private static final ResourceLocation BULLET = GunsRPG.makeResource("chukonu/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("chukonu/unjam");

    public ChuKoNuItem(String name) {
        super(name, new Properties().setISTER(() -> ChuKoNuRenderer::new).durability(450));
    }

    // TODO HEAVY BOLTS - weakness I 90t, base dmg +3
    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.chukonu)
                .ammo(WeaponCategory.CROSSBOW)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 9)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 11)
                    .define(AmmoMaterials.AMETHYST, 13)
                    .define(AmmoMaterials.NETHERITE, 15)
                .build();
    }

    @Override
    protected Firemode getDefaultFiremode() {
        return Firemode.FULL_AUTO;
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 35;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 4;
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(15, player, this, player.getMainHandItem(), BULLET);
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 6;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CHUKONU_ASSEMBLY;
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
        return RenderConfigs.CHUKONU_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.CHUKONU_RIGHT;
    }
}
