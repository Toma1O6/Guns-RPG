package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ReloadManagerClipOrSingle implements IReloadManager {

    public static final ReloadManagerClipOrSingle CLIP_OR_SINGLE = new ReloadManagerClipOrSingle();

    @Override
    public void finishReload(PlayerEntity player, GunItem item, ItemStack stack) {
        AmmoType ammoType = item.getAmmoType();
        AmmoMaterial material = item.getMaterialFromNBT(stack);
        if (material == null) return;
        int ammo = item.getAmmo(stack);
        int maxAmmo = item.getMaxAmmo(player);
        if (ammo < maxAmmo) {
            if (ammo == 0) {
                this.handleClipReload(player, item, stack, ammo, maxAmmo, material, ammoType);
            } else {
                this.handleSingleBulletReload(player, item, stack, ammo, maxAmmo, material, ammoType);
            }
        }
    }

    private void handleClipReload(PlayerEntity player, GunItem item, ItemStack stack, int ammo, int maxAmmo, AmmoMaterial material, AmmoType ammoType) {
        if (player.isCreative()) {
            item.setAmmoCount(stack, maxAmmo);
            return;
        }
        int left = maxAmmo;
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack itemStack = player.inventory.getItem(i);
            if (itemStack.getItem() instanceof IAmmoProvider) {
                IAmmoProvider provider = (IAmmoProvider) itemStack.getItem();
                if (provider.getAmmoType() == ammoType && provider.getMaterial() == material) {
                    int count = Math.min(left, itemStack.getCount());
                    left -= count;
                    itemStack.shrink(count);
                    if (left <= 0) break;
                }
            }
        }
        item.setAmmoCount(stack, maxAmmo - left);
    }

    private void handleSingleBulletReload(PlayerEntity player, GunItem item, ItemStack stack, int ammo, int maxAmmo, AmmoMaterial material, AmmoType ammoType) {
        boolean continueReload = maxAmmo - ammo > 1;
        if (player.isCreative()) {
            item.setAmmoCount(stack, ammo + 1);
            if (continueReload) {
                startReloading(player, item.getReloadTime(player), stack);
            }
            return;
        }
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack itemStack = player.inventory.getItem(i);
            if (itemStack.getItem() instanceof IAmmoProvider) {
                IAmmoProvider provider = (IAmmoProvider) itemStack.getItem();
                if (provider.getAmmoType() == ammoType && provider.getMaterial() == material) {
                    itemStack.shrink(1);
                    item.setAmmoCount(stack, ammo + 1);
                    if (continueReload) {
                        startReloading(player, item.getReloadTime(player), stack);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public boolean canBeInterrupted(GunItem item, ItemStack stack) {
        return item.getAmmo(stack) > 0;
    }
}
