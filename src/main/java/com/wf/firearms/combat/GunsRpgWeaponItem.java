package com.wf.firearms.combat;

import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczWeaponBinding;
import com.wf.firearms.compat.tacz.TaczWeaponCatalog;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

/** 判断物品是否为本包 wf 枪械（含 TaCZ 映射枪）。 */
public final class GunsRpgWeaponItem {
    private GunsRpgWeaponItem() {}

    public static Optional<String> weaponKey(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        if (stack.getItem() instanceof FirearmItem firearm) {
            return Optional.of(firearm.getWeaponKey());
        }
        if (TaczBackendConfig.useTaczShooting()) {
            Optional<String> tacz =
                    TaczWeaponBinding.read(stack)
                            .or(() -> TaczBridge.readGunId(stack).flatMap(TaczWeaponCatalog::weaponKeyForGunId));
            if (tacz.isPresent() && TaczBackendConfig.weaponMap().containsKey(tacz.get())) {
                return tacz;
            }
        }
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id != null) {
            return WeaponMapping.weaponKeyForItem(id.toString());
        }
        return Optional.empty();
    }

    public static boolean isGunsRpgWeapon(ItemStack stack) {
        return weaponKey(stack).isPresent();
    }
}
