package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class CrystalTypesAdapter implements JsonDeserializer<CrystalConfiguration.Types> {

    @Override
    public CrystalConfiguration.Types deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ICountFunction buff = context.deserialize(JSONUtils.getAsJsonObject(object, "buff"), ICountFunction.class);
        ICountFunction debuff = context.deserialize(JSONUtils.getAsJsonObject(object, "debuff"), ICountFunction.class);
        return new CrystalConfiguration.Types(buff, debuff);
    }
}
