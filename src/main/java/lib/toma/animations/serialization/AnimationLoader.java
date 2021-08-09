package lib.toma.animations.serialization;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.IKeyframeProvider;
import lib.toma.animations.pipeline.frame.KeyframeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class AnimationLoader extends JsonReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("Loader");
    private Gson gson;
    private final Map<ResourceLocation, IKeyframeProvider> animationDefinitions = new HashMap<>();
    private final List<ILoadingFinished> loadListeners = new ArrayList<>();
    private ILoader loader;

    public AnimationLoader() {
        super(new GsonBuilder().disableHtmlEscaping().create(), "animations");
    }

    public void init(boolean devMode) {
        init(devMode, gsonBuilder -> {});
    }

    public void init(boolean devMode, Consumer<GsonBuilder> callback) {
        GsonBuilder builder = new GsonBuilder();
        builder
                .disableHtmlEscaping()
                .registerTypeHierarchyAdapter(IKeyframeProvider.class, new KeyframeProviderSerializer())
                .registerTypeHierarchyAdapter(IKeyframe.class, new KeyframeSerializer())
                .registerTypeAdapter(Vector3d.class, new Vector3dSerializer())
                .registerTypeAdapter(Vector3f.class, new Vector3fSerializer())
                .registerTypeAdapter(Quaternion.class, new QuaternionSerializer());
        callback.accept(builder);
        gson = builder.create();
        loader = reader -> gson.fromJson(reader, IKeyframeProvider.class);
        if (devMode) {
            AnimationEngine.get().setup();
        }
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(this);
    }

    public IKeyframeProvider getProvider(ResourceLocation key) {
        return animationDefinitions.computeIfAbsent(key, KeyframeProvider::noFrames);
    }

    public void addListener(ILoadingFinished loadingFinishedListener) {
        this.loadListeners.add(loadingFinishedListener);
    }

    public String serializeToString(IKeyframeProvider provider) {
        return gson.toJson(provider, IKeyframeProvider.class).replaceAll("\\s+", "");
    }

    public ILoader reader() {
        return loader;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager manager, IProfiler profiler) {
        profiler.push("Animation loading");
        Logger log = AnimationEngine.logger;
        log.info(MARKER, "Loading animations");
        animationDefinitions.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement value = entry.getValue();
            try {
                IKeyframeProvider provider = gson.fromJson(value, IKeyframeProvider.class);
                animationDefinitions.put(key, provider);
            } catch (JsonParseException parseException) {
                log.error(MARKER, "Couldn't parse animation file {}: {}", key, parseException.getMessage());
            } catch (Exception e) {
                log.fatal(MARKER, "Exception ocurred while parsing animation file {}: {}", key, e.toString());
            }
        }
        notifyListeners(log);
        log.info(MARKER, "Animations loaded");
        profiler.pop();
    }

    private void notifyListeners(Logger log) {
        int len = loadListeners.size();
        if (len == 0) return;
        log.debug(MARKER, "Sending load notification to {} listeners", loadListeners.size());
        ImmutableMap<ResourceLocation, IKeyframeProvider> map = ImmutableMap.copyOf(animationDefinitions);
        loadListeners.forEach(listener -> listener.onLoaded(map, loader));
        log.debug(MARKER, "All listeners have been notified");
    }

    @FunctionalInterface
    public interface ILoadingFinished {
        void onLoaded(Map<ResourceLocation, IKeyframeProvider> providerMap, ILoader loader);
    }

    public interface ILoader {
        IKeyframeProvider load(JsonReader reader);
    }
}
