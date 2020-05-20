package dev.toma.gunsrpg.common.item.guns;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class GunItem extends GRPGItem {

    protected final WeaponConfiguration weaponConfig;
    protected final ShootFunc shootFunc;
    protected final AmmoType ammoType;

    public GunItem(String name, GunBuilder builder) {
        super(name);
        this.weaponConfig = builder.weaponConfiguration;
        this.shootFunc = builder.shootingHandler;
        // TODO
        this.ammoType = null;
    }

    public void createNBT(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("ammo", 0);
    }

    public AmmoMaterial getMaterialFromNBT(ItemStack stack) {
        this.createNBT(stack);
        NBTTagCompound nbt = new NBTTagCompound();
        int id = Math.max(0, Math.min(AmmoMaterial.values().length, nbt.getInteger("material")));
        return nbt.hasKey("material") ? AmmoMaterial.values()[id] : null;
    }

    public int getAmmo(ItemStack stack) {
        this.createNBT(stack);
        return stack.getTagCompound().getInteger("ammo");
    }

    public boolean hasAmmo(ItemStack stack) {
        return this.getAmmo(stack) > 0;
    }

    public AmmoType getAmmoType() {
        return ammoType;
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
        this.shootFunc.shoot(world, entity, stack);
        world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 0.5F, 1.0F);
        if (tracker != null) {
            tracker.setCooldown(item, 1);
        }
    }

    public WeaponConfiguration getWeaponConfig() {
        return weaponConfig;
    }

    @FunctionalInterface
    public interface ShootFunc {
        ShootFunc DEFAULT = (world, entityLivingBase, stack) -> world.spawnEntity(new EntityBullet(world, entityLivingBase, stack));

        void shoot(World world, EntityLivingBase entityLivingBase, ItemStack stack);
    }

    public static class GunBuilder {

        private String name;
        private WeaponConfiguration weaponConfiguration;
        private ShootFunc shootingHandler = ShootFunc.DEFAULT;
        private GunBuilder() {
        }

        public static GunBuilder create() {
            return new GunBuilder();
        }

        public GunBuilder stats(WeaponConfiguration weaponConfiguration) {
            this.weaponConfiguration = weaponConfiguration;
            return this;
        }

        public GunBuilder shootingHandler(ShootFunc func) {
            this.shootingHandler = func;
            return this;
        }

        public GunItem build(String name) {
            Preconditions.checkNotNull(weaponConfiguration, "Not building: Gun stats are NULL");
            Preconditions.checkNotNull(shootingHandler, "Not building: Shooting handler is NULL");
            Preconditions.checkState(name != null && !name.isEmpty(), "Not building: Invalid registry name");
            return new GunItem(name, this);
        }
    }
}
