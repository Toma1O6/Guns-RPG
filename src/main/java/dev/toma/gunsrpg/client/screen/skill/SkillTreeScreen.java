package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SkillTreeScreen<D extends IKillData> extends Screen {

    private static final ITextComponent TITLE = new TranslationTextComponent("screen.skill_tree");
    private IViewManager manager;

    public SkillTreeScreen(D data, Options options) {
        super(TITLE);
    }

    public static class Options {

        private IViewManager manager;

        public Options(IViewManager manager) {
            this.manager = manager;
        }
    }

    public static final class Cache {

        private static final Marker MARKER = MarkerManager.getMarker("SkillCache");
        private static CompletableFuture<Map<SkillCategory, TreeCollection<SkillType<?>>>> buildTask;

        public static void buildCache() {
            GunsRPG.log.info(MARKER, "Initializing skill placement cache");
            buildTask = CompletableFuture.supplyAsync(Cache::buildCacheAsync);
        }

        public static boolean dataReady() {
            return buildTask.isDone();
        }

        public static Map<SkillCategory, TreeCollection<SkillType<?>>> queryData() {
            if (buildTask == null) {
                GunsRPG.log.fatal(MARKER, "Build task hasn't started yet! Cannot query data.");
                return Collections.emptyMap();
            }
            try {
                return buildTask.get();
            } catch (ExecutionException | InterruptedException e) {
                GunsRPG.log.fatal(MARKER, "Cache building task failed: ", e);
                return Collections.emptyMap();
            }
        }

        private static Map<SkillCategory, TreeCollection<SkillType<?>>> buildCacheAsync() {
            Map<SkillCategory, TreeCollection<SkillType<?>>> result = new EnumMap<>(SkillCategory.class);
            Map<SkillCategory, List<SkillType<?>>> data = ModUtils.SKILLS_BY_CATEGORY.split(ModRegistries.SKILLS);
            return result;
        }
    }
}
