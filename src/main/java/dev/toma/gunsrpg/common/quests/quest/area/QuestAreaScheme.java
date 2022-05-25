package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.adapters.MobSpawnerAdapter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QuestAreaScheme {

    private final int size;
    private final int distance;
    private final List<IMobSpawner> mobSpawnerList;

    public QuestAreaScheme(int size, int distance, List<IMobSpawner> mobSpawnerList) {
        this.size = size;
        this.distance = distance;
        this.mobSpawnerList = mobSpawnerList;
    }

    public int getSize() {
        return size;
    }

    public int getDistance() {
        return distance;
    }

    public List<IMobSpawner> getMobSpawnerList() {
        return mobSpawnerList;
    }

    @Override
    public String toString() {
        return String.format("Size: %d, MinDistance: %d, SpawnerCount: %d", size, distance, mobSpawnerList.size());
    }

    public static QuestAreaScheme fromJson(JsonObject object) throws JsonParseException {
        int size = JSONUtils.getAsInt(object, "size");
        int distance = JSONUtils.getAsInt(object, "distance");
        List<IMobSpawner> list;
        if (object.has("spawners")) {
            JsonArray array = JSONUtils.getAsJsonArray(object, "spawners");
            MobSpawnerAdapter adapter = new MobSpawnerAdapter();
            list = JsonHelper.<List<IMobSpawner>, IMobSpawner>deserialize(array, arr -> new ArrayList<>(arr.size()), adapter::deserialize, List::add);
        } else {
            list = Collections.emptyList();
        }
        return new QuestAreaScheme(size, distance, list);
    }
}
