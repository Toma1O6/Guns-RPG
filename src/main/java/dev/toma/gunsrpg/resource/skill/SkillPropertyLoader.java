package dev.toma.gunsrpg.resource.skill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.resource.adapter.SkillPropertyAdapter;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Map;

public final class SkillPropertyLoader extends JsonReloadListener {

    private static final Marker MARKER = MarkerManager.getMarker("SkillPropertyLoader");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Properties.class, new SkillPropertyAdapter())
            .create();

    public SkillPropertyLoader() {
        super(GSON, "skillProperties");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceMap, IResourceManager resourceManager, IProfiler profiler) {
        try {
            IForgeRegistry<SkillType<?>> skillRegistry = ModRegistries.SKILLS;
            for (Map.Entry<ResourceLocation, JsonElement> entry : resourceMap.entrySet()) {
                ResourceLocation skillKey = entry.getKey();
                SkillType<?> context = skillRegistry.getValue(skillKey);
                if (context == null) {
                    GunsRPG.log.error(MARKER, "Found invalid skill entry in data, skill does not exist: {}", skillKey);
                    continue;
                }
                try {
                    Properties properties = GSON.fromJson(entry.getValue(), Properties.class);
                    context.onDataAssign(properties);
                } catch (JsonParseException jpe) {
                    GunsRPG.log.error(MARKER, "Unable to load skill data for skill {}, error occurred {}", skillKey, jpe);
                }
            }
        } catch (Exception e) {
            GunsRPG.log.fatal(MARKER, "Fatal error occurred during skill property loading, aborting... ", e);
        }
    }

    public static class Properties {

        private final int level;
        private final int price;
        private final SkillCategory category;
        private final SkillType<?>[] children;

        public Properties(int level, int price, SkillCategory category, SkillType<?>[] children) {
            this.level = level;
            this.price = price;
            this.category = category;
            this.children = children;
        }

        public int getLevel() {
            return level;
        }

        public int getPrice() {
            return price;
        }

        public SkillCategory getCategory() {
            return category;
        }

        public SkillType<?>[] getChildren() {
            return children;
        }
    }
}
