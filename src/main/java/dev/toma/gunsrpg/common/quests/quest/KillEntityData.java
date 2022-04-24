package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;

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

    public static final class Serializer implements QuestType.IQuestDataResolver<KillEntityData> {

        @Override
        public KillEntityData resolve(JsonElement element) throws JsonParseException {
            return null;
        }
    }
}
