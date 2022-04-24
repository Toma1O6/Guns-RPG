package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.IIntervalProvider;

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

    @SuppressWarnings("unchecked")
    @Override
    public <Q extends IQuestData> Q copy() {
        return (Q) new AreaSurvivalData(this::getTicks, areaScheme);
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<AreaSurvivalData> {

        @Override
        public AreaSurvivalData resolve(JsonElement element) throws JsonParseException {
            return null;
        }
    }
}
