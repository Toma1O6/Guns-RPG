package com.wf.firearms.launcher;

import net.minecraft.world.item.Item;

/** 发射器弹药（榴弹/火箭）爆炸参数。 */
public record LauncherShellStats(
        float blastRadius,
        float explosionDamage,
        boolean explodeOnImpact,
        int fuseTicks,
        boolean rocket,
        boolean incendiary,
        boolean toxic) {

    public static LauncherShellStats forItem(Item item) {
        if (item == null) {
            return standardGrenade();
        }
        String id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(item).getPath();
        return switch (id) {
            case "impact_grenade_launcher_shell" ->
                    new LauncherShellStats(2.8f, 22f, true, 0, false, false, false);
            case "high_explosive_grenade_launcher_shell" ->
                    new LauncherShellStats(3.6f, 38f, false, 50, false, false, false);
            case "explosive_grenade_launcher_shell" ->
                    new LauncherShellStats(3.2f, 32f, false, 45, false, false, false);
            case "sticky_grenade_launcher_shell" ->
                    new LauncherShellStats(3.0f, 26f, false, 80, false, false, false);
            case "tear_gas_grenade_launcher_shell" ->
                    new LauncherShellStats(3.5f, 6f, false, 35, false, false, true);
            case "rocket_shell", "rocket_launcher_shell" ->
                    new LauncherShellStats(4.2f, 52f, true, 0, true, false, false);
            case "incendiary_rocket_shell" ->
                    new LauncherShellStats(3.8f, 40f, true, 0, true, true, false);
            default -> standardGrenade();
        };
    }

    public static LauncherShellStats standardGrenade() {
        return new LauncherShellStats(3.0f, 24f, false, 50, false, false, false);
    }
}
