package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloader;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.animation.ReloadAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
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

import java.util.List;
import java.util.stream.Collectors;

public class FullReloader implements IReloader {

    private IAnimationProvider animationProvider;
    private boolean reloading;
    private int ticksLeft;
    private GunItem gun;
    private ItemStack stack;

    public FullReloader() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> this.animationProvider = IAnimationProvider.DEFAULT_PROVIDER);
    }

    public FullReloader(IAnimationProvider provider) {
        this.animationProvider = provider;
    }

    @Override
    public void initiateReload(PlayerEntity player, GunItem item, ItemStack _stack) {
        reloading = true;
        ticksLeft = item.getReloadTime(PlayerData.getUnsafe(player).getAttributes(), _stack);
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
        IPlayerData data = PlayerData.getUnsafe(player);
        int weaponLimit = gun.getMaxAmmo(data.getAttributes());
        int currentAmmo = gun.getAmmo(stack);
        int toLoad = weaponLimit - currentAmmo;
        AmmoType ammoType = gun.getAmmoType();
        IAmmoMaterial material = gun.getMaterialFromNBT(stack);
        PlayerInventory inventory = player.inventory;
        if (!player.isCreative()) {
            while (true) {
                boolean consumed = ItemLocator.consume(inventory, ItemLocator.filterByAmmoTypeAndMaterial(ammoType, material), ctx -> {
                    int ammo = gun.getAmmo(stack);
                    int remaining = weaponLimit - ammo;
                    ItemStack stack = ctx.getCurrectStack();
                    int count = stack.getCount();
                    int load = Math.min(remaining, count);
                    stack.shrink(load);
                    gun.setAmmoCount(player.getMainHandItem(), ammo + load);
                });
                if (!consumed || weaponLimit - gun.getAmmo(stack) <= 0) {
                    break;
                }
            }
        } else {
            gun.setAmmoCount(player.getMainHandItem(), weaponLimit);
        }
        data.getHandState().freeHands();
    }

    @OnlyIn(Dist.CLIENT)
    private void playAnimation(PlayerEntity player) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        ResourceLocation path = animationProvider.getReloadAnimationPath(gun, player);
        ReloadAnimation animation = AnimationUtils.createAnimation(path, provider -> new ReloadAnimation(provider, ticksLeft));
        pipeline.insert(ModAnimations.RELOAD, animation);
    }

    @OnlyIn(Dist.CLIENT)
    private void cancelAnimations() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.remove(ModAnimations.RELOAD);
    }

    @FunctionalInterface
    public interface IAnimationProvider {

        IAnimationProvider DEFAULT_PROVIDER = GunItem::getReloadAnimation;

        ResourceLocation getReloadAnimationPath(GunItem item, PlayerEntity player);
    }
}
