package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.S686Renderer;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
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

public class S686Item extends AbstractShotgun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("s686/reload_both");
    private static final ResourceLocation RELOAD_SINGLE = GunsRPG.makeResource("s686/reload_single");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("s686/unjam");

    public S686Item(String name) {
        super(name, new Properties().setISTER(() -> S686Renderer::new).durability(230));
    }

    @Override
    public int getPelletCount(LivingEntity shooter, ItemStack stack) {
        return 8;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SG)
                .config(ModConfig.weaponConfig.s686)
                .firemodeSelector(this::switchFiremode)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 1)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 2)
                    .define(AmmoMaterials.EMERALD, 5)
                    .define(AmmoMaterials.QUARTZ, 4)
                    .define(AmmoMaterials.DIAMOND, 6)
                    .define(AmmoMaterials.AMETHYST, 8)
                    .define(AmmoMaterials.NETHERITE, 10)
                .build();
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_S686;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 60;
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 8;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 2;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S686_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        int count = getAmmoCount(stack);
        return count == 0 ? RELOAD : RELOAD_SINGLE;
    }

    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.S686_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.S686_RIGHT;
    }

    private Firemode switchFiremode(PlayerEntity player, Firemode firemode) {
        boolean canSwitch = firemode == Firemode.DOUBLE_ACTION || PlayerData.hasActiveSkill(player, Skills.S686_CANNON_BLAST);
        return canSwitch ? firemode == Firemode.DOUBLE_ACTION ? Firemode.SINGLE : Firemode.DOUBLE_ACTION : firemode;
    }
}
