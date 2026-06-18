package com.wf.firearms.client.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.WeaponAmmoDisplay;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczGunsrpgAmmoBridge;
import com.wf.firearms.compat.tacz.TaczGunStatSync;
import com.wf.firearms.compat.tacz.TaczPerkBridge;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 覆盖 TaCZ 枪械大 tooltip 中的弹药名/图标，改为 GunsRPG 材料弹口径，避免仍显示 .50 AE 等原版弹种。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TaczGunTooltipAmmoPatch {
    private static final String GUN_TOOLTIP = "com.tacz.guns.inventory.tooltip.GunTooltip";
    private static final String CLIENT_GUN_TOOLTIP = "com.tacz.guns.client.tooltip.ClientGunTooltip";

    private TaczGunTooltipAmmoPatch() {}

    @SubscribeEvent
    public static void registerTooltipFactory(RegisterClientTooltipComponentFactoriesEvent event) {
        if (!TaczCompat.isTaczLoaded() || !TaczBackendConfig.useTaczShooting()) {
            return;
        }
        try {
            @SuppressWarnings("unchecked")
            Class<? extends TooltipComponent> gunTooltipClass =
                    (Class<? extends TooltipComponent>) Class.forName(GUN_TOOLTIP);
            event.register(
                    gunTooltipClass,
                    tooltip -> {
                        ClientTooltipComponent base = createClientGunTooltip(tooltip);
                        patchIfNeeded(tooltip, base);
                        return base;
                    });
            GunsRpg.LOGGER.info("[gunsrpg] 已注册 TaCZ 枪械 tooltip 弹药展示覆盖");
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ tooltip 覆盖（缺少 TaCZ）");
        }
    }

    private static ClientTooltipComponent createClientGunTooltip(Object gunTooltip) {
        try {
            Class<?> clientClass = Class.forName(CLIENT_GUN_TOOLTIP);
            Constructor<?> ctor = clientClass.getConstructor(Class.forName(GUN_TOOLTIP));
            return (ClientTooltipComponent) ctor.newInstance(gunTooltip);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static void patchIfNeeded(Object gunTooltip, ClientTooltipComponent base) {
        try {
            ItemStack gun = readGun(gunTooltip);
            if (gun.isEmpty() || !WeaponAmmoDisplay.isPackWeapon(gun)) {
                return;
            }
            String key = WeaponAmmoDisplay.resolveWeaponKey(gun).orElse(null);
            if (key == null) {
                return;
            }
            FirearmSpec spec = FirearmRegistry.getOrDefault(key);
            ItemStack ammoIcon = WeaponAmmoDisplay.representativeAmmoStack(spec);
            Component ammoName = WeaponAmmoDisplay.materialAmmoTooltipLine(spec);
            if (ammoName.getString().isEmpty()) {
                return;
            }
            Class<?> clientClass = base.getClass();
            if (!ammoIcon.isEmpty()) {
                setField(clientClass, base, "ammo", ammoIcon);
            }
            setField(clientClass, base, "ammoName", ammoName);
            var player = Minecraft.getInstance().player;
            int currentMag = TaczBridge.getCurrentAmmoCount(gun);
            int maxMag = TaczPerkBridge.magazineCap(gun);
            if (maxMag <= 0) {
                maxMag = spec.magazineSize();
            }
            maxMag = Math.max(1, maxMag);
            int reserve = 0;
            if (player != null && TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
                reserve = TaczGunsrpgAmmoBridge.countGunsrpgRounds(player, gun, spec);
            }
            setField(
                    clientClass,
                    base,
                    "ammoCountText",
                    WeaponAmmoDisplay.taczAmmoCountLine(gun, spec, currentMag, maxMag, reserve));
            float displayDamage =
                    TaczGunStatSync.readDisplayDamage(
                            gun, spec, Minecraft.getInstance().player);
            setField(clientClass, base, "damage", TaczGunStatSync.damageTooltipLine(displayDamage));
            Font font = Minecraft.getInstance().font;
            int rowWidth = font.width(ammoName) + 22;
            Field maxWidthField = clientClass.getDeclaredField("maxWidth");
            maxWidthField.setAccessible(true);
            int current = maxWidthField.getInt(base);
            maxWidthField.setInt(base, Math.max(current, rowWidth));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] TaCZ tooltip 弹药覆盖失败: {}", ex.toString());
        }
    }

    private static ItemStack readGun(Object gunTooltip) throws ReflectiveOperationException {
        Object stack =
                gunTooltip.getClass().getMethod("getGun").invoke(gunTooltip);
        if (stack instanceof ItemStack itemStack) {
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    private static void setField(Class<?> clazz, Object instance, String name, Object value)
            throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(instance, value);
    }
}
