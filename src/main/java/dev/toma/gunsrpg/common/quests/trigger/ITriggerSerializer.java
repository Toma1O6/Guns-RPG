package dev.toma.gunsrpg.common.quests.trigger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface ITriggerSerializer<CTX> {

    ITrigger<CTX> fromJson(JsonObject obj) throws JsonParseException;
}
