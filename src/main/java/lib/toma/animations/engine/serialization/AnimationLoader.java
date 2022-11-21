package lib.toma.animations.engine.serialization;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationLoader;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.engine.frame.KeyframeProvider;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AnimationLoader extends JsonReloadListener implements IAnimationLoader {

    public static final Marker MARKER = MarkerManager.getMarker("Loader");
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeHierarchyAdapter(IKeyframeProvider.class, new KeyframeProviderSerializer())
            .registerTypeHierarchyAdapter(IKeyframe.class, new KeyframeSerializer())
            .registerTypeAdapter(Vector3d.class, new Vector3dSerializer())
            .registerTypeAdapter(Vector3f.class, new Vector3fSerializer())
            .create();
    private final Map<ResourceLocation, IKeyframeProvider> animationDefinitions = new HashMap<>();
    private final List<ILoadingFinished> loadListeners = new ArrayList<>();
    private final ILoader loader;

    public AnimationLoader() {
        super(GSON, "animation");
        this.loader = reader -> GSON.fromJson(reader, IKeyframeProvider.class);
    }

    @Override
    public IKeyframeProvider getProvider(ResourceLocation key) {
        return animationDefinitions.computeIfAbsent(key, KeyframeProvider::noFrames);
    }

    @Override
    public ILoader getResourceReader() {
        return loader;
    }

    @Override
    public String serializeResource(IKeyframeProvider provider) {
        return GSON.toJson(provider, IKeyframeProvider.class).replaceAll("\\s+", "");
    }

    @Override
    public void addLoadingListener(ILoadingFinished listener) {
        loadListeners.add(listener);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager manager, IProfiler profiler) {
        profiler.startTick();
        profiler.push("Animation loading");
        Logger log = AnimationEngine.logger;
        log.info(MARKER, "Loading animations");
        animationDefinitions.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement value = entry.getValue();
            try {
                IKeyframeProvider provider = GSON.fromJson(value, IKeyframeProvider.class);
                animationDefinitions.put(key, provider);
            } catch (JsonParseException parseException) {
                log.error(MARKER, "Couldn't parse animation file {}: {}", key, parseException.getMessage());
            } catch (Exception e) {
                log.fatal(MARKER, "Exception ocurred while parsing animation file {}: {}", key, e.toString());
            }
        }
        notifyListeners(log);
        log.info(MARKER, "Loaded {} animations", animationDefinitions.size());
        profiler.pop();
        profiler.endTick();
    }

    private void notifyListeners(Logger log) {
        int len = loadListeners.size();
        if (len == 0) return;
        log.debug(MARKER, "Sending load notification to {} listeners", loadListeners.size());
        ImmutableMap<ResourceLocation, IKeyframeProvider> map = ImmutableMap.copyOf(animationDefinitions);
        loadListeners.forEach(listener -> listener.onLoaded(map, loader));
        log.debug(MARKER, "All listeners have been notified");
    }
}
