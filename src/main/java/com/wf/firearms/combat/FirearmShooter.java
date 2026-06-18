package com.wf.firearms.combat;

import com.wf.firearms.entity.BulletProjectile;
import com.wf.firearms.item.FirearmItem;
import com.wf.firearms.registry.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class FirearmShooter {
    private FirearmShooter() {}

    public static boolean tryFire(Player player, ItemStack gunStack, FirearmSpec spec, boolean aiming) {
        if (player.level().isClientSide || !(gunStack.getItem() instanceof FirearmItem)) {
            return false;
        }
        FirearmStackState.ensureInitialized(gunStack, spec);
        if (!FirearmCombat.canFire(player, gunStack, spec)) {
            if (FirearmStackState.getAmmo(gunStack) <= 0
                    || !FirearmStackState.hasLoadedAmmoType(gunStack)) {
                player.displayClientMessage(Component.translatable("gunsrpg.gun.need_reload"), true);
            }
            return false;
        }

        Level level = player.level();
        if (!fireSingleVolley(player, gunStack, spec, aiming, level)) {
            return false;
        }

        int interval = Math.max(1, spec.fireIntervalTicks());
        player.getCooldowns().addCooldown(gunStack.getItem(), interval);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.PLAYERS,
                0.25f,
                FirearmCombat.isAutomaticMode(gunStack, spec)
                        ? 1.6f + level.random.nextFloat() * 0.2f
                        : 1.2f);
        FirearmBurstHandler.scheduleBurst(player, gunStack, spec);
        return true;
    }

    /** 连射后续发（由 {@link FirearmBurstHandler} 调度，不走扳机冷却）。 */
    public static boolean fireBurstFollowUp(Player player, ItemStack gunStack, FirearmSpec spec) {
        if (player.level().isClientSide || !(gunStack.getItem() instanceof FirearmItem)) {
            return false;
        }
        FirearmStackState.ensureInitialized(gunStack, spec);
        if (FirearmStackState.isBusy(gunStack)
                || FirearmStackState.isJammed(gunStack)
                || FirearmStackState.getAmmo(gunStack) <= 0
                || !FirearmStackState.hasLoadedAmmoType(gunStack)) {
            return false;
        }
        Level level = player.level();
        boolean aiming = FirearmStackState.isAiming(gunStack);
        if (!fireSingleVolley(player, gunStack, spec, aiming, level)) {
            return false;
        }
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.PLAYERS,
                0.22f,
                1.15f + level.random.nextFloat() * 0.15f);
        return true;
    }

    private static boolean fireSingleVolley(
            Player player, ItemStack gunStack, FirearmSpec spec, boolean aiming, Level level) {
        if (LauncherShooter.isLauncher(spec)) {
            return LauncherShooter.fire(player, gunStack, spec, aiming);
        }
        float damage = FirearmCombat.damageForShot(player, gunStack, spec);
        AmmoMaterial mat = FirearmStackState.getLoadedMaterial(gunStack);
        AmmoCaliber caliber = AmmoCaliber.forWeapon(spec);
        float baseSpread = FirearmCombat.spreadForShot(player, gunStack, spec, aiming);
        int pellets = com.wf.firearms.gameplay.WeaponExtensionCombat.pelletCount(
                player, spec.weaponKey(), Math.max(1, spec.pelletCount()));
        boolean pelletDecay = pellets > 1;
        for (int i = 0; i < pellets; i++) {
            BulletProjectile bullet =
                    new BulletProjectile(
                            ModEntities.BULLET.get(),
                            level,
                            player,
                            damage,
                            mat,
                            caliber,
                            spec.weaponClass(),
                            spec.weaponKey(),
                            pelletDecay);
            bullet.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            float spread = pellets > 1 ? baseSpread * (0.85f + level.random.nextFloat() * 0.3f) : baseSpread;
            bullet.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0f,
                    spec.projectileSpeed(),
                    spread);
            level.addFreshEntity(bullet);
        }
        FirearmCombat.onShotFired(player, gunStack, spec, level);
        return true;
    }
}
