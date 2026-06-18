package com.wf.firearms.registry;

import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.compat.tacz.TaczGiveGun;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** 创造栏枪械（TaCZ 模式发放 TaCZ 枪，否则发放自研 FirearmItem）。 */
public final class CreativeGunCatalog {
    private static final String[] GUN_KEYS = {
        "glock", "m1911", "r45", "desert_eagle", "ump45", "vector", "uzi", "p90",
        "akm", "hk416", "type_81", "aug",
        "fn_fal", "sks", "spr15", "mk14ebr",
        "kar98k", "winchester", "awm", "m95",
        "db2", "s1897", "s12k", "s686",
        "pkm", "m249", "gatling",
        "grenade_launcher", "rocket_launcher"
    };

    private CreativeGunCatalog() {}

    public static String[] gunKeys() {
        return GUN_KEYS.clone();
    }

    public static ItemStack creativeStack(String weaponKey) {
        if (TaczBackendConfig.useTaczShooting()) {
            return taczCreativeStack(weaponKey);
        }
        return nativeCreativeStack(weaponKey);
    }

    private static ItemStack taczCreativeStack(String weaponKey) {
        ItemStack stack = TaczGiveGun.create(weaponKey, true);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        stack.setHoverName(Component.literal(WeaponMapping.displayNameForWeaponKey(weaponKey)));
        stack.getOrCreateTag().putInt("gunsrpg_creative_idx", weaponKey.hashCode());
        return stack;
    }

    private static ItemStack nativeCreativeStack(String weaponKey) {
        Item item = nativeItem(weaponKey);
        if (item == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(item);
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        FirearmStackState.ensureInitialized(stack, spec);
        stack.setHoverName(Component.literal(WeaponMapping.displayNameForWeaponKey(weaponKey)));
        return stack;
    }

    private static Item nativeItem(String weaponKey) {
        return switch (weaponKey) {
            case "m1911" -> ModItems.M1911.get();
            case "r45" -> ModItems.R45.get();
            case "desert_eagle" -> ModItems.DESERT_EAGLE.get();
            case "thompson" -> ModItems.THOMPSON.get();
            case "ump45" -> ModItems.UMP45.get();
            case "vector" -> ModItems.VECTOR.get();
            case "akm" -> ModItems.AKM.get();
            case "hk416" -> ModItems.HK416.get();
            case "aug" -> ModItems.AUG.get();
            case "fn_fal" -> ModItems.FN_FAL.get();
            case "sks" -> ModItems.SKS.get();
            case "spr15" -> ModItems.SPR15.get();
            case "mk14ebr" -> ModItems.MK14EBR.get();
            case "kar98k" -> ModItems.KAR98K.get();
            case "winchester" -> ModItems.WINCHESTER.get();
            case "awm" -> ModItems.AWM.get();
            case "m95" -> ModItems.M95.get();
            case "db2" -> ModItems.DB2.get();
            case "s686" -> ModItems.S686.get();
            case "s1897" -> ModItems.S1897.get();
            case "s12k" -> ModItems.S12K.get();
            case "pkm", "m249", "gatling" -> null;
            case "grenade_launcher" -> ModItems.GRENADE_LAUNCHER.get();
            case "rocket_launcher" -> ModItems.ROCKET_LAUNCHER.get();
            default -> null;
        };
    }
}
