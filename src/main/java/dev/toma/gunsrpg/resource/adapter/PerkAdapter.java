package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkValueSpec;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class PerkAdapter implements JsonDeserializer<Perk> {

    @Override
    public Perk deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "attribute"));
        IAttributeId attributeId = Attribs.find(id);
        if (attributeId == null) {
            throw new JsonSyntaxException("Unknown attribute: " + id);
        }
        float scale = JSONUtils.getAsFloat(object, "scaling");
        PerkValueSpec bounds = context.deserialize(JSONUtils.getAsJsonObject(object, "bounds"), PerkValueSpec.class);
        boolean invertCalculation = JSONUtils.getAsBoolean(object, "invertCalculation", false);
        return new Perk(attributeId, scale, bounds, invertCalculation);
    }
}
