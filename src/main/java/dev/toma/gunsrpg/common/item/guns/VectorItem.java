package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VectorRenderer;
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

public class VectorItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vector/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("vector/unjam");

    public VectorItem(String name) {
        super(name, new Properties().setISTER(() -> VectorRenderer::new).durability(1350));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.VECTOR_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.vector)
                .caliber(AmmoType.AMMO_9MM)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo(WeaponCategory.SMG)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 1)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 2)
                    .define(AmmoMaterials.EMERALD, 5)
                    .define(AmmoMaterials.QUARTZ, 4)
                    .define(AmmoMaterials.DIAMOND, 6)
                    .define(AmmoMaterials.AMETHYST, 7)
                    .define(AmmoMaterials.NETHERITE, 9)
                .build();
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.VECTOR_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_VECTOR_SILENCED : ModSounds.GUN_VECTOR;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 70;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 1;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 55;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 13;
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
        return RenderConfigs.VECTOR_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.VECTOR_RIGHT;
    }
}
