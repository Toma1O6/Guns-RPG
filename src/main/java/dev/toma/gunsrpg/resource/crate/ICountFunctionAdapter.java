package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;
import dev.toma.gunsrpg.resource.util.functions.IFunction;

public interface ICountFunctionAdapter {

    ICountFunction deserialize(JsonObject data, IFunction range);
}
