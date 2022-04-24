package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;

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

    public static final class Serializer implements QuestType.IQuestDataResolver<KillInAreaData> {

        @Override
        public KillInAreaData resolve(JsonElement element) throws JsonParseException {
            return null;
        }
    }
}
