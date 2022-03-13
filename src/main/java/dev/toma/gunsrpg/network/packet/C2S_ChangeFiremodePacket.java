package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.network.AbstractHandlePacket;
import dev.toma.gunsrpg.network.NetworkManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_ChangeFiremodePacket extends AbstractHandlePacket<C2S_ChangeFiremodePacket> {

    @Override
    public C2S_ChangeFiremodePacket recreate() {
        return new C2S_ChangeFiremodePacket();
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
                NetworkManager.sendClientPacket(player, new S2C_AnimationPacket(S2C_AnimationPacket.Action.PLAY, ModAnimations.FIREMODE));
            }
        }
    }
}
