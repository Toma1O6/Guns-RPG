package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.*;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class KillEntityData implements IQuestData {

    public static final Predicate<Entity> ANY_HOSTILE = entity -> entity instanceof MonsterEntity;

    private final Predicate<Entity> entityFilter;
    private final int kills;

    public KillEntityData(@Nullable Predicate<Entity> entityFilter, int kills) {
        this.entityFilter = ModUtils.either(entityFilter, ANY_HOSTILE);
        this.kills = kills;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Q extends IQuestData> Q copy() {
        return (Q) new KillEntityData(entityFilter, kills);
    }

    protected Predicate<Entity> getEntityFilter() {
        return entityFilter;
    }

    protected int getKillTarget() {
        return kills;
    }

    @Nullable
    static Predicate<Entity> parseEntityFilter(JsonElement element) throws JsonParseException {
        Predicate<Entity> entityFilter = null;
        if (!element.isJsonNull()) {
            JsonArray array = element.getAsJsonArray();
            EntityType<?>[] validTypes = JsonHelper.deserializeInto(array, EntityType[]::new, KillEntityData::parseEntityType);
            entityFilter = entity -> ModUtils.contains(entity.getType(), validTypes);
        }
        return entityFilter;
    }

    static EntityType<?> parseEntityType(JsonElement element) throws JsonParseException {
        String id = element.getAsString();
        ResourceLocation location = new ResourceLocation(id);
        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(location);
        if (type == null) {
            throw new JsonSyntaxException("Unknown entity: " + id);
        }
        return type;
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<KillEntityData> {

        @Override
        public KillEntityData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonElement filterElement = object.get("entities");
            Predicate<Entity> entityFilter = parseEntityFilter(filterElement);
            int count = JSONUtils.getAsInt(object, "count", 1);
            return new KillEntityData(entityFilter, count);
        }
    }
}
