package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerMagazine;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class GunItem extends GRPGItem {

    protected static Random random = new Random();
    protected final GunType gunType;
    protected final Map<AmmoMaterial, Integer> materialDamageBonusMap;

    public GunItem(String name, GunType type) {
        super(name);
        this.gunType = type;
        this.setMaxStackSize(1);
        this.fillAmmoMaterialData(materialDamageBonusMap = new HashMap<>());
    }

    public SoundEvent getShootSound(EntityLivingBase entity) {
        return SoundEvents.BLOCK_LEVER_CLICK;
    }

    public SoundEvent getReloadSound(EntityPlayer player) {
        return SoundEvents.BLOCK_LEVER_CLICK;
    }

    public Firemode getFiremode(EntityPlayer player) {
        return Firemode.SINGLE;
    }

    @SideOnly(Side.CLIENT)
    public void renderRightArm() {

    }

    @SideOnly(Side.CLIENT)
    public void renderLeftArm() {

    }

    @SideOnly(Side.CLIENT)
    public final void renderArm(EnumHandSide side) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
        Render<AbstractClientPlayer> render = mc.getRenderManager().getEntityRenderObject(mc.player);
        RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(40.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
        if (side == EnumHandSide.RIGHT) {
            GlStateManager.translate(0.8F, -0.3F, -0.4F);
            GlStateManager.rotate(-41.0F, 0.0F, 0.0F, 1.0F);
            renderplayer.renderRightArm(mc.player);
        } else {
            GlStateManager.translate(-0.5F, 0.6F, -0.36F);
            GlStateManager.rotate(-41.0F, 0.0F, 0.0F, 1.0F);
            renderplayer.renderLeftArm(mc.player);
        }

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.54F, 0.06F, 0.0F);
    }

    @SideOnly(Side.CLIENT)
    public Animation createReloadAnimation(EntityPlayer player) {
        return new MultiStepAnimation.Configurable(this.getReloadTime(player), "pistol_reload");
    }

    public abstract WeaponConfiguration getWeaponConfig();

    public abstract void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data);

    public IReloadManager getReloadManager() {
        return ReloadManagerMagazine.MANAGER;
    }

    public boolean isSilenced(EntityPlayer player) {
        return false;
    }

    public int getFirerate(EntityPlayer player) {
        return 0;
    }

    public int getMaxAmmo(EntityPlayer player) {
        return 0;
    }

    public int getReloadTime(EntityPlayer player) {
        return 50;
    }

    public float getVerticalRecoil(EntityPlayer player) {
        return this.getWeaponConfig().recoilVertical;
    }

    public float getHorizontalRecoil(EntityPlayer player) {
        boolean f = random.nextBoolean();
        float v = this.getWeaponConfig().recoilHorizontal;
        return f ? v : -v;
    }

    public void onHitEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {

    }

    public void onKillEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {

    }

    public void updateBullet(EntityBullet bullet) {

    }

    public void shootBullet(World world, EntityLivingBase entity, ItemStack stack) {
        EntityBullet bullet = new EntityBullet(world, entity, this, stack);
        boolean aim = entity instanceof EntityPlayer && PlayerDataFactory.get((EntityPlayer) entity).getAimInfo().isAiming();
        float pitch = entity.rotationPitch + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float yaw = entity.rotationYaw + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        bullet.fire(pitch, yaw, getWeaponConfig().velocity);
        world.spawnEntity(bullet);
    }

    public final void shoot(World world, EntityLivingBase entity, ItemStack stack) {
        Item item = stack.getItem();
        CooldownTracker tracker = null;
        if (entity instanceof EntityPlayer) {
            tracker = ((EntityPlayer) entity).getCooldownTracker();
            if (tracker.hasCooldown(item)) {
                return;
            }
        }
        this.shootBullet(world, entity, stack);
        this.setAmmoCount(stack, this.getAmmo(stack) - 1);
        world.playSound(null, entity.posX, entity.posY, entity.posZ, this.getShootSound(entity), SoundCategory.MASTER, 15.0F, 1.0F);
        if (tracker != null) {
            tracker.setCooldown(item, this.getFirerate((EntityPlayer) entity));
        }
    }

    public final int getDamageBonus(ItemStack stack) {
        AmmoMaterial material = this.getMaterialFromNBT(stack);
        Integer v = material != null ? materialDamageBonusMap.get(material) : 0;
        return v == null ? 0 : v;
    }

    public AmmoType getAmmoType() {
        return gunType.getAmmoType();
    }

    public void createNBT(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("ammo", 0);
        stack.setTagCompound(nbt);
    }

    public AmmoMaterial getMaterialFromNBT(ItemStack stack) {
        this.createNBT(stack);
        NBTTagCompound nbt = stack.getTagCompound();
        int id = Math.max(0, Math.min(AmmoMaterial.values().length, nbt.getInteger("material")));
        return nbt.hasKey("material") ? AmmoMaterial.values()[id] : null;
    }

    public int getAmmo(ItemStack stack) {
        this.createNBT(stack);
        return stack.getTagCompound().getInteger("ammo");
    }

    public void setAmmoCount(ItemStack stack, int count) {
        this.createNBT(stack);
        NBTTagCompound nbt = stack.getTagCompound();
        nbt.setInteger("ammo", Math.max(0, count));
    }

    public boolean hasAmmo(ItemStack stack) {
        return this.getAmmo(stack) > 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        AmmoMaterial material = getMaterialFromNBT(stack);
        tooltip.add("Ammo type: " + (material != null ? material.getColor() + material.name().toUpperCase() : "???"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        return false;
    }
}
