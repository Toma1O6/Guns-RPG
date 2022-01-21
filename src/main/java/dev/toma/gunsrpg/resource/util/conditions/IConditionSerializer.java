package dev.toma.gunsrpg.resource.util.conditions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;

public interface IConditionSerializer<C extends IRecipeCondition> {

    C deserialize(JsonObject data) throws JsonParseException;

    void toNetwork(PacketBuffer buffer, C condition);

    C fromNetwork(PacketBuffer buffer);
}
