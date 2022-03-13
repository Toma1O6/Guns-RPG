package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.network.AbstractHandlePacket;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_RequestBatteryChange extends AbstractHandlePacket<C2S_RequestBatteryChange> {

    public C2S_RequestBatteryChange() {}

    @Override
    public C2S_RequestBatteryChange recreate() {
        return new C2S_RequestBatteryChange();
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack held = player.getMainHandItem();
        if (held.getItem() == ModItems.STASH_DETECTOR) {
            ItemStack battery = ItemLocator.findFirst(player.inventory, StashDetectorItem::isValidBatterySource);
            if (held.getDamageValue() > 0 && !battery.isEmpty()) {
                held.setDamageValue(0);
                battery.shrink(1);
            }
        }
    }
}
