package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.ModDamageTypes;
import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.gameplay.WeaponExtensionService;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CombatPerkHandler {
    private CombatPerkHandler() {}

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        DamageSource source = event.getSource();
        if (!GunKillHandler.isFirearmBulletDamage(source)) {
            return;
        }
        Player player = resolveShooter(source);
        if (player == null) {
            return;
        }
        String weaponKey = GunKillHandler.heldFirearmKey(player);
        if (weaponKey == null) {
            weaponKey = PlayerFirearmsData.root(player).getString("last_wf_gun");
            if (weaponKey.isEmpty()) {
                weaponKey = "m1911";
            }
        }
        WeaponClass weaponClass = FirearmRegistry.classForWeaponKey(weaponKey);
        double mult = PerkEffectService.firearmDamageMultiplier(player, weaponClass);
        boolean silenced =
                WeaponExtensionService.isSilenced(player, weaponKey)
                        || "vss".equals(weaponKey)
                        || "sks".equals(weaponKey);
        mult *= PerkEffectService.noiseCategoryDamageMultiplier(player, silenced);
        if (isHeadshotAgainst(player, event.getEntity())) {
            mult *= PerkEffectService.headshotDamageMultiplier(player);
            mult *= WeaponExtensionService.headshotBonusMultiplier(player, weaponKey);
        }
        float amount =
                com.wf.firearms.gameplay.WeaponExtensionCombat.damageMultiplier(
                        player, weaponKey, event.getEntity(), (float) (event.getAmount() * mult));
        if (weaponClass == WeaponClass.SHOTGUN
                && PlayerFirearmsData.isUnlocked(player, weaponKey + "_extended_barrel")) {
            amount += 1.0f;
        }
        if (amount != event.getAmount()) {
            event.setAmount(amount);
        }
    }

    public static boolean isHeadshotAgainst(Player player, Entity target) {
        return target != null && target.getY() - player.getY() > 1.2 && player.getLookAngle().y < -0.15;
    }

    private static Player resolveShooter(DamageSource source) {
        if (source.getEntity() instanceof Player p) {
            return p;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof Projectile projectile && projectile.getOwner() instanceof Player p) {
            return p;
        }
        return null;
    }
}
