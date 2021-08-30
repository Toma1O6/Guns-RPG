package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloader;
import dev.toma.gunsrpg.util.AmmoLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ReloadInfo {

    private final PlayerData factory;

    private IReloader activeReloadManager = IReloader.EMPTY;
    private int reloadingSlot;

    public ReloadInfo(PlayerData factory) {
        this.factory = factory;
    }

    public void enqueueCancel() {
        activeReloadManager.enqueueCancel();
    }

    public void tick() {
        PlayerEntity owner = factory.getPlayer();
        int equippedSlot = owner.inventory.selected;
        if (equippedSlot != reloadingSlot) {
            activeReloadManager.forceCancel();
            activeReloadManager = IReloader.EMPTY;
        }
        activeReloadManager.tick(factory.getPlayer());
    }

    public void startReloading(PlayerEntity player, GunItem gun, ItemStack stack, int slot) {
        AmmoLocator locator = new AmmoLocator();
        AmmoType ammoType = gun.getAmmoType();
        IAmmoMaterial material = gun.getMaterialFromNBT(stack);
        if (material != null && (player.isCreative() || locator.hasAmmo(player.inventory, AmmoLocator.ISearchConstraint.typeAndMaterial(ammoType, material)))) {
            activeReloadManager = gun.getReloadManager(player).createReloadHandler();
            activeReloadManager.initiateReload(player, gun, stack);
            reloadingSlot = slot;
        }
    }

    public boolean isReloading() {
        return activeReloadManager.isReloading();
    }
}
