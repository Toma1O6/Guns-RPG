package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.adapters.MobSpawnerAdapter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class SetEntityControllerProcessor implements IMobSpawnProcessor {

    private final MobSpawnProcessorType<SetEntityControllerProcessor> type;
    private final EntityType<? extends LivingEntity> controllerType;

    private SetEntityControllerProcessor(MobSpawnProcessorType<SetEntityControllerProcessor> type, EntityType<? extends LivingEntity> controllerType) {
        this.type = type;
        this.controllerType = controllerType;
    }

    @Override
    public MobSpawnProcessorType<SetEntityControllerProcessor> getType() {
        return type;
    }

    @Override
    public void processMobSpawn(LivingEntity entity) {
        World world = entity.level;
        LivingEntity controller = controllerType.create(world);
        controller.setPosAndOldPos(entity.getX(), entity.getY(), entity.getZ());
        world.addFreshEntity(controller);
        if (controller instanceof MobEntity && entity instanceof MobEntity) {
            ((MobEntity) controller).setTarget(((MobEntity) entity).getTarget());
        }
        controller.startRiding(entity);
    }

    public static final class Serializer implements IMobSpawnProcessorSerializer<SetEntityControllerProcessor> {

        @Override
        public SetEntityControllerProcessor deserialize(MobSpawnProcessorType<SetEntityControllerProcessor> type, JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "controller"));
            EntityType<? extends LivingEntity> entity = MobSpawnerAdapter.tryParseAsLivingEntity(id);
            return new SetEntityControllerProcessor(type, entity);
        }

        @Override
        public void toNbt(SetEntityControllerProcessor processor, CompoundNBT nbt) {
            nbt.putString("controller", processor.controllerType.getRegistryName().toString());
        }

        @SuppressWarnings("unchecked")
        @Override
        public SetEntityControllerProcessor fromNbt(MobSpawnProcessorType<SetEntityControllerProcessor> type, CompoundNBT nbt) {
            ResourceLocation entityId = new ResourceLocation(nbt.getString("controller"));
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityId);
            return new SetEntityControllerProcessor(type, (EntityType<? extends LivingEntity>) entityType);
        }
    }
}
