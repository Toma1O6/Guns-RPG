package com.wf.firearms.gameplay;



import com.wf.firearms.GunsRpg;

import com.wf.firearms.compat.TaczCompat;

import com.wf.firearms.combat.ModDamageTypes;

import com.wf.firearms.data.PlayerFirearmsData;

import com.wf.firearms.data.WeaponMapping;

import com.wf.firearms.item.FirearmItem;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.entity.projectile.Projectile;

import net.minecraft.world.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public final class GunKillHandler {

    private static final String LAST_GUN = "last_wf_gun";



    private GunKillHandler() {}



    @SubscribeEvent

    public static void onLivingHurt(LivingHurtEvent event) {

        if (event.getEntity().level().isClientSide) {

            return;

        }

        Player player = resolveGunPlayer(event.getSource());

        if (player == null || !isFirearmBulletDamage(event.getSource())) {

            return;

        }

        rememberGun(player);

    }



    @SubscribeEvent

    public static void onLivingDeath(LivingDeathEvent event) {

        if (event.getEntity().level().isClientSide) {

            return;

        }

        DamageSource source = event.getSource();

        Player player = resolveGunPlayer(source);

        if (player == null) {

            return;

        }

        String gunId = resolveGunId(player, source);

        if (gunId == null) {

            return;

        }

        ProgressionService.recordGunKill(player, gunId);
        com.wf.firearms.gameplay.WeaponExtensionCombat.onGunKill(player, gunId);

        LivingEntity victim = event.getEntity();
        if ("r45".equals(gunId)
                && PlayerFirearmsData.isUnlocked(player, "r45_ace_of_hearts")
                && CombatPerkHandler.isHeadshotAgainst(player, victim)) {
            int wLv = PlayerFirearmsData.getWeaponLevel(player, "r45");
            player.heal(4f + wLv * 2f);
        }
    }



    private static void rememberGun(Player player) {

        String held = heldFirearmKey(player);

        if (held != null) {

            PlayerFirearmsData.root(player).putString(LAST_GUN, held);

        }

    }



    private static String resolveGunId(Player player, DamageSource source) {

        String held = heldFirearmKey(player);

        if (held != null) {

            rememberGun(player);

            return held;

        }

        if (isFirearmBulletDamage(source)) {

            String last = PlayerFirearmsData.root(player).getString(LAST_GUN);

            if (!last.isEmpty()) {

                return last;

            }

            return "m1911";

        }

        return null;

    }



    static boolean isFirearmBulletDamage(DamageSource source) {
        if (TaczCompat.isTaczBulletDamage(source)) {
            return true;
        }
        return ModDamageTypes.isBullet(source);
    }



    /** @deprecated 使用 {@link #isFirearmBulletDamage} */

    @Deprecated

    static boolean isCgmBulletDamage(DamageSource source) {

        return isFirearmBulletDamage(source);

    }



    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        Player player = resolveGunPlayer(event.getSource());
        if (player == null) {
            return;
        }
        String gunId = resolveGunId(player, event.getSource());
        if (gunId == null) {
            return;
        }
        LivingEntity victim = event.getEntity();
        boolean harvester = WeaponExtensionIds.isUnlocked(player, gunId, "harvester");
        boolean unarmoredLoot = WeaponExtensionIds.isUnlocked(player, gunId, "unarmored_loot")
                && victim.getArmorValue() <= 0;
        if (!harvester && !unarmoredLoot) {
            return;
        }
        if (harvester && player.getRandom().nextFloat() >= 0.15f && !unarmoredLoot) {
            return;
        }
        List<ItemEntity> extras = new ArrayList<>();
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.isEmpty()) {
                continue;
            }
            ItemEntity copy = new ItemEntity(
                    drop.level(),
                    drop.getX(),
                    drop.getY(),
                    drop.getZ(),
                    stack.copy());
            copy.setDefaultPickUpDelay();
            copy.setDeltaMovement(drop.getDeltaMovement());
            extras.add(copy);
        }
        event.getDrops().addAll(extras);
    }

    private static Player resolveGunPlayer(DamageSource source) {

        if (source.getEntity() instanceof Player p) {

            return p;

        }

        Entity direct = source.getDirectEntity();

        if (direct instanceof Projectile projectile) {

            Entity owner = projectile.getOwner();

            if (owner instanceof Player p) {

                return p;

            }

        }

        return null;

    }



    public static String heldFirearmKey(Player player) {
        for (ItemStack stack : new ItemStack[] {player.getMainHandItem(), player.getOffhandItem()}) {
            if (stack.getItem() instanceof FirearmItem firearm) {
                return firearm.getWeaponKey();
            }
            Optional<String> mapped = WeaponMapping.weaponKeyFromStack(stack);
            if (mapped.isPresent()) {
                return mapped.get();
            }
        }
        return null;
    }

    static String heldFirearmItemId(Player player) {
        for (ItemStack stack : new ItemStack[] {player.getMainHandItem(), player.getOffhandItem()}) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (id == null) {
                continue;
            }
            if (stack.getItem() instanceof FirearmItem
                    || WeaponMapping.weaponKeyForItem(id.toString()).isPresent()) {
                return id.toString();
            }
        }
        return null;
    }

}

