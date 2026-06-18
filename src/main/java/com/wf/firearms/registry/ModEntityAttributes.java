package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.entity.BloodmoonGolemEntity;
import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import com.wf.firearms.entity.RocketAngelEntity;
import com.wf.firearms.entity.ZombieGunnerEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityAttributes {
    private ModEntityAttributes() {}

    @SubscribeEvent
    public static void register(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BLOODMOON_GOLEM.get(), BloodmoonGolemEntity.createAttributes().build());
        event.put(ModEntities.ROCKET_ANGEL.get(), RocketAngelEntity.createAttributes().build());
        event.put(ModEntities.ZOMBIE_GUNNER.get(), ZombieGunnerEntity.createAttributes().build());
        event.put(ModEntities.EXPLOSIVE_SKELETON.get(), ExplosiveSkeletonEntity.createAttributes().build());
    }
}
