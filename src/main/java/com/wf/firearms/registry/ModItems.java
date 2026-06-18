package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.item.AmmoItem;
import com.wf.firearms.item.FirearmItem;
import com.wf.firearms.item.GunsRpgFoodItem;
import com.wf.firearms.item.GrenadeItem;
import com.wf.firearms.item.LauncherShellItem;
import com.wf.firearms.item.ModFoods;
import com.wf.firearms.grenade.GrenadeKind;
import com.wf.firearms.grenade.GrenadeMaterial;
import com.wf.firearms.grenade.GrenadeStats;
import com.wf.firearms.item.HealMedicalItem;
import com.wf.firearms.item.MedicalUseItem;
import com.wf.firearms.item.PointAwardItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 火器物品注册（M2：枪族 + 弹药梯队 + 枪械台零件）。 */
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GunsRpg.MOD_ID);

    public static final String[] AMMO_CALIBERS = {"9mm", "45acp", "556mm", "762mm", "magnum", "12g"};

    public static final Map<String, RegistryObject<Item>> AMMO = new LinkedHashMap<>();

    static {
        for (AmmoMaterial material : AmmoMaterial.values()) {
            if (material == AmmoMaterial.UNKNOWN) {
                continue;
            }
            String prefix = material.itemPrefix();
            for (String caliber : AMMO_CALIBERS) {
                if ("magnum".equals(caliber)
                        && (material == AmmoMaterial.WOOD || material == AmmoMaterial.STONE)) {
                    continue;
                }
                String id = prefix + "_" + caliber;
                AMMO.put(id, ITEMS.register(id, () -> new AmmoItem(new Item.Properties().stacksTo(64))));
            }
        }
    }

    // —— 枪械台零件 ——
    public static final RegistryObject<Item> GUN_PARTS = simple("gun_parts");
    public static final RegistryObject<Item> BARREL = simple("barrel");
    public static final RegistryObject<Item> MAGAZINE = simple("magazine");
    public static final RegistryObject<Item> WOODEN_STOCK = simple("wooden_stock");
    public static final RegistryObject<Item> LONG_BARREL = simple("long_barrel");
    public static final RegistryObject<Item> SMALL_IRON_STOCK = simple("small_iron_stock");
    public static final RegistryObject<Item> SMALL_BULLET_CASING = simple("small_bullet_casing");
    public static final RegistryObject<Item> LARGE_BULLET_CASING = simple("large_bullet_casing");
    public static final RegistryObject<Item> SHOTGUN_SHELL = simple("shotgun_shell");
    public static final RegistryObject<Item> BOLT_FLETCHING = simple("bolt_fletching");

    // —— 弹药（兼容旧引用） ——
    public static final RegistryObject<Item> WOODEN_9MM = ammo("wooden_9mm");
    public static final RegistryObject<Item> WOODEN_45ACP = ammo("wooden_45acp");
    public static final RegistryObject<Item> WOODEN_556MM = ammo("wooden_556mm");
    public static final RegistryObject<Item> WOODEN_762MM = ammo("wooden_762mm");
    public static final RegistryObject<Item> WOODEN_12G = ammo("wooden_12g");
    public static final RegistryObject<Item> STONE_9MM = ammo("stone_9mm");
    public static final RegistryObject<Item> STONE_45ACP = ammo("stone_45acp");
    public static final RegistryObject<Item> STONE_556MM = ammo("stone_556mm");
    public static final RegistryObject<Item> STONE_762MM = ammo("stone_762mm");
    public static final RegistryObject<Item> STONE_12G = ammo("stone_12g");
    public static final RegistryObject<Item> IRON_9MM = ammo("iron_9mm");
    public static final RegistryObject<Item> IRON_45ACP = ammo("iron_45acp");
    public static final RegistryObject<Item> IRON_556MM = ammo("iron_556mm");
    public static final RegistryObject<Item> IRON_762MM = ammo("iron_762mm");
    public static final RegistryObject<Item> IRON_12G = ammo("iron_12g");

    // —— 枪械 ——
    public static final RegistryObject<FirearmItem> GLOCK = firearm("glock");
    public static final RegistryObject<FirearmItem> M1911 = firearm("m1911");
    public static final RegistryObject<FirearmItem> R45 = firearm("r45");
    public static final RegistryObject<FirearmItem> DESERT_EAGLE = firearm("desert_eagle");
    public static final RegistryObject<FirearmItem> THOMPSON = firearm("thompson");
    public static final RegistryObject<FirearmItem> UMP45 = firearm("ump45");
    public static final RegistryObject<FirearmItem> VECTOR = firearm("vector");
    public static final RegistryObject<FirearmItem> UZI = firearm("uzi");
    public static final RegistryObject<FirearmItem> P90 = firearm("p90");
    public static final RegistryObject<FirearmItem> AKM = firearm("akm");
    public static final RegistryObject<FirearmItem> HK416 = firearm("hk416");
    public static final RegistryObject<FirearmItem> TYPE_81 = firearm("type_81");
    public static final RegistryObject<FirearmItem> AUG = firearm("aug");
    public static final RegistryObject<FirearmItem> VSS = firearm("vss");
    public static final RegistryObject<FirearmItem> FN_FAL = firearm("fn_fal");
    public static final RegistryObject<FirearmItem> SKS = firearm("sks");
    public static final RegistryObject<FirearmItem> SPR15 = firearm("spr15");
    public static final RegistryObject<FirearmItem> MK14EBR = firearm("mk14ebr");
    public static final RegistryObject<FirearmItem> KAR98K = firearm("kar98k");
    public static final RegistryObject<FirearmItem> WINCHESTER = firearm("winchester");
    public static final RegistryObject<FirearmItem> AWM = firearm("awm");
    public static final RegistryObject<FirearmItem> M95 = firearm("m95");
    public static final RegistryObject<FirearmItem> DB2 = firearm("db2");
    public static final RegistryObject<FirearmItem> S686 = firearm("s686");
    public static final RegistryObject<FirearmItem> S1897 = firearm("s1897");
    public static final RegistryObject<FirearmItem> S12K = firearm("s12k");
    public static final RegistryObject<FirearmItem> PKM = firearm("pkm");
    public static final RegistryObject<FirearmItem> M249 = firearm("m249");
    public static final RegistryObject<FirearmItem> GATLING = firearm("gatling");
    public static final RegistryObject<FirearmItem> MINIGUN = firearm("minigun");

    public static final RegistryObject<FirearmItem> GRENADE_LAUNCHER = firearm("grenade_launcher");
    public static final RegistryObject<FirearmItem> ROCKET_LAUNCHER = firearm("rocket_launcher");

    public static final RegistryObject<Item> GRENADE_LAUNCHER_SHELL = launcherShell("grenade_launcher_shell");
    public static final RegistryObject<Item> IMPACT_GRENADE_LAUNCHER_SHELL =
            launcherShell("impact_grenade_launcher_shell");
    public static final RegistryObject<Item> HIGH_EXPLOSIVE_GRENADE_LAUNCHER_SHELL =
            launcherShell("high_explosive_grenade_launcher_shell");
    public static final RegistryObject<Item> EXPLOSIVE_GRENADE_LAUNCHER_SHELL =
            launcherShell("explosive_grenade_launcher_shell");
    public static final RegistryObject<Item> STICKY_GRENADE_LAUNCHER_SHELL =
            launcherShell("sticky_grenade_launcher_shell");
    public static final RegistryObject<Item> TEAR_GAS_GRENADE_LAUNCHER_SHELL =
            launcherShell("tear_gas_grenade_launcher_shell");
    public static final RegistryObject<Item> ROCKET_SHELL = launcherShell("rocket_shell");

    // —— 医疗品 ——
    public static final RegistryObject<Item> BANDAGE =
            med("bandage", 70);
    public static final RegistryObject<Item> PLASTER_CAST =
            med("plaster_cast", 65);
    public static final RegistryObject<Item> HEMOSTAT =
            med("hemostat", 50);
    public static final RegistryObject<Item> ANTIDOTUM_PILLS =
            med("antidotum_pills", 60);
    public static final RegistryObject<Item> VACCINE =
            med("vaccine", 75);
    public static final RegistryObject<Item> FIELD_BANDAGE =
            heal("field_bandage", 55, HealMedicalItem.Kind.FIELD_BANDAGE);
    public static final RegistryObject<Item> ANALGETICS =
            heal("analgetics", 45, HealMedicalItem.Kind.ANALGETICS);
    public static final RegistryObject<Item> PAINKILLERS =
            heal("painkillers", 40, HealMedicalItem.Kind.PAINKILLERS);
    public static final RegistryObject<Item> MORPHINE =
            heal("morphine", 75, HealMedicalItem.Kind.MORPHINE);
    public static final RegistryObject<Item> ADRENALINE =
            heal("adrenaline", 75, HealMedicalItem.Kind.ADRENALINE);
    public static final RegistryObject<Item> STEROIDS =
            heal("steroids", 75, HealMedicalItem.Kind.STEROIDS);

    // —— 手雷（金属材质 × 种类；铁保留 legacy id） ——
    public static final Map<String, RegistryObject<Item>> GRENADES = new LinkedHashMap<>();

    static {
        for (GrenadeMaterial material :
                List.of(
                        GrenadeMaterial.ironDefaults(),
                        GrenadeMaterial.goldDefaults(),
                        GrenadeMaterial.netheriteDefaults())) {
            for (GrenadeKind kind : GrenadeKind.values()) {
                registerGrenade(kind.itemId(material.id()), GrenadeStats.of(material, kind));
                if ("iron".equals(material.id())) {
                    registerGrenade(kind.legacyItemId(), GrenadeStats.of(material, kind));
                }
            }
        }
    }

    public static final RegistryObject<Item> GRENADE = GRENADES.get("grenade");
    public static final RegistryObject<Item> MASSIVE_GRENADE = GRENADES.get("massive_grenade");
    public static final RegistryObject<Item> IMPACT_GRENADE = GRENADES.get("impact_grenade");

    public static final RegistryObject<Item> COOKING_OIL =
            ITEMS.register("cooking_oil", () -> new Item(new Item.Properties().stacksTo(16)));

    // —— 特色食物 ——
    public static final RegistryObject<Item> BACON_BURGER =
            ITEMS.register("bacon_burger", () -> GunsRpgFoodItem.of(ModFoods.BACON_BURGER));
    public static final RegistryObject<Item> FISH_AND_CHIPS =
            ITEMS.register("fish_and_chips", () -> GunsRpgFoodItem.of(ModFoods.FISH_AND_CHIPS));
    public static final RegistryObject<Item> GARDEN_SOUP =
            ITEMS.register("garden_soup", () -> GunsRpgFoodItem.of(ModFoods.GARDEN_SOUP));
    public static final RegistryObject<Item> CHICKEN_DINNER =
            ITEMS.register("chicken_dinner", () -> GunsRpgFoodItem.of(ModFoods.CHICKEN_DINNER));
    public static final RegistryObject<Item> DELUXE_MEAL =
            ITEMS.register("deluxe_meal", () -> GunsRpgFoodItem.ofHeal(ModFoods.DELUXE_MEAL, 3));
    public static final RegistryObject<Item> MEATY_STEW_XXL =
            ITEMS.register("meaty_stew_xxl", () -> GunsRpgFoodItem.ofHeal(ModFoods.MEATY_STEW_XXL, 5));
    public static final RegistryObject<Item> RABBIT_CREAMY_SOUP =
            ITEMS.register("rabbit_creamy_soup", () -> GunsRpgFoodItem.ofHeal(ModFoods.RABBIT_CREAMY_SOUP, 3));
    public static final RegistryObject<Item> SHEPHERDS_PIE =
            ITEMS.register("shepherds_pie", () -> GunsRpgFoodItem.ofHeal(ModFoods.SHEPHERDS_PIE, 4));
    public static final RegistryObject<Item> FRUIT_SALAD =
            ITEMS.register("fruit_salad", () -> GunsRpgFoodItem.ofHeal(ModFoods.FRUIT_SALAD, 2));
    public static final RegistryObject<Item> EGG_SALAD =
            ITEMS.register("egg_salad", () -> GunsRpgFoodItem.of(ModFoods.EGG_SALAD));
    public static final RegistryObject<Item> CHOCOLATE_GLAZED_APPLE_PIE =
            ITEMS.register(
                    "chocolate_glazed_apple_pie",
                    () -> GunsRpgFoodItem.ofHeal(ModFoods.CHOCOLATE_GLAZED_APPLE_PIE, 3));
    public static final RegistryObject<Item> FRIED_EGG =
            ITEMS.register("fried_egg", () -> GunsRpgFoodItem.of(ModFoods.FRIED_EGG));
    public static final RegistryObject<Item> FRIES =
            ITEMS.register("fries", () -> GunsRpgFoodItem.of(ModFoods.FRIES));
    public static final RegistryObject<Item> CHICKEN_NUGGETS =
            ITEMS.register("chicken_nuggets", () -> GunsRpgFoodItem.ofHeal(ModFoods.CHICKEN_NUGGETS, 1));
    public static final RegistryObject<Item> SCHNITZEL =
            ITEMS.register("schnitzel", () -> GunsRpgFoodItem.of(ModFoods.SCHNITZEL));
    public static final RegistryObject<Item> RAW_DOUGHNUT =
            ITEMS.register("raw_doughnut", () -> GunsRpgFoodItem.of(ModFoods.RAW_DOUGHNUT));
    public static final RegistryObject<Item> DOUGHNUT =
            ITEMS.register("doughnut", () -> GunsRpgFoodItem.of(ModFoods.DOUGHNUT));
    public static final RegistryObject<Item> SUSHI_MAKI =
            ITEMS.register("sushi_maki", () -> GunsRpgFoodItem.of(ModFoods.SUSHI_MAKI));

    public static final RegistryObject<Item> WEAPON_REPAIR_KIT =
            ITEMS.register("weapon_repair_kit", () -> new Item(new Item.Properties().stacksTo(16).durability(8)));

    // —— 点数书 ——
    public static final RegistryObject<Item> SKILLPOINT_BOOK =
            ITEMS.register("skillpoint_book", () -> new PointAwardItem(
                    PointAwardItem.PointKind.SKILL, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> PERKPOINT_BOOK =
            ITEMS.register("perkpoint_book", () -> new PointAwardItem(
                    PointAwardItem.PointKind.PERK, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> WEAPON_BOOK =
            ITEMS.register("weapon_book", () -> new PointAwardItem(
                    PointAwardItem.PointKind.WEAPON, new Item.Properties().stacksTo(16)));

    // —— 血月特殊怪刷怪蛋（测试 / 创造） ——
    public static final RegistryObject<SpawnEggItem> BLOODMOON_GOLEM_SPAWN_EGG =
            ITEMS.register(
                    "bloodmoon_golem_spawn_egg",
                    () ->
                            new ForgeSpawnEggItem(
                                    ModEntities.BLOODMOON_GOLEM,
                                    0x5b5b5b,
                                    0xa30000,
                                    new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> ROCKET_ANGEL_SPAWN_EGG =
            ITEMS.register(
                    "rocket_angel_spawn_egg",
                    () ->
                            new ForgeSpawnEggItem(
                                    ModEntities.ROCKET_ANGEL,
                                    0x6669cc,
                                    0x16a3e8,
                                    new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> ZOMBIE_GUNNER_SPAWN_EGG =
            ITEMS.register(
                    "zombie_gunner_spawn_egg",
                    () ->
                            new ForgeSpawnEggItem(
                                    ModEntities.ZOMBIE_GUNNER,
                                    0x3f6f3f,
                                    0x4a4a4a,
                                    new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> EXPLOSIVE_SKELETON_SPAWN_EGG =
            ITEMS.register(
                    "explosive_skeleton_spawn_egg",
                    () ->
                            new ForgeSpawnEggItem(
                                    ModEntities.EXPLOSIVE_SKELETON,
                                    0xc00000,
                                    0xd7d7d7,
                                    new Item.Properties()));

    public static RegistryObject<Item> ammo(String id) {
        return AMMO.get(id);
    }

    public static List<Item> allGrenades() {
        return GRENADES.values().stream()
                .map(RegistryObject::get)
                .filter(item -> {
                    String path = ForgeRegistries.ITEMS.getKey(item).getPath();
                    return !path.equals("grenade")
                            && !path.equals("impact_grenade")
                            && !path.equals("massive_grenade");
                })
                .toList();
    }

    public static List<Item> allAmmoItems() {
        return AMMO.values().stream().map(RegistryObject::get).toList();
    }

    public static List<Item> allLauncherShells() {
        return List.of(
                GRENADE_LAUNCHER_SHELL.get(),
                IMPACT_GRENADE_LAUNCHER_SHELL.get(),
                HIGH_EXPLOSIVE_GRENADE_LAUNCHER_SHELL.get(),
                EXPLOSIVE_GRENADE_LAUNCHER_SHELL.get(),
                STICKY_GRENADE_LAUNCHER_SHELL.get(),
                TEAR_GAS_GRENADE_LAUNCHER_SHELL.get(),
                ROCKET_SHELL.get());
    }

    public static List<Item> ammoForCalibers(String... calibers) {
        return java.util.Arrays.stream(calibers)
                .flatMap(
                        caliber ->
                                AmmoMaterial.streamCraftable()
                                        .map(mat -> ammo(mat.itemPrefix() + "_" + caliber))
                                        .filter(ro -> ro != null && ro.isPresent())
                                        .map(RegistryObject::get))
                .toList();
    }

    /** 按 weaponKey 取已注册枪械物品（mob 配装用）。 */
    public static Item gunItem(String weaponKey) {
        if (weaponKey == null || weaponKey.isEmpty()) {
            return null;
        }
        Item item =
                ForgeRegistries.ITEMS.getValue(
                        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, weaponKey));
        return item instanceof FirearmItem ? item : null;
    }

    private static RegistryObject<Item> simple(String id) {
        return ITEMS.register(id, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> launcherShell(String id) {
        return ITEMS.register(id, () -> new LauncherShellItem(new Item.Properties().stacksTo(16)));
    }

    private static RegistryObject<FirearmItem> firearm(String id) {
        return ITEMS.register(id, () -> new FirearmItem(id, new Item.Properties().stacksTo(1)));
    }

    private static RegistryObject<Item> med(String id, int useTicks) {
        return ITEMS.register(id, () -> new MedicalUseItem(new Item.Properties().stacksTo(16), useTicks));
    }

    private static RegistryObject<Item> heal(String id, int useTicks, HealMedicalItem.Kind kind) {
        return ITEMS.register(id, () -> new HealMedicalItem(new Item.Properties().stacksTo(16), useTicks, kind));
    }

    public static RegistryObject<Item> grenade(String id) {
        RegistryObject<Item> ro = GRENADES.get(id);
        if (ro == null) {
            throw new IllegalArgumentException("Unknown grenade id: " + id);
        }
        return ro;
    }

    private static void registerGrenade(String id, GrenadeStats stats) {
        if (GRENADES.containsKey(id)) {
            return;
        }
        GRENADES.put(
                id,
                ITEMS.register(id, () -> new GrenadeItem(new Item.Properties().stacksTo(64), stats)));
    }

    private ModItems() {}
}
