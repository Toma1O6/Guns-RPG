package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerMagazine;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

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
        this.fillAmmoMaterialData(materialDamageBonusMap = new HashMap<>());
    }

    public abstract WeaponConfiguration getWeaponConfig();

    public abstract void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data);

    public IReloadManager getReloadManager() {
        return ReloadManagerMagazine.MANAGER;
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
        return 0.0F;
    }

    public float getHorizontalRecoil(EntityPlayer player) {
        return 0.0F;
    }

    public void onHitEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {

    }

    public void onKillEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {

    }

    public void shootBullet(World world, EntityLivingBase entity, ItemStack stack) {
        world.spawnEntity(new EntityBullet(world, entity, stack));
    }

    public void shoot(World world, EntityLivingBase entity, ItemStack stack) {
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
        world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 0.5F, 1.0F);
        if (tracker != null) {
            tracker.setCooldown(item, this.getFirerate((EntityPlayer) entity));
        }
    }

    public final int getDamageBonus(AmmoMaterial material) {
        return materialDamageBonusMap.get(material);
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
}
