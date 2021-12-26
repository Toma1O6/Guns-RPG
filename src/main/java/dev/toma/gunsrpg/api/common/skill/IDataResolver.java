package dev.toma.gunsrpg.api.common.skill;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface IDataResolver<D> {

    D resolve(JsonElement json);
}
