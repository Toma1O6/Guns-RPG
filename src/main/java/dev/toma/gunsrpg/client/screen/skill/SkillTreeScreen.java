package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkillTreeScreen extends Screen implements IViewContext {

    private static final ITextComponent TITLE = new TranslationTextComponent("screen.skill_tree");
    private final IViewManager manager;
    private IPlayerData data;

    public SkillTreeScreen() {
        super(TITLE);
        this.manager = this.new ViewManager();
        this.queryCache();
    }

    @Override
    protected void init() {
        this.data = PlayerData.get(minecraft.player).orElseThrow(NullPointerException::new);
        MainWindow window = minecraft.getWindow();
        View view = manager.getView();
        if (view == null) {
            view = new LoadingView(window, this::checkLoaded, this::onLoadCorrection);
            manager.setView(view);
        }
        addWidget(view);
        view.resizeFor(window);
        synchronized (view.lock) {
            view.init();
        }
    }

    @Override
    public IPlayerData getData() {
        return data;
    }

    @Override
    public void tick() {
        manager.getView().tick();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        manager.getView().render(matrixStack, mouseX, mouseY, partialTicks);
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

    private boolean checkLoaded() {
        return Cache.hasBeenBuilt();
    }

    private void onLoadCorrection() {
        updateView();
    }

    private void updateView() {
        MainWindow window = Minecraft.getInstance().getWindow();
        setView(new SkillsView(window.getGuiScaledWidth(), window.getGuiScaledHeight(), manager));
    }

    private void onSkillTreeReady(Map<SkillCategory, SkillTrees> result) {
        Cache.onBuildFinished(result);
        updateView();
    }

    private void setView(View view) {
        this.manager.setView(view);
        Minecraft mc = Minecraft.getInstance();
        MainWindow window = mc.getWindow();
        this.init(mc, window.getGuiScaledWidth(), window.getGuiScaledHeight());
    }

    public class ViewManager implements IViewManager {

        private View view;

        @Override
        public IViewContext getContext() {
            return SkillTreeScreen.this;
        }

        @Override
        public void setView(View view) {
            this.view = view;
        }

        @Override
        public View getView() {
            return view;
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
            GunsRPG.log.debug(MARKER, "Starting skill placement cache build.");
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
            GunsRPG.log.debug(MARKER, "Skill tree cache has been built after {} ms", System.currentTimeMillis() - time);
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
