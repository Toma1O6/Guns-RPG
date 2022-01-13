package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.skills.core.SkillProperties;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class SkillPropertyAdapter implements JsonDeserializer<ISkillProperties> {

    @Override
    public ISkillProperties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = JsonHelper.asJsonObject(json);
        int level = JSONUtils.getAsInt(data, "level");
        int price = JSONUtils.getAsInt(data, "price");
        ITransactionValidator validator = deserializeValidator(JSONUtils.getAsJsonObject(data, "transactionValidator"));
        return new SkillProperties(level, price, validator);
    }

    private static <T extends ITransactionValidator, D> T deserializeValidator(JsonObject object) throws JsonParseException {
        String typeKey = JSONUtils.getAsString(object, "type");
        ITransactionValidatorFactory<T, D> factory = TransactionValidatorRegistry.getValidatorFactory(new ResourceLocation(typeKey));
        if (factory == null) {
            throw new JsonSyntaxException("Unknown transaction validator: " + typeKey);
        }
        JsonElement data = object.has("data") ? object.get("data") : JsonNull.INSTANCE;
        return TransactionValidatorRegistry.getTransactionValidator(factory, data);
    }
}
