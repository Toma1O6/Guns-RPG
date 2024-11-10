package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.quest.area.IQuestAreaProvider;
import dev.toma.gunsrpg.common.quests.quest.area.QuestAreaScheme;
import dev.toma.gunsrpg.common.quests.quest.filter.EntityFilterType;
import dev.toma.gunsrpg.common.quests.quest.filter.IEntityFilter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class KillInAreaData extends KillEntityData implements IQuestAreaProvider {

    private final QuestAreaScheme areaScheme;

    public KillInAreaData(IEntityFilter filter, int killCount, float memberScale, QuestAreaScheme scheme) {
        super(filter, killCount, memberScale);
        this.areaScheme = scheme;
    }

    @Override
    public QuestAreaScheme getAreaScheme() {
        return areaScheme;
    }

    @Override
    public String toString() {
        return String.format("KillEntitiesInArea - Filter: %s, Count: %d, Area: { %s }", this.getEntityFilter().toString(), this.getKillTargetUnmodified(), areaScheme.toString());
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<KillInAreaData> {

        @Override
        public KillInAreaData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonElement filterElement = object.get("entities");
            IEntityFilter filter = EntityFilterType.resolveJsonFile(filterElement);
            int count = JSONUtils.getAsInt(object, "count", 1);
            float scale = JSONUtils.getAsFloat(object, "memberScale", GunsRPG.config.quests.defaultKillMemberMultiplier);
            QuestAreaScheme areaScheme = QuestAreaScheme.fromJson(JSONUtils.getAsJsonObject(object, "area"));
            return new KillInAreaData(filter, count, scale, areaScheme);
        }

        @Override
        public CompoundNBT serialize(KillInAreaData data) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("kills", data.kills);
            nbt.putFloat("memberScale", data.memberScaling);
            nbt.put("area", data.areaScheme.toNbt());
            nbt.put("filter", EntityFilterType.serializeNbt(data.filter));
            return nbt;
        }

        @Override
        public KillInAreaData deserialize(CompoundNBT nbt) {
            int kills = nbt.getInt("kills");
            float memberScale = nbt.getFloat("memberScale");
            QuestAreaScheme scheme = QuestAreaScheme.fromNbt(nbt.getCompound("area"));
            IEntityFilter filter = EntityFilterType.deserializeNbt(nbt.getCompound("filter"));
            return new KillInAreaData(filter, kills, memberScale, scheme);
        }
    }
}
