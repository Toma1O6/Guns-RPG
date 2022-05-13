package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.adapters.MobSpawnerAdapter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SetEntityControllerProcessor implements IMobSpawnProcessor {

    private final EntityType<? extends LivingEntity> controllerType;

    private SetEntityControllerProcessor(EntityType<? extends LivingEntity> controllerType) {
        this.controllerType = controllerType;
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
        public SetEntityControllerProcessor deserialize(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "controller"));
            EntityType<? extends LivingEntity> type = MobSpawnerAdapter.tryParseAsLivingEntity(id);
            return new SetEntityControllerProcessor(type);
        }
    }
}
