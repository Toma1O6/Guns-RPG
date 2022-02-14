package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.S686Renderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class S686Item extends AbstractShotgun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("s686/reload");
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
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 2)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 3)
                    .define(AmmoMaterials.DIAMOND, 5)
                    .define(AmmoMaterials.QUARTZ, 5)
                    .define(AmmoMaterials.EMERALD, 6)
                    .define(AmmoMaterials.AMETHYST, 8)
                    .define(AmmoMaterials.NETHERITE, 10)
                .build();
    }

    @Override
    public int getUnjamTime(ItemStack stack, IPlayerData data) {
        return 70;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S686_ASSEMBLY;
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
        return RenderConfigs.S686_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.S686_RIGHT;
    }
}
