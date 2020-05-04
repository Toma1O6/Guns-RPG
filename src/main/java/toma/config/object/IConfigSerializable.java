package toma.config.object;

import com.google.gson.JsonObject;

public interface IConfigSerializable {

    void save(JsonObject toObj);

    void load(JsonObject fromObj);
}
