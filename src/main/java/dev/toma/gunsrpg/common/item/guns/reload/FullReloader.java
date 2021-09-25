package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.api.common.IReloader;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.animation.ReloadAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class FullReloader implements IReloader {

    private boolean reloading;
    private int ticksLeft;
    private GunItem gun;
    private ItemStack stack;

    @Override
    public void initiateReload(PlayerEntity player, GunItem item, ItemStack _stack) {
        reloading = true;
        ticksLeft = item.getReloadTime(PlayerData.getUnsafe(player).getAttributes());
        gun = item;
        stack = _stack;
        if (player.level.isClientSide) {
            playAnimation(player);
        }
    }

    @Override
    public boolean isReloading() {
        return reloading;
    }

    @Override
    public void enqueueCancel() {
        // never called
    }

    @Override
    public void forceCancel() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> this::cancelAnimations);
    }

    @Override
    public void tick(PlayerEntity player) {
        if (reloading && --ticksLeft <= 0) {
            reloading = false;
            onReload(player);
        }
    }

    private void onReload(PlayerEntity player) {
        if (!(gun instanceof GunItem)) return;
        int weaponLimit = gun.getMaxAmmo(PlayerData.getUnsafe(player).getAttributes());
        int currentAmmo = gun.getAmmo(stack);
        int toLoad = weaponLimit - currentAmmo;
        AmmoType ammoType = gun.getAmmoType();
        IAmmoMaterial material = gun.getMaterialFromNBT(stack);
        PlayerInventory inventory = player.inventory;
        if (!player.isCreative()) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack.getItem() instanceof IAmmoProvider) {
                    IAmmoProvider provider = (IAmmoProvider) stack.getItem();
                    if (provider.getAmmoType() == ammoType && provider.getMaterial() == material) {
                        int count = stack.getCount();
                        int load = Math.min(toLoad, count);
                        toLoad -= load;
                        stack.shrink(load);
                    }
                    if (toLoad <= 0) {
                        break;
                    }
                }
            }
        } else {
            toLoad = 0;
        }
        gun.setAmmoCount(stack, weaponLimit - toLoad);
    }

    @OnlyIn(Dist.CLIENT)
    private void playAnimation(PlayerEntity player) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        ResourceLocation path = gun.getReloadAnimation(player);
        ReloadAnimation animation = AnimationUtils.createAnimation(path, provider -> new ReloadAnimation(provider, ticksLeft));
        pipeline.insert(ModAnimations.RELOAD, animation);
    }

    @OnlyIn(Dist.CLIENT)
    private void cancelAnimations() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.remove(ModAnimations.RELOAD);
    }
}
