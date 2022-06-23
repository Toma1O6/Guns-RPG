package dev.toma.gunsrpg.common.quests.quest.filter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EntityFilterType<F extends IEntityFilter> {

    private static final Map<ResourceLocation, EntityFilterType<?>> MAP = new HashMap<>();

    public static final EntityFilterType<IEntityFilter> ANY = new EntityFilterType<>(GunsRPG.makeResource("any"), new AnyEntityFilterSerializer());
    public static final EntityFilterType<SpecificEntityFilter> SPECIFIC = new EntityFilterType<>(GunsRPG.makeResource("specific_entity"), new SpecificEntityFilter.Serializer());
    public static final EntityFilterType<HostileMobsFilter> HOSTILE = new EntityFilterType<>(GunsRPG.makeResource("hostile"), new HostileMobsFilter.Serializer());

    private final ResourceLocation id;
    private final Serializer<F> serializer;

    public EntityFilterType(ResourceLocation id, Serializer<F> serializer) {
        this.id = id;
        this.serializer = serializer;

        MAP.put(id, this);
    }

    @SuppressWarnings("unchecked")
    public static <F extends IEntityFilter> F any() {
        return (F) AnyEntityFilterSerializer.ANY;
    }

    @SuppressWarnings("unchecked")
    public static <F extends IEntityFilter> EntityFilterType<F> getType(ResourceLocation id) {
        return (EntityFilterType<F>) MAP.get(id);
    }

    public Serializer<F> getSerializer() {
        return serializer;
    }

    public ResourceLocation getId() {
        return id;
    }

    public static <F extends IEntityFilter> F resolveJsonFile(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        ResourceLocation type = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        EntityFilterType<F> filterType = getType(type);
        if (filterType == null) {
            throw new JsonSyntaxException("Unknown entity filter: " + type);
        }
        Serializer<F> serializer = filterType.getSerializer();
        return serializer.resolveFile(filterType, object);
    }

    @SuppressWarnings("unchecked")
    public static <F extends IEntityFilter> CompoundNBT serializeNbt(F filter) {
        CompoundNBT nbt = new CompoundNBT();
        EntityFilterType<F> type = (EntityFilterType<F>) filter.getType();
        nbt.putString("type", type.getId().toString());
        type.getSerializer().toNbt(filter, nbt);
        return nbt;
    }

    @Nullable
    public static <F extends IEntityFilter> F deserializeNbt(CompoundNBT nbt) {
        ResourceLocation type = new ResourceLocation(nbt.getString("type"));
        EntityFilterType<F> filterType = getType(type);
        if (filterType == null) {
            return any();
        }
        return filterType.getSerializer().fromNbt(filterType, nbt);
    }

    public interface Serializer<F extends IEntityFilter> {

        F resolveFile(EntityFilterType<F> type, JsonElement json) throws JsonParseException;

        void toNbt(F filter, CompoundNBT nbt);

        F fromNbt(EntityFilterType<F> filterType, CompoundNBT nbt);
    }

    private static final class AnyEntityFilterSerializer implements Serializer<IEntityFilter> {

        static final IEntityFilter ANY = new IEntityFilter() {
            @Override
            public EntityFilterType<?> getType() {
                return EntityFilterType.ANY;
            }

            @Override
            public boolean test(Entity entity) {
                return true;
            }

            @Override
            public String toString() {
                return "Any";
            }
        };

        @Override
        public IEntityFilter resolveFile(EntityFilterType<IEntityFilter> type, JsonElement json) throws JsonParseException {
            return ANY;
        }

        @Override
        public void toNbt(IEntityFilter filter, CompoundNBT nbt) {
        }

        @Override
        public IEntityFilter fromNbt(EntityFilterType<IEntityFilter> filterType, CompoundNBT nbt) {
            return ANY;
        }
    }
}
