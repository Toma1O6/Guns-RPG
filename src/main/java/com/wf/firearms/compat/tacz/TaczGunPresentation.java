package com.wf.firearms.compat.tacz;

import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.FirearmRegistry;
import net.minecraft.world.item.ItemStack;

/** TaCZ 枪在整合包内的展示：tooltip 来源、隐藏枪包信息等。 */
public final class TaczGunPresentation {
    /** {@link com.tacz.guns.item.GunTooltipPart#PACK_INFO} */
    private static final int HIDE_PACK_INFO = 1 << 5;
    /** {@link com.tacz.guns.item.GunTooltipPart#UPGRADES_TIP} */
    private static final int HIDE_UPGRADES_TIP = 1 << 4;

    private TaczGunPresentation() {}

    public static void applyToPackGun(ItemStack gun, String weaponKey) {
        if (gun == null || gun.isEmpty() || weaponKey == null) {
            return;
        }
        hideTooltipParts(gun, HIDE_PACK_INFO | HIDE_UPGRADES_TIP);
        FirearmRegistry.get(weaponKey).ifPresent(spec -> TaczBridge.applyFireModeFromSpec(gun, spec));
    }

    private static void hideTooltipParts(ItemStack gun, int extraMask) {
        if (!TaczCompat.isTaczLoaded()) {
            return;
        }
        try {
            Class<?> partClass = Class.forName("com.tacz.guns.item.GunTooltipPart");
            int current =
                    (int)
                            partClass
                                    .getMethod("getHideFlags", ItemStack.class)
                                    .invoke(null, gun);
            partClass
                    .getMethod("setHideFlags", ItemStack.class, int.class)
                    .invoke(null, gun, current | extraMask);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
