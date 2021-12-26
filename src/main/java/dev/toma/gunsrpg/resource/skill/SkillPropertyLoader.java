package dev.toma.gunsrpg.resource.skill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillDisplay;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.resource.adapter.SkillDataAdapter;
import dev.toma.gunsrpg.resource.adapter.SkillHierarchyAdapter;
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
            .registerTypeHierarchyAdapter(ILoadResult.class, new SkillDataAdapter())
            .registerTypeHierarchyAdapter(ISkillHierarchy.class, new SkillHierarchyAdapter())
            .registerTypeHierarchyAdapter(ISkillProperties.class, new SkillPropertyAdapter())
            .create();

    public SkillPropertyLoader() {
        super(GSON, "skill_properties");
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
                finishLoading(skillKey, context, entry.getValue());
            }
        } catch (Exception e) {
            GunsRPG.log.fatal(MARKER, "Fatal error occurred during skill property loading, aborting... ", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <S extends ISkill> void finishLoading(ResourceLocation skillKey, SkillType<S> context, JsonElement data) {
        try {
            ILoadResult<S> result = GSON.fromJson(data, ILoadResult.class);
            context.onDataAssign(result);
        } catch (JsonParseException jpe) {
            GunsRPG.log.error(MARKER, "Unable to load skill data for skill {}, error occurred {}", skillKey, jpe);
        }
    }

    public interface ILoadResult<S extends ISkill> {

        ISkillHierarchy<S> hierarchy();

        ISkillProperties properties();

        ISkillDisplay display();
    }

    public static class Result<S extends ISkill> implements ILoadResult<S> {

        private final ISkillHierarchy<S> hierarchy;
        private final ISkillProperties properties;
        private final ISkillDisplay display;

        public Result(ISkillHierarchy<S> hierarchy, ISkillProperties properties, ISkillDisplay display) {
            this.hierarchy = hierarchy;
            this.properties = properties;
            this.display = display;
        }

        @Override
        public ISkillHierarchy<S> hierarchy() {
            return hierarchy;
        }

        @Override
        public ISkillProperties properties() {
            return properties;
        }

        @Override
        public ISkillDisplay display() {
            return display;
        }
    }
}
