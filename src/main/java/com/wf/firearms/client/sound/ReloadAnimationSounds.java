package com.wf.firearms.client.sound;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 从 gunsrpg animation json 提取换弹音效时间点。 */
public final class ReloadAnimationSounds {
    private static final Map<ResourceLocation, List<SoundCue>> CACHE = new ConcurrentHashMap<>();

    private ReloadAnimationSounds() {}

    public record SoundCue(float normalizedTime, ResourceLocation sound, float volume, float pitch) {}

    public static List<SoundCue> cuesFor(String weaponKey, ReloadSoundClip clip) {
        ResourceLocation path = WeaponReloadAnimations.clipPath(weaponKey, clip);
        if (path == null) {
            return List.of();
        }
        return CACHE.computeIfAbsent(path, ReloadAnimationSounds::loadFromResource);
    }

    private static List<SoundCue> loadFromResource(ResourceLocation path) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getResourceManager() == null) {
            return List.of();
        }
        try {
            Resource resource = mc.getResourceManager().getResource(path).orElse(null);
            if (resource == null) {
                return List.of();
            }
            JsonObject root;
            try (var reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
                root = JsonParser.parseReader(reader).getAsJsonObject();
            }
            JsonArray events = root.getAsJsonArray("events");
            if (events == null) {
                return List.of();
            }
            List<SoundCue> cues = new ArrayList<>();
            for (JsonElement element : events) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject event = element.getAsJsonObject();
                if (!"minecraft:sound".equals(event.get("type").getAsString())) {
                    continue;
                }
                JsonObject data = event.getAsJsonObject("data");
                if (data == null || !data.has("sound")) {
                    continue;
                }
                float target = event.get("target").getAsFloat();
                ResourceLocation sound = ResourceLocation.tryParse(data.get("sound").getAsString());
                if (sound == null) {
                    continue;
                }
                float volume = data.has("volume") ? data.get("volume").getAsFloat() : 1f;
                float pitch = data.has("pitch") ? data.get("pitch").getAsFloat() : 1f;
                cues.add(new SoundCue(target, sound, volume, pitch));
            }
            cues.sort((a, b) -> Float.compare(a.normalizedTime, b.normalizedTime));
            return Collections.unmodifiableList(cues);
        } catch (Exception ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 读取换弹音效 {} 失败: {}", path, ex.toString());
            return List.of();
        }
    }
}
