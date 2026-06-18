package com.wf.firearms.combat;

import com.wf.firearms.entity.LaunchedExplosiveEntity;
import com.wf.firearms.gameplay.WeaponExtensionService;
import com.wf.firearms.launcher.LauncherShellStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/** 榴弹/火箭发射器开火：生成 {@link LaunchedExplosiveEntity}。 */
public final class LauncherShooter {
    private LauncherShooter() {}

    public static boolean isLauncher(String weaponKey) {
        return "grenade_launcher".equals(weaponKey) || "rocket_launcher".equals(weaponKey);
    }

    public static boolean isLauncher(FirearmSpec spec) {
        return spec != null && spec.weaponClass() == WeaponClass.LAUNCHER;
    }

    public static boolean fire(Player player, ItemStack gunStack, FirearmSpec spec, boolean aiming) {
        if (player.level().isClientSide) {
            return false;
        }
        FirearmStackState.ensureInitialized(gunStack, spec);
        if (!FirearmCombat.canFire(player, gunStack, spec)) {
            return false;
        }
        Item shellItem = resolveLoadedShell(gunStack);
        if (shellItem == null) {
            shellItem = PlayerAmmoInventory.resolveReloadAmmo(player, spec, gunStack);
        }
        if (shellItem == null) {
            return false;
        }
        spawnProjectile(player, gunStack, shellItem, spec, aiming);
        FirearmCombat.onShotFired(player, gunStack, spec, player.level());
        return true;
    }

    /** TaCZ M320 / RPG-7：击发时生成 gunsrpg 爆炸弹体。 */
    public static void fireTacz(Player player, ItemStack gun, FirearmSpec spec) {
        if (player.level().isClientSide || !isLauncher(spec.weaponKey())) {
            return;
        }
        Item shellItem = resolveLoadedShell(gun);
        if (shellItem == null) {
            shellItem = PlayerAmmoInventory.resolveReloadAmmo(player, spec, gun);
        }
        if (shellItem == null) {
            return;
        }
        if (!FirearmStackState.hasLoadedAmmoType(gun)) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(shellItem);
            if (id != null) {
                FirearmStackState.setLoadedAmmoItem(gun, id.toString());
            }
        }
        spawnProjectile(player, gun, shellItem, spec, false);
    }

    private static void spawnProjectile(
            Player player, ItemStack gunStack, Item shellItem, FirearmSpec spec, boolean aiming) {
        LauncherShellStats base = LauncherShellStats.forItem(shellItem);
        float radiusMul = WeaponExtensionService.blastRadiusMultiplier(player, spec.weaponKey());
        float speedMul = WeaponExtensionService.launchSpeedMultiplier(player, spec.weaponKey());
        float dmgMul = (float) WeaponExtensionService.damageMultiplier(player, spec.weaponKey());
        float radius = base.blastRadius() * radiusMul;
        float damage = Math.max(1f, base.explosionDamage() * dmgMul);
        boolean rocket = base.rocket() || "rocket_launcher".equals(spec.weaponKey());
        ItemStack visual = new ItemStack(shellItem);
        LaunchedExplosiveEntity projectile =
                new LaunchedExplosiveEntity(
                        player.level(),
                        player,
                        visual,
                        base.fuseTicks(),
                        radius,
                        damage,
                        base.explodeOnImpact() || rocket,
                        rocket,
                        base.incendiary(),
                        base.toxic());
        projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        float spread = FirearmCombat.spreadForShot(player, gunStack, spec, aiming);
        float speed = spec.projectileSpeed() * (rocket ? 1.8f : 1.1f) * speedMul;
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, speed, spread);
        player.level().addFreshEntity(projectile);
    }

    private static Item resolveLoadedShell(ItemStack gunStack) {
        String id = FirearmStackState.getLoadedAmmoItemId(gunStack);
        if (id == null || id.isEmpty()) {
            return null;
        }
        ResourceLocation loc = ResourceLocation.tryParse(id.contains(":") ? id : "gunsrpg:" + id);
        if (loc == null) {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(loc);
    }
}
