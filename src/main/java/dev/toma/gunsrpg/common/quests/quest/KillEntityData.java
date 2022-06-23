package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.quest.filter.EntityFilterType;
import dev.toma.gunsrpg.common.quests.quest.filter.IEntityFilter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class KillEntityData implements IQuestData {

    private final IEntityFilter filter;
    private final int kills;

    public KillEntityData(IEntityFilter filter, int kills) {
        this.filter = filter;
        this.kills = kills;
    }

    protected IEntityFilter getEntityFilter() {
        return filter;
    }

    protected int getKillTarget() {
        return kills;
    }

    @Override
    public String toString() {
        return String.format("KillEntities - Filter: %s, Count: %d", filter.toString(), kills);
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<KillEntityData> {

        @Override
        public KillEntityData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonElement filterElement = object.get("entities");
            IEntityFilter entityFilter = EntityFilterType.resolveJsonFile(filterElement);
            int count = JSONUtils.getAsInt(object, "count", 1);
            return new KillEntityData(entityFilter, count);
        }

        @Override
        public CompoundNBT serialize(KillEntityData data) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("kills", data.kills);
            nbt.put("filter", EntityFilterType.serializeNbt(data.filter));
            return nbt;
        }

        @Override
        public KillEntityData deserialize(CompoundNBT nbt) {
            return new KillEntityData(EntityFilterType.deserializeNbt(nbt.getCompound("filter")), nbt.getInt("kills"));
        }
    }
}
