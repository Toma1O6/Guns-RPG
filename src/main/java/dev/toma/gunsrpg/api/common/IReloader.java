package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IReloader {

    IReloader EMPTY = new Dummy();

    /**
     * Initiates reload when player presses 'R' and only when player isn't already reloading.
     * In case player is reloading and {@link IReloadManager} is {@code cancelable},
     * {@link IReloader#enqueueCancel()} is called instead.
     *
     * @param player Player who started reloading
     * @param item Held gun
     * @param stack Gun itemstack
     */
    void initiateReload(PlayerEntity player, GunItem item, ItemStack stack);

    boolean isReloading();

    void enqueueCancel();

    /**
     * Called when player performs action, which forbids default cancelation behaviour such as
     * switching to other items etc. Should be used mainly to cancel animations
     */
    void forceCancel();

    void tick(PlayerEntity player);

    default boolean isEmpty() {
        return this == EMPTY;
    }

    class Dummy implements IReloader {

        @Override
        public boolean isReloading() {
            return false;
        }

        @Override
        public void enqueueCancel() {
        }

        @Override
        public void forceCancel() {
        }

        @Override
        public void tick(PlayerEntity player) {
        }

        @Override
        public void initiateReload(PlayerEntity player, GunItem item, ItemStack stack) {
        }
    }
}
