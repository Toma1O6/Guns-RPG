package dev.toma.gunsrpg.resource.util.functions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface IFunctionAdapter<F extends IFunction> {

    F resolveFromJson(JsonObject object) throws JsonParseException;
}
