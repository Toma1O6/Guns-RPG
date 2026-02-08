package dev.toma.gunsrpg.common.experience;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

public final class MobExpRewardManager extends JsonReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("MobExpRewardManager");
    public static final Gson GSON = new Gson();

    private final Map<EntityType<?>, MobExpReward> rewardMap = new HashMap<>();

    public MobExpRewardManager() {
        super(GSON, "mob_exp");
    }

    public MobExpReward getReward(EntityType<?> type) {
        return this.rewardMap.getOrDefault(type, MobExpReward.DEFAULT);
    }

    public MobExpReward getReward(Entity entity) {
        return this.getReward(entity.getType());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager manager, IProfiler profiler) {
        GunsRPG.log.info(MARKER, "Loading mob experience rewards");
        this.rewardMap.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement resource = entry.getValue();
            try {
                MobExpReward reward = MobExpReward.parseJson(resource);
                this.rewardMap.put(reward.getEntityType(), reward);
            } catch (Exception e) {
                GunsRPG.log.error(MARKER, "Error loading mob experience reward file {}", id, e);
            }
        }
        GunsRPG.log.info(MARKER, "Loaded {} mob experience rewards", this.rewardMap.size());
    }
}
