package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.entity.projectile.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class ModEntities {

    private static final DeferredRegister<EntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, GunsRPG.MODID);

    public static final RegistryObject<EntityType<ZombieGunnerEntity>> ZOMBIE_GUNNER = register("zombie_gunner", ZombieGunnerEntity::new, EntityClassification.MONSTER,
            builder -> builder.setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).sized(0.6F, 1.99F)
    );
    public static final RegistryObject<EntityType<ExplosiveSkeletonEntity>> EXPLOSIVE_SKELETON = register("explosive_skeleton", ExplosiveSkeletonEntity::new, EntityClassification.MONSTER,
            builder -> builder.setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).sized(0.6F, 1.99F)
    );
    public static final RegistryObject<EntityType<BloodmoonGolemEntity>> BLOODMOON_GOLEM = register("bloodmoon_golem", BloodmoonGolemEntity::new, EntityClassification.MONSTER,
            builder -> builder.setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).sized(1.4F, 2.7F)
    );
    public static final RegistryObject<EntityType<RocketAngelEntity>> ROCKET_ANGEL = register("rocket_angel", RocketAngelEntity::new, EntityClassification.MONSTER,
            builder -> builder.setTrackingRange(128).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).sized(0.8F, 1.6F)
    );
    public static final RegistryObject<EntityType<GoldDragonEntity>> GOLD_DRAGON = register("gold_dragon", GoldDragonEntity::new, EntityClassification.MONSTER,
            builder -> builder.setTrackingRange(160).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).fireImmune().sized(16.0F, 8.0F).clientTrackingRange(10)
    );
    public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = register("airdrop", AirdropEntity::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(1.0F, 1.0F)
    );
    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE = register("grenade", GrenadeEntity::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(64).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.2F, 0.2F)
    );
    public static final RegistryObject<EntityType<FlareEntity>> FLARE = register("flare", FlareEntity::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.2F, 0.2F)
    );
    public static final RegistryObject<EntityType<Bullet>> BULLET = register("bullet", Bullet::new, EntityClassification.MISC,
            builder -> builder.sized(0.001F, 0.001F).noSummon().noSave().setShouldReceiveVelocityUpdates(false)
    );
    public static final RegistryObject<EntityType<Pellet>> PELLET = register("pellet", Pellet::new, EntityClassification.MISC,
            builder -> builder.sized(0.001F, 0.001F).noSummon().noSave().setShouldReceiveVelocityUpdates(false)
    );
    public static final RegistryObject<EntityType<Bolt>> BOLT = register("bolt", Bolt::new, EntityClassification.MISC,
            builder -> builder.sized(0.01F, 0.01F).noSummon().noSave().setShouldReceiveVelocityUpdates(false)
    );
    public static final RegistryObject<EntityType<Pebble>> PEBBLE = register("pebble", Pebble::new, EntityClassification.MISC,
            builder -> builder.sized(0.1F, 0.1F).noSummon().noSave().clientTrackingRange(4).updateInterval(20)
    );
    public static final RegistryObject<EntityType<Grenade>> GRENADE_SHELL = register("grenade_shell", Grenade::new, EntityClassification.MISC,
            builder -> builder.sized(0.3F, 0.3F).noSummon().noSave().setShouldReceiveVelocityUpdates(false)
    );
    public static final RegistryObject<EntityType<Rocket>> ROCKET = register("rocket", Rocket::new, EntityClassification.MISC,
            builder -> builder.sized(0.4F, 0.4F).noSummon().noSave().setTrackingRange(256).setShouldReceiveVelocityUpdates(false)
    );
    public static final RegistryObject<EntityType<MayorEntity>> MAYOR = register("mayor", MayorEntity::new, EntityClassification.MISC,
            builder -> builder.sized(0.6F, 1.95F).clientTrackingRange(10)
    );
    public static final RegistryObject<EntityType<ZombieKnightEntity>> ZOMBIE_KNIGHT = register("zombie_knight", ZombieKnightEntity::new, EntityClassification.MONSTER,
            builder -> builder.sized(0.6F, 1.95F).clientTrackingRange(8)
    );

    private static <E extends Entity> RegistryObject<EntityType<E>> register(
            String name,
            EntityType.IFactory<E> factory,
            EntityClassification classification,
            Function<EntityType.Builder<E>, EntityType.Builder<E>> action
    ) {
        return TYPES.register(name, () -> action.apply(EntityType.Builder.of(factory, classification)).build(name));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
