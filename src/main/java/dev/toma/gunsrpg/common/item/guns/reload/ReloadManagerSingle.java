package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ReloadManagerSingle implements IReloadManager {

    public static final ReloadManagerSingle SINGLE = new ReloadManagerSingle();

    @Override
    public void finishReload(PlayerEntity player, GunItem item, ItemStack stack) {
        AmmoType ammoType = item.getAmmoType();
        AmmoMaterial material = item.getMaterialFromNBT(stack);
        if (material == null) return;
        int ammo = item.getAmmo(stack);
        int max = item.getMaxAmmo(player);
        boolean continueReload = max - ammo > 1;
        if (ammo < max) {
            if (player.isCreative()) {
                item.setAmmoCount(stack, ammo + 1);
                if (continueReload) {
                    this.startReloading(player, item.getReloadTime(player), stack);
                }
                return;
            }
            for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                ItemStack itemStack = player.inventory.getItem(i);
                if (itemStack.getItem() instanceof IAmmoProvider) {
                    IAmmoProvider provider = (IAmmoProvider) itemStack.getItem();
                    if (itemStack.getCount() > 0 && provider.getAmmoType() == ammoType && provider.getMaterial() == material) {
                        itemStack.shrink(1);
                        item.setAmmoCount(stack, ammo + 1);
                        if (continueReload) {
                            this.startReloading(player, item.getReloadTime(player), stack);
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean canBeInterrupted(GunItem item, ItemStack stack) {
        return true;
    }
}
