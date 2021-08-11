package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.network.AbstractHandlePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketChangeFiremode extends AbstractHandlePacket<SPacketChangeFiremode> {

    @Override
    public SPacketChangeFiremode thisPacket() {
        return new SPacketChangeFiremode();
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) stack.getItem();
            if (gun.switchFiremode(stack, player)) {
                Firemode currentMode = gun.getFiremode(stack);
                player.sendMessage(new StringTextComponent("Firemode: " + currentMode.getName()), ChatType.GAME_INFO, Util.NIL_UUID);
                // TODO firemode animation
                //NetworkManager.sendClientPacket(player, new CPacketSendAnimation(Animations.FIREMODE));
            }
        }
    }
}
