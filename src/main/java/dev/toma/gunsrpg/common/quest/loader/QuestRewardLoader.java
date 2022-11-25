package dev.toma.gunsrpg.common.quest.loader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quest.QuestDataManager;
import dev.toma.gunsrpg.common.quest.reward.TieredReward;
import dev.toma.questing.reward.IReward;
import dev.toma.questing.reward.RewardType;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class QuestRewardLoader extends JsonReloadListener {

    private static final Gson GSON = new Gson();
    private final Map<ResourceLocation, IReward> rewardMap = new HashMap<>();

    public QuestRewardLoader() {
        super(GSON, "quest/reward");
    }

    public Optional<IReward> getRewardByIdentifier(ResourceLocation identifier) {
        return Optional.ofNullable(this.rewardMap.get(identifier));
    }

    public IReward getTieredReward(TieredReward.RewardTier tier) {
        return this.getRewardByIdentifier(tier.getIdentifier()).orElseThrow(NullPointerException::new);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager manager, IProfiler profiler) {
        GunsRPG.log.info(QuestDataManager.MARKER, "Loading quest rewards");
        this.rewardMap.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation path = entry.getKey();
            JsonElement element = entry.getValue();
            GunsRPG.log.debug(QuestDataManager.MARKER, "Loading quest reward with ID {}", path);
            try {
                IReward reward = RewardType.fromJson(element);
                this.rewardMap.put(path, reward);
                GunsRPG.log.debug(QuestDataManager.MARKER, "Loaded quest reward with ID {}", path);
            } catch (JsonParseException e) {
                GunsRPG.log.error(QuestDataManager.MARKER, "Got error while loading quest {} - {}", path, e);
            } catch (Exception e) {
                GunsRPG.log.fatal(QuestDataManager.MARKER, "Got fatal error while loading quest {}", path);
                throw new IllegalStateException(e);
            }
        }
        GunsRPG.log.info(QuestDataManager.MARKER, "Loaded {} quest rewards", this.rewardMap.size());
    }
}
