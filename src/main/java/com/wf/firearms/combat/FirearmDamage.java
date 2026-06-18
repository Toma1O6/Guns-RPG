package com.wf.firearms.combat;

import com.wf.firearms.config.CombatConfig;
import com.wf.firearms.gameplay.PerkEffectService;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** 单发弹丸伤害 = 枪身本体 × 弹药材质 × 天赋；命中时再乘距离衰减。 */
public final class FirearmDamage {
    private FirearmDamage() {}

    public static float pelletDamage(Player player, ItemStack gun, FirearmSpec spec) {
        AmmoMaterial mat = FirearmStackState.getLoadedMaterial(gun);
        AmmoCaliber caliber = FirearmStackState.getLoadedCaliber(gun);
        double extMult = com.wf.firearms.gameplay.WeaponExtensionService.damageMultiplier(player, spec.weaponKey());
        float dmg = (float)
                (spec.baseDamage()
                        * AmmoStatsRegistry.damageMultiplier(mat, caliber)
                        * PerkEffectService.firearmDamageMultiplier(player, spec.weaponClass())
                        * extMult)
                + com.wf.firearms.gameplay.WeaponExtensionService.pelletDamageBonus(player, spec.weaponKey());
        if (com.wf.firearms.gameplay.WeaponExtensionService.everyBulletCounts(player, spec.weaponKey())
                && FirearmStackState.getAmmo(gun) <= 1) {
            dmg *= 3.0f;
        }
        return dmg;
    }

    public static float withDistanceFalloff(
            float damage, String weaponKey, WeaponClass weaponClass, double distanceBlocks) {
        String curve = CombatConfig.curveForWeapon(weaponKey, weaponClass);
        float mult = CombatConfig.falloffMultiplier(curve, distanceBlocks);
        return Math.max(0.5f, damage * mult);
    }
}
