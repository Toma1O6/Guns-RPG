package dev.toma.gunsrpg.resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class SingleJsonFileReloadListener extends ReloadListener<JsonElement> {

    private static final Marker MARKER = MarkerManager.getMarker("ResourceLoader");
    private final ResourceLocation path;
    private final Gson gson;

    public SingleJsonFileReloadListener(ResourceLocation path, Gson gson) {
        this.path = path;
        this.gson = gson;
    }

    @Override
    protected JsonElement prepare(IResourceManager resourceManager, IProfiler profiler) {
        try (
            IResource resource = resourceManager.getResource(path);
            InputStream stream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        ) {
            JsonElement element = JSONUtils.fromJson(gson, reader, JsonElement.class);
            if (element != null) {
                return element;
            }
            GunsRPG.log.error(MARKER, "Couldn't load data file {} as it's null or doesn't exist.", path);
        } catch (IllegalArgumentException | IOException | JsonParseException e) {
            GunsRPG.log.error("Couldn't parse data file {} because {} exception was thrown.", path, e);
        }
        return new JsonObject(); // empty result on error
    }
}
