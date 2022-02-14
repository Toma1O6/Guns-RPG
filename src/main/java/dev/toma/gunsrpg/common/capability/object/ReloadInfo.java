package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloader;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ReloadInfo implements IReloadInfo {

    private final IAttributeRef attrRef;
    private final IHandState handState;
    private IReloader activeReloadManager = IReloader.EMPTY;
    private int reloadingSlot;

    public ReloadInfo(IAttributeRef attrRef, IHandState state) {
        this.attrRef = attrRef;
        this.handState = state;
    }

    @Override
    public void enqueueCancel() {
        activeReloadManager.enqueueCancel();
    }

    @Override
    public void tick(PlayerEntity player) {
        int equippedSlot = player.inventory.selected;
        if (equippedSlot != reloadingSlot) {
            activeReloadManager.forceCancel();
            activeReloadManager = IReloader.EMPTY;
            handState.freeHands();
        }
        activeReloadManager.tick(player);
    }

    @Override
    public void startReloading(PlayerEntity player, GunItem gun, ItemStack stack, int slot) {
        AmmoType ammoType = gun.getAmmoType();
        IAmmoMaterial material = gun.getMaterialFromNBT(stack);
        if (material != null && (player.isCreative() || !ItemLocator.findFirst(player.inventory, ItemLocator.typeAndMaterial(ammoType, material)).isEmpty())) {
            activeReloadManager = gun.getReloadManager(player, attrRef.getProviderReference()).createReloadHandler();
            activeReloadManager.initiateReload(player, gun, stack);
            reloadingSlot = slot;
            handState.setHandsBusy();
        }
    }

    @Override
    public boolean isReloading() {
        return activeReloadManager.isReloading();
    }

    @FunctionalInterface
    public interface IAttributeRef {
        IAttributeProvider getProviderReference();
    }
}
