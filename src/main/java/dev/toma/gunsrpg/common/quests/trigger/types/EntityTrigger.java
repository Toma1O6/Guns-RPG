package dev.toma.gunsrpg.common.quests.trigger.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.tracking.EntityProgressionTracker;
import dev.toma.gunsrpg.common.quests.tracking.IProgressionTracker;
import dev.toma.gunsrpg.common.quests.trigger.ITrigger;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerSerializer;
import dev.toma.gunsrpg.common.quests.trigger.Triggers;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.JSONUtils;

public class EntityTrigger extends AbstractTrigger<Entity> {

    private final EntityType<?>[] validTypes;

    public EntityTrigger(EntityType<?>[] validTypes) {
        super(Triggers.KILL_ENTITY);
        this.validTypes = validTypes;
    }

    @Override
    public IProgressionTracker<Entity> createTracker(JsonObject taskObj) {
        int target = JSONUtils.getAsInt(taskObj, "count");
        return new EntityProgressionTracker(target);
    }

    @Override
    public boolean isValid(Entity context) {
        EntityType<?> type = context.getType();
        return ModUtils.contains(type, validTypes, Object::equals);
    }

    public static class Serializer implements ITriggerSerializer<Entity> {

        @Override
        public ITrigger<Entity> fromJson(JsonObject obj) throws JsonParseException {
            return null;
        }
    }
}
