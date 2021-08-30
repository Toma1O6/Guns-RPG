package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Kar98kRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.SkillUtil;
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

    public Kar98kItem(String name) {
        super(name, GunType.SR, new Properties().setISTER(() -> Kar98kRenderer::new));
    }

    @Override
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.kar98k;
    }

    @Override
    public void fillAmmoMaterialData(MaterialContainer container) {
        container
                .add(AmmoMaterials.WOOD, 0)
                .add(AmmoMaterials.STONE, 4)
                .add(AmmoMaterials.IRON, 9)
                .add(AmmoMaterials.GOLD, 13)
                .add(AmmoMaterials.DIAMOND, 17)
                .add(AmmoMaterials.EMERALD, 20)
                .add(AmmoMaterials.AMETHYST, 25);
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        return ReloadManagers.either(
                getAmmo(stack) < getMaxAmmo(player),
                ReloadManagers.singleBulletLoading(46, player, this, stack, LOAD_BULLET_ANIMATION),
                ReloadManagers.fullMagLoading()
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
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.KAR98K_EXTENDED) ? 10 : 5;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfig cfg = getWeaponConfig();
        return PlayerData.hasActiveSkill(player, Skills.KAR98K_FAST_HANDS) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        boolean empty = this.getAmmo(player.getMainHandItem()) == 0;
        boolean magSkill = PlayerData.hasActiveSkill(player, Skills.KAR98K_FAST_HANDS);
        int time = magSkill ? empty ? 40 : 20 : empty ? 66 : 33;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.KAR98K_SUPPRESSOR);
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.KAR98K_CHEEKPAD) ? ModConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public float getHorizontalRecoil(PlayerEntity player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.KAR98K_CHEEKPAD) ? ModConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * f;
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
