package dev.toma.gunsrpg.resource.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface ILevelRewardAdapter<R extends ILevelReward> {

    R resolveFromJson(JsonObject object) throws JsonParseException;
}
