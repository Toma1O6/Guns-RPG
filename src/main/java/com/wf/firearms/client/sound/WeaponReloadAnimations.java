package com.wf.firearms.client.sound;

import com.wf.firearms.GunsRpg;
import net.minecraft.resources.ResourceLocation;

/** wf 武器 key → gunsrpg 动画 json 路径。 */
public final class WeaponReloadAnimations {
    private WeaponReloadAnimations() {}

    public static ResourceLocation clipPath(String weaponKey, ReloadSoundClip clip) {
        String folder = animationFolder(weaponKey);
        if (folder == null) {
            return null;
        }
        String file =
                switch (clip) {
                    case FULL_MAG -> fullMagFile(weaponKey);
                    case SHELL -> "load_bullet";
                    case UNJAM -> "unjam";
                };
        return ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "animation/" + folder + "/" + file + ".json");
    }

    private static String fullMagFile(String weaponKey) {
        if ("s686".equals(weaponKey)) {
            return "reload_both";
        }
        return "reload";
    }

    private static String animationFolder(String weaponKey) {
        return switch (weaponKey) {
            case "desert_eagle" -> "deagle";
            case "minigun" -> null;
            default -> weaponKey;
        };
    }
}
