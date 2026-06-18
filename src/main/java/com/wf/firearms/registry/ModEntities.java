package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.entity.AirdropEntity;
import com.wf.firearms.entity.BloodmoonGolemEntity;
import com.wf.firearms.entity.BulletProjectile;
import com.wf.firearms.entity.GrenadeEntity;
import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import com.wf.firearms.entity.LaunchedExplosiveEntity;
import com.wf.firearms.entity.RocketAngelEntity;
import com.wf.firearms.entity.ZombieGunnerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GunsRpg.MOD_ID);

    public static final RegistryObject<EntityType<BulletProjectile>> BULLET = ENTITIES.register(
            "bullet",
            () -> EntityType.Builder.<BulletProjectile>of(BulletProjectile::new, MobCategory.MISC)
                    .sized(0.15f, 0.15f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("bullet"));

    public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = ENTITIES.register(
            "airdrop",
            () -> EntityType.Builder.<AirdropEntity>of(AirdropEntity::new, MobCategory.MISC)
                    .sized(0.8f, 0.8f)
                    .clientTrackingRange(256)
                    .updateInterval(1)
                    .build("airdrop"));

    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE = ENTITIES.register(
            "grenade",
            () -> EntityType.Builder.<GrenadeEntity>of(GrenadeEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("grenade"));

    public static final RegistryObject<EntityType<BloodmoonGolemEntity>> BLOODMOON_GOLEM = ENTITIES.register(
            "bloodmoon_golem",
            () -> EntityType.Builder.<BloodmoonGolemEntity>of(BloodmoonGolemEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 2.7f)
                    .clientTrackingRange(80)
                    .updateInterval(3)
                    .build("bloodmoon_golem"));

    public static final RegistryObject<EntityType<RocketAngelEntity>> ROCKET_ANGEL = ENTITIES.register(
            "rocket_angel",
            () -> EntityType.Builder.<RocketAngelEntity>of(RocketAngelEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.6f)
                    .clientTrackingRange(128)
                    .updateInterval(3)
                    .build("rocket_angel"));

    public static final RegistryObject<EntityType<ZombieGunnerEntity>> ZOMBIE_GUNNER = ENTITIES.register(
            "zombie_gunner",
            () -> EntityType.Builder.<ZombieGunnerEntity>of(ZombieGunnerEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .clientTrackingRange(80)
                    .updateInterval(3)
                    .build("zombie_gunner"));

    public static final RegistryObject<EntityType<ExplosiveSkeletonEntity>> EXPLOSIVE_SKELETON = ENTITIES.register(
            "explosive_skeleton",
            () -> EntityType.Builder.<ExplosiveSkeletonEntity>of(ExplosiveSkeletonEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.99f)
                    .clientTrackingRange(80)
                    .updateInterval(3)
                    .build("explosive_skeleton"));

    public static final RegistryObject<EntityType<LaunchedExplosiveEntity>> LAUNCHED_EXPLOSIVE = ENTITIES.register(
            "launched_explosive",
            () -> EntityType.Builder.<LaunchedExplosiveEntity>of(LaunchedExplosiveEntity::new, MobCategory.MISC)
                    .sized(0.35f, 0.35f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("launched_explosive"));

    private ModEntities() {}
}
