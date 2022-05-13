package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public interface IMobSpawnProcessorSerializer<P extends IMobSpawnProcessor> {

    P deserialize(JsonElement element) throws JsonParseException;
}
