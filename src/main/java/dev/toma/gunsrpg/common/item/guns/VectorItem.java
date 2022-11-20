package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VectorRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.Random;

public class VectorItem extends GunItem {

    private static final ResourceLocation AIM = GunsRPG.makeResource("vector/aim");
    private static final ResourceLocation AIM_RED_DOT = GunsRPG.makeResource("vector/aim_red_dot");
    private static final ResourceLocation EJECT = GunsRPG.makeResource("vector/eject");
    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vector/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("vector/unjam");

    public VectorItem(String name) {
        super(name, new Properties().setISTER(() -> VectorRenderer::new).durability(1100));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.VECTOR_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(GunsRPG.config.weapon.vector)
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
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.VECTOR_RELOAD.intValue(provider);
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
        return provider.getAttribute(Attribs.VECTOR_MAG_CAPACITY).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.VECTOR_VERTICAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.VECTOR_NOISE.value(provider);
    }

    @Override
    protected boolean consumeAmmo(ItemStack stack, LivingEntity consumer) {
        if (consumer instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) consumer;
            if (PlayerData.hasActiveSkill(player, Skills.VECTOR_OVERLOADED)) {
                Random random = player.getRandom();
                float chance = random.nextFloat();
                return !(chance < SkillUtil.NO_AMMO_CONSUME_CHANCE);
            }
        }
        return true;
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
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.VECTOR_RED_DOT) ? AIM_RED_DOT : AIM;
    }

    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
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
