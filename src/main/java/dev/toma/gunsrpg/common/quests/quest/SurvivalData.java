package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

public class SurvivalData implements IQuestData {

    private final int ticks;

    public SurvivalData(IIntervalProvider provider) {
        this.ticks = provider.getTicks();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Q extends IQuestData> Q copy() {
        return (Q) new SurvivalData(() -> ticks);
    }

    public int getTicks() {
        return ticks;
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<SurvivalData> {

        @Override
        public SurvivalData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            String timeString = JSONUtils.getAsString(object, "duration");
            Interval interval = Interval.parse(timeString);
            return new SurvivalData(interval);
        }
    }
}
