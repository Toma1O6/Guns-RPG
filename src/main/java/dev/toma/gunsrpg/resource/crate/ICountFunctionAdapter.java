package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import net.minecraft.network.PacketBuffer;

public interface ICountFunctionAdapter<F extends ICountFunction> {

    ICountFunction deserialize(JsonObject data, IFunction range);

    void encode(F function, PacketBuffer buffer);

    F decode(PacketBuffer buffer);
}
