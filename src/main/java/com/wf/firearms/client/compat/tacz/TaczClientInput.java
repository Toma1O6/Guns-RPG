package com.wf.firearms.client.compat.tacz;

import com.wf.firearms.client.KeyBindings;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.WeaponAmmoDisplay;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczGunsrpgAmmoBridge;
import com.wf.firearms.compat.tacz.TaczShootBlock;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.gameplay.WeaponUseGate;
import com.wf.firearms.network.FirearmActionPacket;
import com.wf.firearms.network.ModNetwork;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;

/**
 * TaCZ 整合包枪：须在 TaCZ {@code ShootKey} 之前写入 {@code GunFireMode=AUTO}，否则只能点射。
 */
@Mod.EventBusSubscriber(modid = com.wf.firearms.GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class TaczClientInput {
    private TaczClientInput() {}

    /** 高于 TaCZ ShootKey（NORMAL），确保本 tick 开火检测前已同步射击模式。 */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void syncBeforeTaCzShoot(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !TaczBackendConfig.useTaczShooting()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) {
            return;
        }
        ItemStack gun = mc.player.getMainHandItem();
        if (!TaczBridge.isTaczGun(gun) || !WeaponAmmoDisplay.isPackWeapon(gun)) {
            return;
        }
        String weaponKey = WeaponAmmoDisplay.resolveWeaponKey(gun).orElse(null);
        if (weaponKey == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        TaczBridge.applyFireModeFromSpec(gun, spec);
        if (TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
            TaczGunsrpgAmmoBridge.syncDummyPool(mc.player, gun, weaponKey);
        }
        TaczBridge.setAttachmentLock(gun, !WeaponUseGate.hasAssemblyUnlocked(mc.player, weaponKey));

        if (TaczShootBlock.shouldBlockShoot(mc.player, gun)) {
            while (mc.options.keyAttack.consumeClick()) {}
        }

        while (KeyBindings.UNJAM.consumeClick()) {
            ModNetwork.sendFirearmAction(new FirearmActionPacket(FirearmActionPacket.Action.UNJAM, false));
        }
    }

    /** 改装被锁时提示玩家去技能树解锁装配（TaCZ RefitKey 本身静默忽略）。 */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRefitKeyHint(InputEvent.Key event) {
        if (!TaczBackendConfig.useTaczShooting() || event.getAction() != GLFW.GLFW_PRESS) {
            return;
        }
        if (!refitKeyMatches(event)) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null || mc.player.isSpectator()) {
            return;
        }
        ItemStack gun = mc.player.getMainHandItem();
        if (!TaczBridge.isTaczGun(gun) || !WeaponAmmoDisplay.isPackWeapon(gun)) {
            return;
        }
        String weaponKey = WeaponAmmoDisplay.resolveWeaponKey(gun).orElse(null);
        if (weaponKey == null || WeaponUseGate.hasAssemblyUnlocked(mc.player, weaponKey)) {
            return;
        }
        mc.player.displayClientMessage(
                Component.translatable(
                        "gunsrpg.tacz.refit.need_assembly",
                        WeaponMapping.displayNameForWeaponKey(weaponKey)),
                true);
    }

    /** 卡弹/排障时拦截原版左键攻击，避免对训练假人等目标刷微量伤/回血数字。 */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void blockVanillaAttack(InputEvent.InteractionKeyMappingTriggered event) {
        if (!TaczBackendConfig.useTaczShooting() || !event.isAttack()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) {
            return;
        }
        ItemStack gun = mc.player.getMainHandItem();
        if (TaczShootBlock.isPackTaczGun(gun) && TaczShootBlock.shouldBlockShoot(mc.player, gun)) {
            event.setCanceled(true);
        }
    }

    public static void tick(Minecraft mc) {
        if (!TaczBackendConfig.useTaczShooting() || mc.player == null || mc.screen != null) {
            return;
        }
        ItemStack gun = mc.player.getMainHandItem();
        if (!TaczBridge.isTaczGun(gun) || !WeaponAmmoDisplay.isPackWeapon(gun)) {
            return;
        }
        String weaponKey = WeaponAmmoDisplay.resolveWeaponKey(gun).orElse(null);
        if (weaponKey == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);

        while (KeyBindings.FIREMODE.consumeClick()) {
            if (!spec.supportsFireModeSwitch()) {
                continue;
            }
            ModNetwork.sendFirearmAction(new FirearmActionPacket(FirearmActionPacket.Action.FIREMODE, false));
        }
    }

    private static boolean refitKeyMatches(InputEvent.Key event) {
        KeyMapping mapping = taczRefitKeyMapping();
        if (mapping != null) {
            return mapping.matches(event.getKey(), event.getScanCode());
        }
        return event.getKey() == GLFW.GLFW_KEY_Z;
    }

    private static KeyMapping taczRefitKeyMapping() {
        try {
            Class<?> refitClass = Class.forName("com.tacz.guns.client.input.RefitKey");
            Field field = refitClass.getField("REFIT_KEY");
            Object value = field.get(null);
            if (value instanceof KeyMapping mapping) {
                return mapping;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }
}
