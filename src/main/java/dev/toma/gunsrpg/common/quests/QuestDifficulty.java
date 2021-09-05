package dev.toma.gunsrpg.common.quests;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public enum QuestDifficulty {

    EASY, MEDIUM, HARD;

    public static QuestDifficulty fromJson(JsonElement element) throws JsonParseException {
        String diff = element.getAsString();
        try {
            return QuestDifficulty.valueOf(diff.toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new JsonSyntaxException("Unknown difficulty type: " + diff);
        }
    }
}
