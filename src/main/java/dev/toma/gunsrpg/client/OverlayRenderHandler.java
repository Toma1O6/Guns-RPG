package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OverlayRenderHandler {

    @SubscribeEvent
    public static void disableOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            MainWindow window = event.getWindow();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GunItem) {
                event.setCanceled(true);

            }
        }
    }
}
