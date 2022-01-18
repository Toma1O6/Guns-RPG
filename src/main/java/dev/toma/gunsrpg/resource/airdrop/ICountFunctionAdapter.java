package dev.toma.gunsrpg.resource.airdrop;

import com.google.gson.JsonObject;

public interface ICountFunctionAdapter {

    ICountFunction deserialize(JsonObject data);
}
