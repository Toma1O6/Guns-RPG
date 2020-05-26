package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ReloadManagerMagazine implements IReloadManager {

    public static final ReloadManagerMagazine MANAGER = new ReloadManagerMagazine();

    @Override
    public boolean finishReload(EntityPlayer player, GunItem item, ItemStack stack) {
        AmmoType type = item.getAmmoType();
        AmmoMaterial material = item.getMaterialFromNBT(stack);
        if(material == null) return true;
        int max = item.getMaxAmmo(player);
        int left = max - item.getAmmo(stack);
        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if(player.isCreative()) {
                left = 0;
                break;
            }
            ItemStack ammoStack = player.inventory.getStackInSlot(i);
            if(ammoStack.getItem() instanceof IAmmoProvider) {
                IAmmoProvider ammoProvider = (IAmmoProvider) ammoStack.getItem();
                if(ammoProvider.getAmmoType() == type && ammoProvider.getMaterial() == material) {
                    int count = Math.min(left, ammoStack.getCount());
                    left -= count;
                    ammoStack.shrink(count);
                    if(left <= 0) break;
                }
            }
        }
        item.setAmmoCount(stack, max - left);
        return true;
    }
}
