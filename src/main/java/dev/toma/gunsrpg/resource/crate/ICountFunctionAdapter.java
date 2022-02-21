package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;

public interface ICountFunctionAdapter {

    ICountFunction deserialize(JsonObject data);
}
