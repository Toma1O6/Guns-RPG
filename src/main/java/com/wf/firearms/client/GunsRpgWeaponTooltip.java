package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.combat.WeaponAmmoDisplay;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczGunStatSync;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** TaCZ 枪物品 tooltip：磨损与材料弹提示。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GunsRpgWeaponTooltip {
    private GunsRpgWeaponTooltip() {}

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!TaczBackendConfig.useTaczShooting()
                || !TaczCompat.isTaczLoaded()
                || !TaczBridge.isTaczGun(stack)
                || !WeaponAmmoDisplay.isPackWeapon(stack)) {
            return;
        }
        WeaponAmmoDisplay.resolveWeaponKey(stack)
                .ifPresent(
                        key -> {
                            FirearmStackState.appendWearTooltip(event.getToolTip(), stack, key);
                            var spec = FirearmRegistry.getOrDefault(key);
                            event.getToolTip().add(WeaponAmmoDisplay.inventoryHintLine(spec));
                            event.getToolTip().add(
                                    TaczGunStatSync.damageTooltipLine(
                                            TaczGunStatSync.readDisplayDamage(
                                                    stack, spec, Minecraft.getInstance().player)));
                        });
    }
}
