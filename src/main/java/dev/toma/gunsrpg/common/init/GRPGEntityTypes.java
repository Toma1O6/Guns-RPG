package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class GRPGEntityTypes {

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
            builder -> builder.setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).sized(0.8F, 1.6F)
    );
    public static final RegistryObject<EntityType<EntityGoldDragon>> GOLD_DRAGON = register("gold_dragon", EntityGoldDragon::new, EntityClassification.MONSTER,
            builder -> builder.setTrackingRange(160).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).fireImmune().sized(16.0F, 8.0F).clientTrackingRange(10)
    );
    public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = register("airdrop", AirdropEntity::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(1.0F, 1.0F)
    );
    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE = register("grenade", GrenadeEntity::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(64).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.2F, 0.2F)
    );
    public static final RegistryObject<EntityType<EntityFlare>> FLARE = register("flare", EntityFlare::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.2F, 0.2F)
    );
    public static final RegistryObject<EntityType<EntityExplosiveArrow>> EXPLOSIVE_ARROW = register("explosive_arrow", EntityExplosiveArrow::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(64).setUpdateInterval(20).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F).noSave()
    );
    public static final RegistryObject<EntityType<EntityBullet>> BULLET = register("bullet", EntityBullet::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.05F, 0.05F).noSummon().noSave()
    );
    public static final RegistryObject<EntityType<EntityShotgunPellet>> SHOTGUN_PELLET = register("shotgun_pellet", EntityShotgunPellet::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.05F, 0.05F).noSummon().noSave()
    );
    public static final RegistryObject<EntityType<EntityCrossbowBolt>> CROSSBOW_BOLT = register("crossbow_bolt", EntityCrossbowBolt::new, EntityClassification.MISC,
            builder -> builder.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F).noSummon().noSave()
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
