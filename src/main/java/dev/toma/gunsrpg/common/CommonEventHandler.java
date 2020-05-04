package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onCapAttachP(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof EntityPlayer) {
            event.addCapability(GunsRPG.makeResource("playerdata"), new PlayerDataManager((EntityPlayer) entity));
        }
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerDataFactory.get(event.player).sync();
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingHurtEvent event) {
        if(event.getEntity() instanceof EntityPlayer) {
            PlayerData data = PlayerDataFactory.get((EntityPlayer) event.getEntity());
            data.getDebuffData().forEachDebuff(b -> b.onHurt(event.getSource(), (EntityPlayer) event.getEntity()));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            PlayerDataFactory.get(event.player).tick();
        }
    }
}
