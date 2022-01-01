package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkillTreeScreen extends Screen {

    private static final ITextComponent TITLE = new TranslationTextComponent("screen.skill_tree");
    private final Options usedOptions;
    private IViewManager manager;

    public SkillTreeScreen() {
        this(new Options(new IViewManager.ViewManager()));
    }

    public SkillTreeScreen(Options options) {
        super(TITLE);
        this.usedOptions = options;
        this.manager = options.manager;
        this.queryCache();
    }

    @Override
    protected void init() {
        addWidget(manager.getActive());
    }

    @Override
    public void tick() {
        manager.getActive().tick();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        manager.getActive().render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void queryCache() {
        if (Cache.hasBeenBuilt()) {
            updateView();
            return;
        }
        CompletableFuture
                .supplyAsync(Cache::buildCacheAsync)
                .exceptionally(Cache::onBuildFailed)
                .thenAccept(this::onSkillTreeReady);
    }

    private void updateView() {

    }

    private void onSkillTreeReady(Map<SkillCategory, SkillTrees> result) {
        Cache.onBuildFinished(result);
    }

    public static class Options {

        private IViewManager manager;

        public Options(IViewManager manager) {
            this.manager = manager;
            this.manager.init(IViewFactory.LOADING_VIEW_SUPPLIER.get());
        }
    }

    public static final class Cache {

        private static final Marker MARKER = MarkerManager.getMarker("SkillCache");
        private static Map<SkillCategory, SkillTrees> treeMap;

        public static boolean hasBeenBuilt() {
            return treeMap != null;
        }

        public static Map<SkillCategory, SkillTrees> queryData() {
            return treeMap;
        }

        private static Map<SkillCategory, SkillTrees> buildCacheAsync() {
            GunsRPG.log.info("Starting skill placement cache build.");
            long time = System.currentTimeMillis();
            Map<SkillCategory, SkillTrees> result = new EnumMap<>(SkillCategory.class);
            Map<SkillCategory, List<SkillType<?>>> data = ModUtils.SKILLS_BY_CATEGORY.split(ModRegistries.SKILLS);
            for (Map.Entry<SkillCategory, List<SkillType<?>>> entry : data.entrySet()) {
                SkillCategory category = entry.getKey();
                List<SkillType<?>> skillTypes = entry.getValue();
                SkillTrees trees = new SkillTrees(category);
                trees.populate(skillTypes);
                result.put(category, trees);
            }
            GunsRPG.log.info("Skill tree cache has been built after {} ms", System.currentTimeMillis() - time);
            return result;
        }

        private static Map<SkillCategory, SkillTrees> onBuildFailed(Throwable throwable) {
            GunsRPG.log.fatal(MARKER, "Skill placement cache build failed, no skills can be displayed. ", throwable);
            return null;
        }

        private static void onBuildFinished(Map<SkillCategory, SkillTrees> result) {
            treeMap = result;
        }
    }
}
