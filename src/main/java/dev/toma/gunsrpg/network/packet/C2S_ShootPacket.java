package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.PlayerShootProperties;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.AbstractHandlePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_ShootPacket extends AbstractHandlePacket<C2S_ShootPacket> {

    @Override
    public C2S_ShootPacket thisPacket() {
        return new C2S_ShootPacket();
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        if (item instanceof GunItem) {
            GunItem gun = (GunItem) item;
            gun.shoot(player.level, player, stack, new PlayerShootProperties(player, stack));
        }
    }
}
