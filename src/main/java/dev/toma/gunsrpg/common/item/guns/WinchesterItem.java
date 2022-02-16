package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.WinchesterRenderer;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WinchesterItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("winchester/reload");
    private static final ResourceLocation BULLET = GunsRPG.makeResource("winchester/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("winchester/unjam");

    public WinchesterItem(String name) {
        super(name, new Properties().setISTER(() -> WinchesterRenderer::new).durability(450));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.winchester)
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
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 25;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 15;
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(12, player, this, player.getMainHandItem(), BULLET);
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 8;
    }

    @Override
    public int getUnjamTime(ItemStack stack, IPlayerData data) {
        return 60;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.WINCHESTER_ASSEMBLY;
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
        return RenderConfigs.WINCHESTER_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.WINCHESTER_RIGHT;
    }
}
