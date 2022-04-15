package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface IInputEventHandler {

    void invokeEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data);

    class Single implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data) {
            Utils.shootWithValidation(player, stack, data);
        }
    }

    class Burst implements IInputEventHandler {

        private final BooleanSupplier shootReady = ShootingManager::isShootingReady;
        private int shotsLeft;
        private Item heldItem;

        @Override
        public void invokeEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data) {
            switch (event) {
                case ON_INPUT:
                    handleInput(stack);
                    break;
                case ON_BURST_TICK:
                    handleTick(player, stack, data);
                    break;
            }
        }

        private void handleInput(ItemStack stack) {
            if (!ShootingManager.Client.isBurstModeActive()) {
                ShootingManager.Client.setBurstActive(true);
                shotsLeft = 3;
                heldItem = stack.getItem();
            }
        }

        private void handleTick(PlayerEntity player, ItemStack stack, IPlayerData data) {
            if (shootReady.getAsBoolean()) {
                if (heldItem == stack.getItem() && ShootingManager.canShoot(player, stack)) {
                    ShootingManager.Client.shoot(player, stack, data);
                    if (--shotsLeft <= 0) {
                        ShootingManager.Client.setBurstActive(false);
                    }
                } else {
                    ShootingManager.Client.setBurstActive(false);
                }
            }
        }
    }

    class FullAuto implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data) {
            Utils.shootWithValidation(player, stack, data);
        }
    }

    class Double implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data) {
            int ammo = AbstractGun.getAmmoCount(stack);
            boolean canShootSecond = ammo > 1;
            Utils.shootWithValidation(player, stack, data);
            if (canShootSecond && Utils.isWeaponInShootableState(stack)) {
                ShootingManager.Client.shoot(player, stack, data);
            }
        }
    }

    class Barrage implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data) {
            if (ShootingManager.canShoot(player, stack)) {
                ShootingManager.Client.shoot(player, stack, data);
                ShootingManager.Client.forceShootDelay(3);
            }
        }
    }

    class Utils {

        static boolean isWeaponInShootableState(ItemStack stack) {
            return stack.getDamageValue() < stack.getMaxDamage() && !((GunItem) stack.getItem()).isJammed(stack);
        }

        static void shootWithValidation(PlayerEntity player, ItemStack stack, IPlayerData data) {
            if (ShootingManager.canShoot(player, stack)) {
                ShootingManager.Client.shoot(player, stack, data);
            }
        }
    }
}
