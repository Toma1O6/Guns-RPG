package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {

    private static boolean lastAttackButtonState = false;

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;
        EntityPlayer player = mc.player;
        if (player == null) return;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof GunItem && settings.keyBindAttack.isPressed()) {
            CooldownTracker cooldownTracker = player.getCooldownTracker();
            if (!cooldownTracker.hasCooldown(stack.getItem())) {

            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;
            if (player != null) {
                GameSettings settings = mc.gameSettings;
            }
        }
    }
}
