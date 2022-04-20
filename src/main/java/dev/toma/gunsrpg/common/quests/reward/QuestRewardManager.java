package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.adapters.AssemblyFunctionAdapter;
import dev.toma.gunsrpg.common.quests.adapters.QuestRewardAdapter;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.resource.adapter.CountFunctionAdapter;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.util.ILogHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;

public class QuestRewardManager extends SingleJsonFileReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(IQuestItemProvider.class, new QuestRewardAdapter())
            .registerTypeHierarchyAdapter(ICountFunction.class, CountFunctionAdapter.positive())
            .registerTypeHierarchyAdapter(IAssemblyFunction.class, new AssemblyFunctionAdapter())
            .create();
    private final ILogHandler logger;
    private final Int2ObjectMap<QuestRewardList> rewardTierMap = new Int2ObjectOpenHashMap<>();

    public QuestRewardManager(ILogHandler logger) {
        super(GunsRPG.makeResource("quest/rewards.json"), GSON);
        this.logger = logger;
    }

    @Override
    protected void apply(JsonElement element, IResourceManager manager, IProfiler profiler) {
        logger.info("Loading quest reward list");
        // TODO loading
        logger.info("Quest rewards loaded");
    }
}
