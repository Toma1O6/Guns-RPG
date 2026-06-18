package com.wf.firearms.combat;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.registry.ModItems;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 枪械规格与伤害梯度。
 *
 * <p>设计原则：同族升级链上，后解锁枪应在单发威力或持续 DPS 上明显强于前一档（铁弹、无天赋、贴身）。
 * <ul>
 *   <li>手枪：M1911 → 格洛克 → 沙鹰 → 金牛座（毕业 Lv70）</li>
 *   <li>冲锋枪：UMP45 → Vector → 乌兹 → P90（毕业 Lv75）</li>
 *   <li>步枪：AKM → HK416 → 81-1 → AUG（毕业 Lv75）</li>
 *   <li>轻机枪：PKM → M249 → 加特林（毕业 Lv80）</li>
 *   <li>射手：FN FAL → SKS → SPR-15 → Mk14</li>
 *   <li>狙击：98K → M24 → AWM → M95</li>
 *   <li>霰弹：DB-2 → S1897 → SPAS-12 → S686</li>
 *   <li>轻机枪：加特林（AKM 技能树分支）</li>
 * </ul>
 */
public final class FirearmRegistry {
    private static final Map<String, FirearmSpec> BY_KEY = new HashMap<>();
    private static boolean initialized;

    private FirearmRegistry() {}

    public static void init() {
        if (initialized) {
            return;
        }
        BY_KEY.clear();

        // —— 手枪链 Lv5 → Lv15 → Lv40 → Lv70 ——
        register(FirearmSpec.builder("glock")
                .weaponClass(WeaponClass.PISTOL)
                .baseDamage(7.5f)
                .fireIntervalTicks(10)
                .automatic(false)
                .magazineSize(18)
                .reloadTicks(32)
                .unjamTicks(28)
                .jamChance(0.017f)
                .projectileSpeed(9f)
                .spread(0.02f)
                .ammo(ModItems.ammoForCalibers("9mm"))
                .build());

        register(FirearmSpec.builder("m1911")
                .weaponClass(WeaponClass.PISTOL)
                .baseDamage(5.5f)
                .fireIntervalTicks(11)
                .automatic(false)
                .magazineSize(7)
                .reloadTicks(35)
                .unjamTicks(30)
                .jamChance(0.018f)
                .projectileSpeed(9f)
                .spread(0.022f)
                .ammo(ModItems.ammoForCalibers("9mm"))
                .build());

        register(FirearmSpec.builder("r45")
                .weaponClass(WeaponClass.PISTOL)
                .baseDamage(50f)
                .fireIntervalTicks(7)
                .automatic(false)
                .magazineSize(5)
                .reloadTicks(42)
                .unjamTicks(34)
                .jamChance(0.016f)
                .projectileSpeed(10f)
                .spread(0.048f)
                .ammo(magnumAmmo())
                .build());

        register(FirearmSpec.builder("desert_eagle")
                .weaponClass(WeaponClass.PISTOL)
                .baseDamage(28f)
                .fireIntervalTicks(10)
                .automatic(false)
                .magazineSize(7)
                .reloadTicks(42)
                .unjamTicks(35)
                .jamChance(0.02f)
                .projectileSpeed(10f)
                .spread(0.052f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        // —— 冲锋枪链 Lv10 → Lv25 → Lv50 → Lv75 ——
        register(FirearmSpec.builder("ump45")
                .weaponClass(WeaponClass.SMG)
                .baseDamage(6.5f)
                .fireIntervalTicks(7)
                .automatic(true)
                .magazineSize(25)
                .reloadTicks(46)
                .unjamTicks(36)
                .jamChance(0.015f)
                .projectileSpeed(9f)
                .spread(0.058f)
                .ammo(ModItems.ammoForCalibers("45acp"))
                .build());

        register(FirearmSpec.builder("thompson")
                .weaponClass(WeaponClass.SMG)
                .baseDamage(12f)
                .fireIntervalTicks(5)
                .automatic(true)
                .magazineSize(30)
                .reloadTicks(48)
                .unjamTicks(38)
                .jamChance(0.014f)
                .projectileSpeed(9f)
                .spread(0.055f)
                .ammo(ModItems.ammoForCalibers("9mm"))
                .build());

        register(FirearmSpec.builder("vector")
                .weaponClass(WeaponClass.SMG)
                .baseDamage(9.5f)
                .fireIntervalTicks(4)
                .automatic(true)
                .magazineSize(25)
                .reloadTicks(45)
                .unjamTicks(36)
                .jamChance(0.013f)
                .projectileSpeed(9f)
                .spread(0.05f)
                .ammo(ModItems.ammoForCalibers("9mm"))
                .build());

        register(FirearmSpec.builder("uzi")
                .weaponClass(WeaponClass.SMG)
                .baseDamage(13f)
                .fireIntervalTicks(4)
                .automatic(true)
                .magazineSize(20)
                .reloadTicks(44)
                .unjamTicks(34)
                .jamChance(0.014f)
                .projectileSpeed(9f)
                .spread(0.06f)
                .ammo(ModItems.ammoForCalibers("9mm"))
                .build());

        register(FirearmSpec.builder("p90")
                .weaponClass(WeaponClass.SMG)
                .baseDamage(18f)
                .fireIntervalTicks(3)
                .automatic(true)
                .magazineSize(50)
                .reloadTicks(50)
                .unjamTicks(38)
                .jamChance(0.013f)
                .projectileSpeed(10f)
                .spread(0.055f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        // —— 步枪链 Lv25 → Lv45 → Lv60 → Lv75 ——
        register(FirearmSpec.builder("akm")
                .weaponClass(WeaponClass.RIFLE)
                .baseDamage(10.5f)
                .fireIntervalTicks(5)
                .automatic(true)
                .magazineSize(30)
                .reloadTicks(55)
                .unjamTicks(40)
                .jamChance(0.012f)
                .projectileSpeed(11f)
                .spread(0.038f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        register(FirearmSpec.builder("hk416")
                .weaponClass(WeaponClass.RIFLE)
                .baseDamage(13f)
                .fireIntervalTicks(4)
                .automatic(true)
                .magazineSize(30)
                .reloadTicks(52)
                .unjamTicks(38)
                .jamChance(0.011f)
                .projectileSpeed(11.5f)
                .spread(0.034f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        register(FirearmSpec.builder("type_81")
                .weaponClass(WeaponClass.RIFLE)
                .baseDamage(16f)
                .fireIntervalTicks(5)
                .automatic(true)
                .magazineSize(30)
                .reloadTicks(54)
                .unjamTicks(40)
                .jamChance(0.012f)
                .projectileSpeed(11f)
                .spread(0.036f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        register(FirearmSpec.builder("aug")
                .weaponClass(WeaponClass.RIFLE)
                .baseDamage(20f)
                .fireIntervalTicks(4)
                .automatic(true)
                .magazineSize(30)
                .reloadTicks(50)
                .unjamTicks(36)
                .jamChance(0.01f)
                .projectileSpeed(11.5f)
                .spread(0.032f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        // —— 射手步枪链 Lv20 → Lv25 → Lv50 → Lv75 ——
        register(FirearmSpec.builder("fn_fal")
                .weaponClass(WeaponClass.DMR)
                .baseDamage(15f)
                .fireIntervalTicks(7)
                .automatic(false)
                .magazineSize(20)
                .reloadTicks(50)
                .unjamTicks(34)
                .jamChance(0.011f)
                .projectileSpeed(11f)
                .spread(0.027f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        register(FirearmSpec.builder("sks")
                .weaponClass(WeaponClass.DMR)
                .baseDamage(17f)
                .fireIntervalTicks(7)
                .automatic(false)
                .magazineSize(10)
                .reloadTicks(48)
                .unjamTicks(34)
                .jamChance(0.011f)
                .projectileSpeed(11f)
                .spread(0.026f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        register(FirearmSpec.builder("spr15")
                .weaponClass(WeaponClass.DMR)
                .baseDamage(22f)
                .fireIntervalTicks(6)
                .automatic(false)
                .magazineSize(16)
                .reloadTicks(46)
                .unjamTicks(32)
                .jamChance(0.01f)
                .projectileSpeed(11.5f)
                .spread(0.024f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        register(FirearmSpec.builder("mk14ebr")
                .weaponClass(WeaponClass.DMR)
                .baseDamage(30f)
                .fireIntervalTicks(6)
                .automatic(false)
                .fireModeSwitch(true)
                .magazineSize(20)
                .reloadTicks(52)
                .unjamTicks(34)
                .jamChance(0.01f)
                .projectileSpeed(12f)
                .spread(0.036f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        // —— 狙击链 Lv30 → Lv40 → Lv55 → Lv100 ——
        register(FirearmSpec.builder("kar98k")
                .weaponClass(WeaponClass.SNIPER)
                .baseDamage(56f)
                .fireIntervalTicks(20)
                .automatic(false)
                .magazineSize(5)
                .reloadTicks(62)
                .unjamTicks(44)
                .jamChance(0.01f)
                .projectileSpeed(13.5f)
                .spread(0.012f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        register(FirearmSpec.builder("winchester")
                .weaponClass(WeaponClass.SNIPER)
                .baseDamage(72f)
                .fireIntervalTicks(18)
                .automatic(false)
                .magazineSize(6)
                .reloadTicks(58)
                .unjamTicks(42)
                .jamChance(0.01f)
                .projectileSpeed(14f)
                .spread(0.011f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        register(FirearmSpec.builder("awm")
                .weaponClass(WeaponClass.SNIPER)
                .baseDamage(105f)
                .fireIntervalTicks(22)
                .automatic(false)
                .magazineSize(6)
                .reloadTicks(70)
                .unjamTicks(46)
                .jamChance(0.008f)
                .projectileSpeed(15.5f)
                .spread(0.009f)
                .ammo(magnumAmmo())
                .build());

        register(FirearmSpec.builder("m95")
                .weaponClass(WeaponClass.SNIPER)
                .baseDamage(175f)
                .fireIntervalTicks(24)
                .automatic(false)
                .magazineSize(6)
                .reloadTicks(78)
                .unjamTicks(50)
                .jamChance(0.007f)
                .projectileSpeed(17f)
                .spread(0.007f)
                .ammo(magnumAmmo())
                .build());

        // —— 霰弹链 Lv15 → Lv20 → Lv45 → Lv75 ——
        register(FirearmSpec.builder("db2")
                .weaponClass(WeaponClass.SHOTGUN)
                .baseDamage(1.8f)
                .fireIntervalTicks(16)
                .automatic(false)
                .magazineSize(2)
                .reloadStyle(ReloadStyle.FULL_MAG)
                .reloadTicks(28)
                .unjamTicks(65)
                .jamChance(0.014f)
                .projectileSpeed(9f)
                .spread(FirearmSpread.shotgun() * 1.05f)
                .pelletCount(8)
                .ammo(shotgunAmmo())
                .build());

        register(FirearmSpec.builder("s1897")
                .weaponClass(WeaponClass.SHOTGUN)
                .baseDamage(4.2f)
                .fireIntervalTicks(24)
                .automatic(false)
                .magazineSize(6)
                .reloadStyle(ReloadStyle.SHELL_BY_SHELL)
                .reloadPrepTicks(28)
                .shellReloadTicks(20)
                .reloadTicks(20)
                .unjamTicks(68)
                .jamChance(0.011f)
                .projectileSpeed(8.5f)
                .spread(FirearmSpread.shotgun())
                .pelletCount(7)
                .ammo(shotgunAmmo())
                .build());

        register(FirearmSpec.builder("s12k")
                .weaponClass(WeaponClass.SHOTGUN)
                .baseDamage(6.5f)
                .fireIntervalTicks(11)
                .automatic(false)
                .magazineSize(6)
                .reloadStyle(ReloadStyle.FULL_MAG)
                .reloadTicks(52)
                .unjamTicks(65)
                .jamChance(0.009f)
                .projectileSpeed(9f)
                .spread(FirearmSpread.shotgun() * 0.95f)
                .pelletCount(8)
                .ammo(shotgunAmmo())
                .build());

        register(FirearmSpec.builder("s686")
                .weaponClass(WeaponClass.SHOTGUN)
                .baseDamage(9f)
                .fireIntervalTicks(9)
                .automatic(false)
                .magazineSize(2)
                .reloadStyle(ReloadStyle.FULL_MAG)
                .reloadTicks(22)
                .unjamTicks(62)
                .jamChance(0.01f)
                .projectileSpeed(10f)
                .spread(FirearmSpread.shotgun() * 0.88f)
                .pelletCount(9)
                .ammo(shotgunAmmo())
                .build());

        // —— 轻机枪线 Lv30 → Lv55 → Lv80 ——
        register(FirearmSpec.builder("pkm")
                .weaponClass(WeaponClass.HEAVY)
                .baseDamage(11f)
                .fireIntervalTicks(3)
                .automatic(true)
                .magazineSize(40)
                .reloadTicks(85)
                .unjamTicks(45)
                .jamChance(0.016f)
                .projectileSpeed(10f)
                .spread(0.06f)
                .ammo(ModItems.ammoForCalibers("762mm"))
                .build());

        register(FirearmSpec.builder("m249")
                .weaponClass(WeaponClass.HEAVY)
                .baseDamage(12f)
                .fireIntervalTicks(2)
                .automatic(true)
                .magazineSize(75)
                .reloadTicks(95)
                .unjamTicks(50)
                .jamChance(0.018f)
                .projectileSpeed(10f)
                .spread(0.068f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        register(FirearmSpec.builder("gatling")
                .weaponClass(WeaponClass.HEAVY)
                .baseDamage(10f)
                .fireIntervalTicks(1)
                .automatic(true)
                .magazineSize(200)
                .reloadTicks(110)
                .unjamTicks(55)
                .jamChance(0.022f)
                .projectileSpeed(11f)
                .spread(0.075f)
                .ammo(ModItems.ammoForCalibers("556mm"))
                .build());

        register(FirearmSpec.builder("grenade_launcher")
                .weaponClass(WeaponClass.LAUNCHER)
                .baseDamage(24f)
                .fireIntervalTicks(22)
                .magazineSize(6)
                .reloadTicks(55)
                .unjamTicks(40)
                .jamChance(0.008f)
                .projectileSpeed(1.15f)
                .spread(0.035f)
                .ammo(launcherShellAmmo())
                .build());

        register(FirearmSpec.builder("rocket_launcher")
                .weaponClass(WeaponClass.LAUNCHER)
                .baseDamage(52f)
                .fireIntervalTicks(32)
                .magazineSize(1)
                .reloadTicks(68)
                .unjamTicks(45)
                .jamChance(0.006f)
                .projectileSpeed(2.0f)
                .spread(0.02f)
                .ammo(List.of(ModItems.ROCKET_SHELL.get()))
                .build());

        initialized = true;
        GunsRpg.LOGGER.info("[gunsrpg] 已注册 {} 种枪械规格", BY_KEY.size());
    }

    private static List<Item> pistolAmmo() {
        return ModItems.ammoForCalibers("9mm", "45acp");
    }

    private static List<Item> rifleAmmo() {
        return ModItems.ammoForCalibers("556mm", "762mm");
    }

    private static List<Item> magnumAmmo() {
        return ModItems.ammoForCalibers("magnum");
    }

    private static List<Item> shotgunAmmo() {
        var ammo = new ArrayList<>(ModItems.ammoForCalibers("12g"));
        ammo.add(ModItems.SHOTGUN_SHELL.get());
        return ammo;
    }

    private static List<Item> allBallAmmo() {
        var ammo = new ArrayList<>(ModItems.allAmmoItems());
        ammo.add(ModItems.SHOTGUN_SHELL.get());
        return ammo;
    }

    private static List<Item> launcherShellAmmo() {
        return ModItems.allLauncherShells();
    }

    public static void register(FirearmSpec spec) {
        BY_KEY.put(spec.weaponKey(), spec);
    }

    public static Optional<FirearmSpec> get(String weaponKey) {
        return Optional.ofNullable(BY_KEY.get(weaponKey));
    }

    public static FirearmSpec getOrDefault(String weaponKey) {
        return BY_KEY.getOrDefault(weaponKey, BY_KEY.get("m1911"));
    }

    public static WeaponClass classForWeaponKey(String weaponKey) {
        return get(weaponKey).map(FirearmSpec::weaponClass).orElse(WeaponClass.OTHER);
    }
}
