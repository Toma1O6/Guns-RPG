package lib.toma.animations.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lib.toma.animations.AnimationCompatLayer;
import lib.toma.animations.pipeline.IKeyframeProvider;
import lib.toma.animations.pipeline.frame.IKeyframe;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class AnimationLoader extends JsonReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("Loader");
    private Gson gson;
    private final Map<ResourceLocation, IKeyframeProvider> animationDefinitions = new HashMap<>();

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
        if (devMode) {
            AnimationCompatLayer.instance().setup();
        }
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(this);
    }

    public IKeyframeProvider getProvider(ResourceLocation key) {
        return animationDefinitions.computeIfAbsent(key, KeyframeProvider::noFrames);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager manager, IProfiler profiler) {
        profiler.push("Animation loading");
        Logger log = AnimationCompatLayer.logger;
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
        log.info(MARKER, "Animations loaded");
        profiler.pop();
    }
}
