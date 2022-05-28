package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.quest.area.IQuestAreaProvider;
import dev.toma.gunsrpg.common.quests.quest.area.QuestAreaScheme;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class AreaSurvivalData extends SurvivalData implements IQuestAreaProvider {

    private final QuestAreaScheme areaScheme;

    public AreaSurvivalData(IIntervalProvider provider, QuestAreaScheme areaScheme) {
        super(provider);
        this.areaScheme = areaScheme;
    }

    @Override
    public QuestAreaScheme getAreaScheme() {
        return areaScheme;
    }

    @Override
    public String toString() {
        int ticks = this.getTicks();
        return String.format("Survival - Timer: [%s, (%d ticks)], Area: { %s }", Interval.format(ticks, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND).compact()), ticks, areaScheme.toString());
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<AreaSurvivalData> {

        @Override
        public AreaSurvivalData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            String duration = JSONUtils.getAsString(object, "duration");
            IIntervalProvider provider = Interval.parse(duration);
            QuestAreaScheme areaScheme = QuestAreaScheme.fromJson(JSONUtils.getAsJsonObject(object, "area"));
            return new AreaSurvivalData(provider, areaScheme);
        }

        @Override
        public CompoundNBT serialize(AreaSurvivalData data) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("ticks", data.getTicks());
            nbt.put("area", data.areaScheme.toNbt());
            return nbt;
        }

        @Override
        public AreaSurvivalData deserialize(CompoundNBT nbt) {
            int ticks = nbt.getInt("ticks");
            QuestAreaScheme scheme = QuestAreaScheme.fromNbt(nbt.getCompound("area"));
            return new AreaSurvivalData(() -> ticks, scheme);
        }
    }
}
