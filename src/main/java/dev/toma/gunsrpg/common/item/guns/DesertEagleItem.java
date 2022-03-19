package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.DesertEagleRenderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DesertEagleItem extends GunItem {

    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("deagle/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("deagle/unjam");

    public DesertEagleItem(String name) {
        super(name, new Properties().setISTER(() -> DesertEagleRenderer::new).durability(520));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.PISTOL)
                .caliber(AmmoType.AMMO_762MM)
                .config(ModConfig.weaponConfig.desertEagle)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 6)
                    .define(AmmoMaterials.LAPIS, 5)
                    .define(AmmoMaterials.GOLD, 9)
                    .define(AmmoMaterials.REDSTONE, 8)
                    .define(AmmoMaterials.EMERALD, 13)
                    .define(AmmoMaterials.QUARTZ, 11)
                    .define(AmmoMaterials.DIAMOND, 16)
                    .define(AmmoMaterials.AMETHYST, 20)
                    .define(AmmoMaterials.NETHERITE, 23)
                .build();
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_DEAGLE;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 60;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 9;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 55;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 7;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.DESERT_EAGLE_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.DESERT_EAGLE_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.DESERT_EAGLE_RIGHT;
    }
}
