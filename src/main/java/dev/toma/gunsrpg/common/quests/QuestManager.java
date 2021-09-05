package dev.toma.gunsrpg.common.quests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class QuestManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private final ILogHandler handler;

    public QuestManager(ILogHandler handler) {
        super(GSON, "quest_system/quests");
        this.handler = handler;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        long time = System.currentTimeMillis();
        handler.info("Loading quests");
        handler.info("Quests loaded, took {}ms", System.currentTimeMillis() - time);
    }
}
