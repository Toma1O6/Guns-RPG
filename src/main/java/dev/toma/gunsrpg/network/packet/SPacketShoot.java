package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.AbstractHandlePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketShoot extends AbstractHandlePacket<SPacketShoot> {

    @Override
    public SPacketShoot thisPacket() {
        return new SPacketShoot();
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        if (item instanceof GunItem) {
            GunItem gun = (GunItem) item;
            LazyOptional<IPlayerData> optional = PlayerData.get(player);
            optional.ifPresent(data -> {
                boolean aiming = data.getAimInfo().isAiming();
                gun.shoot(player.level, player, stack, () -> aiming ? 0.0F : 0.25F);
            });
        }
    }
}
