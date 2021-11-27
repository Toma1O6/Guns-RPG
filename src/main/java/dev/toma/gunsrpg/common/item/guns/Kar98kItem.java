package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Kar98kRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.sided.ClientSideManager;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Kar98kItem extends GunItem {

    private static final ResourceLocation EJECT = GunsRPG.makeResource("kar98k/eject");
    private static final ResourceLocation[] AIM_ANIMATIONS = {
            GunsRPG.makeResource("kar98k/aim"),
            GunsRPG.makeResource("kar98k/aim_scoped")
    };
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("kar98k/reload");
    private static final ResourceLocation LOAD_BULLET_ANIMATION = GunsRPG.makeResource("kar98k/load_bullet");
    private static final ResourceLocation RELOAD_CLIP_ANIMATION = GunsRPG.makeResource("kar98k/reload_clip");

    public Kar98kItem(String name) {
        super(name, new Properties().setISTER(() -> Kar98kRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SR)
                .config(ModConfig.weaponConfig.kar98k)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 4)
                    .define(AmmoMaterials.IRON, 9)
                    .define(AmmoMaterials.LAPIS, 9)
                    .define(AmmoMaterials.GOLD, 14)
                    .define(AmmoMaterials.REDSTONE, 14)
                    .define(AmmoMaterials.DIAMOND, 17)
                    .define(AmmoMaterials.QUARTZ, 17)
                    .define(AmmoMaterials.EMERALD, 20)
                    .define(AmmoMaterials.AMETHYST, 24)
                    .define(AmmoMaterials.NETHERITE, 29)
                .build();

        ScopeDataRegistry.getRegistry().register(this, 15.0F, 0.3F);
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.KAR98K_MAG_CAPACITY).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return Attribs.KAR98K_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.KAR98K_FIRERATE).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.KAR98K_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.KAR98K_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.KAR98K_LOUDNESS.value(provider);
    }

    @Override
    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return Attribs.KAR98K_HEADSHOT.value(provider);
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        ItemStack stack = player.getMainHandItem();
        int prepTime = (int) (46 * Attribs.KAR98K_RELOAD.value(attributeProvider));
        return ReloadManagers.either(
                getAmmo(stack) > 0,
                ReloadManagers.singleBulletLoading(prepTime, player, this, stack, LOAD_BULLET_ANIMATION),
                ReloadManagers.clip(RELOAD_CLIP_ANIMATION)
        );
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.KAR98K_SILENT : ModSounds.KAR98K;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.M24;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.KAR98K_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        boolean scope = PlayerData.hasActiveSkill(Minecraft.getInstance().player, Skills.KAR98K_SCOPE);
        return scope ? AIM_ANIMATIONS[1] : AIM_ANIMATIONS[0];
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
    }

    // TODO
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
        //ClientSideManager.instance().processor().play(Animations.REBOLT, new Animations.ReboltKar98k(this.getFirerate(player)));
        NetworkManager.sendServerPacket(new SPacketSetAiming(false));
        ClientSideManager.instance().playDelayedSound(player.blockPosition(), 1.0F, 1.0F, ModSounds.KAR98K_BOLT, SoundCategory.MASTER, 15);
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.KAR98K_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.KAR98K_RIGHT;
    }
}
