package com.wf.firearms.data;



import com.google.gson.JsonObject;

import com.google.gson.JsonParser;

import com.wf.firearms.GunsRpg;

import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczGiveGun;
import com.wf.firearms.compat.tacz.TaczWeaponBinding;
import com.wf.firearms.compat.tacz.TaczWeaponCatalog;
import com.wf.firearms.config.TaczBackendConfig;

import com.wf.firearms.item.FirearmItem;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;

import net.minecraftforge.registries.ForgeRegistries;



import java.io.IOException;

import java.io.Reader;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;

import java.nio.file.Path;

import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.Map;

import java.util.Optional;



/** Guns RPG 武器 key ↔ 本 mod 物品；{@code config/gunsrpg/weapon_mapping.json}。 */

public final class WeaponMapping {

    private static final Map<String, String> WEAPON_TO_ITEM = new LinkedHashMap<>();

    private static final Map<String, String> ASSEMBLY_ICON = new HashMap<>();

    private static final Map<String, String> ITEM_TO_WEAPON = new HashMap<>();

    private static final Map<String, String> DISPLAY_NAMES = new HashMap<>();



    private WeaponMapping() {}



    public static void reload() {

        WEAPON_TO_ITEM.clear();

        ASSEMBLY_ICON.clear();

        ITEM_TO_WEAPON.clear();

        DISPLAY_NAMES.clear();

        Path file = PortPaths.configRoot().resolve("weapon_mapping.json");

        if (!Files.isRegularFile(file)) {

            applyBuiltinDefaults();

            GunsRpg.LOGGER.warn("[gunsrpg] 使用内置武器映射（无 weapon_mapping.json）");

            return;

        }

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {

            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            loadMap(root, "weapons", WEAPON_TO_ITEM);

            loadMap(root, "assembly_icons", ASSEMBLY_ICON);

            loadMap(root, "display_names", DISPLAY_NAMES);

            for (Map.Entry<String, String> e : WEAPON_TO_ITEM.entrySet()) {

                ITEM_TO_WEAPON.putIfAbsent(e.getValue(), e.getKey());

            }

            GunsRpg.LOGGER.info(

                    "[gunsrpg] 武器映射 {} 条，装配体图标 {} 条",

                    WEAPON_TO_ITEM.size(),

                    ASSEMBLY_ICON.size());

        } catch (IOException ex) {

            GunsRpg.LOGGER.error("[gunsrpg] 读取 weapon_mapping.json 失败，使用内置默认", ex);

            applyBuiltinDefaults();

        }

        applyShootingBackendOverrides();

    }

    /** TaCZ 射击后端：wf key 不变，物品层映射到 TaCZ GunId。 */
    private static void applyShootingBackendOverrides() {
        if (TaczBackendConfig.useTaczShooting()) {
            applyTaczModeOverrides();
        }
    }

    /** {@code use_tacz_as_shooting_backend} 时，装配/枪械台产出 TaCZ 枪（GunId NBT）。 */
    private static void applyTaczModeOverrides() {
        for (Map.Entry<String, String> e : TaczBackendConfig.weaponMap().entrySet()) {
            String key = e.getKey();
            String gunId = e.getValue();
            WEAPON_TO_ITEM.put(key, gunId);
            ASSEMBLY_ICON.put(key + "_assembly", gunId);
        }
        ITEM_TO_WEAPON.clear();
        GunsRpg.LOGGER.info(
                "[gunsrpg] TaCZ 射击后端：{} 把 wf 枪映射到 TaCZ GunId",
                TaczBackendConfig.weaponMap().size());
    }

    private static void applyBuiltinDefaults() {
        Map<String, String> source =
                TaczBackendConfig.useTaczShooting()
                        ? TaczBackendConfig.weaponMap()
                        : defaultWfGunIds();
        for (Map.Entry<String, String> e : source.entrySet()) {
            WEAPON_TO_ITEM.put(e.getKey(), e.getValue());
            ASSEMBLY_ICON.put(e.getKey() + "_assembly", e.getValue());
        }
        DISPLAY_NAMES.put("glock", "格洛克");
        DISPLAY_NAMES.put("m1911", "M1911");
        DISPLAY_NAMES.put("r45", "金牛座");
        DISPLAY_NAMES.put("desert_eagle", "沙漠之鹰");
        DISPLAY_NAMES.put("thompson", "MP5");
        DISPLAY_NAMES.put("ump45", "UMP45");
        DISPLAY_NAMES.put("vector", "Vector");
        DISPLAY_NAMES.put("uzi", "乌兹");
        DISPLAY_NAMES.put("p90", "P90");
        DISPLAY_NAMES.put("akm", "AKM");
        DISPLAY_NAMES.put("hk416", "HK-416");
        DISPLAY_NAMES.put("type_81", "81-1式");
        DISPLAY_NAMES.put("aug", "AUG");
        DISPLAY_NAMES.put("vss", "G36C");
        DISPLAY_NAMES.put("fn_fal", "FN FAL");
        DISPLAY_NAMES.put("sks", "SKS");
        DISPLAY_NAMES.put("spr15", "SPR-15");
        DISPLAY_NAMES.put("mk14ebr", "Mk14 EBR");
        DISPLAY_NAMES.put("kar98k", "Kar98k");
        DISPLAY_NAMES.put("winchester", "M24");
        DISPLAY_NAMES.put("awm", "AWM");
        DISPLAY_NAMES.put("m95", "M95");
        DISPLAY_NAMES.put("db2", "DB-2");
        DISPLAY_NAMES.put("s686", "S686");
        DISPLAY_NAMES.put("s1897", "S1897");
        DISPLAY_NAMES.put("s12k", "SPAS-12");
        DISPLAY_NAMES.put("pkm", "PKM");
        DISPLAY_NAMES.put("m249", "M249 轻机枪");
        DISPLAY_NAMES.put("gatling", "M134 加特林");
        DISPLAY_NAMES.put("grenade_launcher", "榴弹发射器");
        DISPLAY_NAMES.put("rocket_launcher", "火箭筒");

        for (Map.Entry<String, String> e : WEAPON_TO_ITEM.entrySet()) {

            ITEM_TO_WEAPON.put(e.getValue(), e.getKey());

        }

    }

    private static Map<String, String> defaultWfGunIds() {
        Map<String, String> map = new LinkedHashMap<>();
        String[] guns = {
            "glock", "m1911", "r45", "desert_eagle", "ump45", "vector", "uzi", "p90",
            "akm", "hk416", "type_81", "aug",
            "fn_fal", "sks", "spr15", "mk14ebr",
            "kar98k", "winchester", "awm", "m95",
            "db2", "s1897", "s12k", "s686",
            "pkm", "m249", "gatling"
        };
        for (String g : guns) {
            map.put(g, "gunsrpg:" + g);
        }
        return map;
    }

    private static void loadMap(JsonObject root, String key, Map<String, String> target) {

        if (!root.has(key)) {

            return;

        }

        JsonObject obj = root.getAsJsonObject(key);

        for (var entry : obj.entrySet()) {

            if (entry.getKey().startsWith("_")) {

                continue;

            }

            target.put(entry.getKey(), entry.getValue().getAsString());

        }

    }



    public static Optional<String> itemIdForWeapon(String weaponKey) {

        return Optional.ofNullable(WEAPON_TO_ITEM.get(weaponKey));

    }



    /** @deprecated 使用 {@link #itemIdForWeapon} */

    @Deprecated

    public static Optional<String> cgmIdForWeapon(String gunsrpgWeapon) {

        return itemIdForWeapon(gunsrpgWeapon);

    }



    public static Optional<String> weaponKeyForItem(String itemId) {

        return Optional.ofNullable(ITEM_TO_WEAPON.get(itemId));

    }



    public static ItemStack iconForAssembly(String assemblyId) {

        String itemId = ASSEMBLY_ICON.get(assemblyId);

        if (itemId == null && assemblyId.endsWith("_assembly")) {

            String base = assemblyId.substring(0, assemblyId.length() - "_assembly".length());

            itemId = WEAPON_TO_ITEM.get(base);

        }

        if (TaczBackendConfig.useTaczShooting() && itemId != null && itemId.indexOf(':') >= 0) {
            String base = assemblyId.endsWith("_assembly")
                    ? assemblyId.substring(0, assemblyId.length() - "_assembly".length())
                    : assemblyId;
            return stackForWeapon(base);
        }
        return stackFromId(itemId);

    }



    public static String displayNameForWeaponKey(String weaponKey) {

        return DISPLAY_NAMES.getOrDefault(weaponKey, weaponKey);

    }



    public static String assemblyTitleLine(String assemblyId) {

        if (!assemblyId.endsWith("_assembly")) {

            return assemblyId;

        }

        String key = assemblyId.substring(0, assemblyId.length() - "_assembly".length());

        return displayNameForWeaponKey(key);

    }



    public static ItemStack craftResultForAssembly(String assemblyId) {

        if (!assemblyId.endsWith("_assembly")) {

            return ItemStack.EMPTY;

        }

        String key = assemblyId.substring(0, assemblyId.length() - "_assembly".length());

        return itemIdForWeapon(key).map(WeaponMapping::stackForWeapon).orElse(ItemStack.EMPTY);

    }



    public static ItemStack iconForWeaponSkill(String weaponKey) {

        return stackForWeapon(weaponKey);

    }

    public static ItemStack stackForWeapon(String weaponKey) {
        if (TaczBackendConfig.useTaczShooting()) {
            return TaczGiveGun.create(weaponKey, false);
        }
        return itemIdForWeapon(weaponKey).map(WeaponMapping::stackFromId).orElse(ItemStack.EMPTY);
    }



    public static Optional<String> weaponKeyFromStack(ItemStack stack) {

        if (stack.isEmpty()) {

            return Optional.empty();

        }

        if (stack.getItem() instanceof FirearmItem firearm) {

            return Optional.of(firearm.getWeaponKey());

        }

        if (TaczBackendConfig.useTaczShooting() && TaczBridge.isTaczGun(stack)) {
            Optional<String> bound = TaczWeaponBinding.read(stack);
            if (bound.isPresent()) {
                return bound;
            }
            return TaczBridge.readGunId(stack).flatMap(TaczWeaponCatalog::weaponKeyForGunId);
        }

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (id == null) {

            return Optional.empty();

        }

        return weaponKeyForItem(id.toString());

    }



    private static ItemStack stackFromId(String itemId) {

        if (itemId == null) {

            return ItemStack.EMPTY;

        }

        ResourceLocation loc = ResourceLocation.tryParse(itemId);

        if (loc == null) {

            return ItemStack.EMPTY;

        }

        Item item = ForgeRegistries.ITEMS.getValue(loc);

        return item == null ? ItemStack.EMPTY : new ItemStack(item);

    }

}

