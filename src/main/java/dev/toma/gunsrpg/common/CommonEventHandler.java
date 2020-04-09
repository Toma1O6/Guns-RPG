package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerDataManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEventHandler {

    @SubscribeEvent
    public static void onCapAttachP(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof EntityPlayer) {
            event.addCapability(GunsRPG.makeResource("playerdata"), new PlayerDataManager((EntityPlayer) entity));
        }
    }
}
