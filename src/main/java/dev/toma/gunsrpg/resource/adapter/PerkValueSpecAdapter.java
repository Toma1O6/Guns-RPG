package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.PerkValueSpec;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class PerkValueSpecAdapter implements JsonDeserializer<PerkValueSpec> {

    @Override
    public PerkValueSpec deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = JsonHelper.asJsonObject(json);
        float buff = JSONUtils.getAsFloat(data, "buff");
        float debuff = JSONUtils.getAsFloat(data, "debuff");
        return new PerkValueSpec(buff, debuff);
    }
}
