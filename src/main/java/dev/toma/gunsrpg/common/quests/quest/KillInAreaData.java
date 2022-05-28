package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.quest.area.IQuestAreaProvider;
import dev.toma.gunsrpg.common.quests.quest.area.QuestAreaScheme;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

import java.util.function.Predicate;

public class KillInAreaData extends KillEntityData implements IQuestAreaProvider {

    private final QuestAreaScheme areaScheme;

    public KillInAreaData(Predicate<Entity> entityFilter, int killCount, QuestAreaScheme scheme) {
        super(entityFilter, killCount);
        this.areaScheme = scheme;
    }

    @Override
    public QuestAreaScheme getAreaScheme() {
        return areaScheme;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Q extends IQuestData> Q copy() {
        return (Q) new KillInAreaData(this.getEntityFilter(), this.getKillTarget(), areaScheme);
    }

    @Override
    public String toString() {
        return String.format("KillEntitiesInArea - Filter: %s, Count: %d, Area: { %s }", this.getEntityFilter().toString(), this.getKillTarget(), areaScheme.toString());
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<KillInAreaData> {

        @Override
        public KillInAreaData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonElement filterElement = object.get("entities");
            Predicate<Entity> filter = parseEntityFilter(filterElement);
            int count = JSONUtils.getAsInt(object, "count", 1);
            QuestAreaScheme areaScheme = QuestAreaScheme.fromJson(JSONUtils.getAsJsonObject(object, "area"));
            return new KillInAreaData(filter, count, areaScheme);
        }

        @Override
        public CompoundNBT serialize(KillInAreaData data) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("kills", data.getKillTarget());
            nbt.put("area", data.areaScheme.toNbt());
            return nbt;
        }

        @Override
        public KillInAreaData deserialize(CompoundNBT nbt) {
            int kills = nbt.getInt("kills");
            QuestAreaScheme scheme = QuestAreaScheme.fromNbt(nbt.getCompound("area"));
            return new KillInAreaData(ANY_HOSTILE, kills, scheme);
        }
    }
}
