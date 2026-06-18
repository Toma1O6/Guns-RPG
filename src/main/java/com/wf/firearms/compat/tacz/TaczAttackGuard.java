package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** TaCZ 整合包枪：左键只用于 TaCZ 开火，不触发原版近战攻击（卡弹时仍会误触实体）。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TaczAttackGuard {
    private TaczAttackGuard() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!TaczBackendConfig.useTaczShooting()) {
            return;
        }
        Player player = event.getEntity();
        ItemStack gun = player.getMainHandItem();
        if (TaczShootBlock.isPackTaczGun(gun)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (!TaczBackendConfig.useTaczShooting()) {
            return;
        }
        Player player = event.getEntity();
        ItemStack gun = player.getMainHandItem();
        if (TaczShootBlock.isPackTaczGun(gun)) {
            event.setCanceled(true);
        }
    }
}
