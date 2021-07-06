package dev.toma.gunsrpg.client.animation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ScriptLoader implements ISelectiveResourceReloadListener {

    private static final Gson GSON_INSTANCE = new GsonBuilder()
            //.registerTypeAdapter(new TypeToken<List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>>() {}.getType(), new ListDeserializer())
            //.registerTypeAdapter(new TypeToken<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>() {}.getType(), new PairDeserializer())
            //.registerTypeAdapter(new TypeToken<Supplier<SimpleAnimation>>() {}.getType(), new SupplierDeserializer())
            //.registerTypeAdapter(MultiStepAnimation.Range.class, new MultiStepAnimation.Range.Deserializer())
            //.registerTypeAdapter(SimpleAnimation.class, new SimpleAnimation.Deserializer())
            .create();
    private static final List<ResourceLocation> PATH = ModUtils.newList(
            resource("pistol_reload"),
            resource("smg_reload"),
            resource("ar_reload"),
            resource("sr_reload"),
            resource("sg_reload")
    );

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        try {
            for(ResourceLocation location : PATH) {
                IResource resource = resourceManager.getResource(location);
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>> list = Collections.emptyList();
                JsonElement element = GSON_INSTANCE.fromJson(reader, JsonElement.class);
                try {
                    list = GSON_INSTANCE.fromJson(element, new TypeToken<List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>>() {}.getType());
                } catch (Exception e) {
                    GunsRPG.log.fatal("Exception while loading resource {}: {}", location, e.getMessage());
                }
                String path = location.getPath();
                int start = path.lastIndexOf("/") + 1;
                int end = path.lastIndexOf(".");
                ClientSideManager.processor().registerScriptAnimation(path.substring(start, end), list);
            }
        } catch (IOException e) {
            GunsRPG.log.fatal(e.getMessage());
        }
    }

    public static class ListDeserializer implements JsonDeserializer<List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>> {
        @Override
        public List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>> list = new ArrayList<>();
            JsonArray array = json.getAsJsonObject().getAsJsonArray("steps");
            for(int i = 0; i < array.size(); i++) {
                JsonElement element = array.get(i);
                list.add(context.deserialize(element, new TypeToken<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>() {}.getType()));
            }
            return list;
        }
    }

    public static class PairDeserializer implements JsonDeserializer<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>> {
        @Override
        public Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Pair.of(context.deserialize(json, MultiStepAnimation.Range.class), context.deserialize(json, new TypeToken<Supplier<SimpleAnimation>>() {}.getType()));
        }
    }

    public static class SupplierDeserializer implements JsonDeserializer<Supplier<SimpleAnimation>> {
        @Override
        public Supplier<SimpleAnimation> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return () -> context.deserialize(json.getAsJsonObject().getAsJsonObject("animation"), SimpleAnimation.class);
        }
    }

    static ResourceLocation resource(String name) {
        return new ResourceLocation("gunsrpg:animations/" + name + ".json");
    }
}
