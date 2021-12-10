package dev.toma.gunsrpg.resource.skill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public final class SkillManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .create();

    public SkillManager() {
        super(GSON, "skills");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceMap, IResourceManager resourceManager, IProfiler profiler) {

    }
}
