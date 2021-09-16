package dev.toma.gunsrpg.common.quests.trigger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public final class TriggerType<CTX> {

    private final ResourceLocation id;
    private final ITriggerSerializer<CTX> serializer;

    private TriggerType(ResourceLocation id, ITriggerSerializer<CTX> serializer) {
        this.id = Objects.requireNonNull(id);
        this.serializer = Objects.requireNonNull(serializer);
    }

    public static <CTX> TriggerType<CTX> newType(ResourceLocation id, ITriggerSerializer<CTX> serializer) {
        return new TriggerType<>(id, serializer);
    }

    public static <CTX> ITrigger<CTX> fromJson(ResourceLocation id, JsonObject data) throws JsonParseException {
        TriggerType<CTX> type = Triggers.getTriggerByKey(id);
        if (type == null)
            throw new JsonSyntaxException("Unknown trigger " + id.toString());
        return type.fromJson(data);
    }

    public ResourceLocation getRegistryName() {
        return id;
    }

    public ITrigger<CTX> fromJson(JsonObject data) throws JsonParseException {
        return serializer.fromJson(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriggerType<?> that = (TriggerType<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
