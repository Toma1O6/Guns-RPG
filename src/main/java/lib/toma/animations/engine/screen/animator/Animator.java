package lib.toma.animations.engine.screen.animator;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.*;
import lib.toma.animations.engine.frame.MutableKeyframe;
import lib.toma.animations.engine.serialization.AnimationLoader;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public final class Animator {

    private static final Animator INSTANCE = new Animator();
    public static final AnimationType<CustomizableAnimation> ANIMATOR_TYPE = AnimationType.create(new ResourceLocation("animator"), Animator.get()::getAnimation);
    public static final AnimationType<BackgroundAnimation> BACKGROUND_TYPE = AnimationType.create(new ResourceLocation("background"));
    private final File exportDir = new File("./export/providers");
    private final Map<String, FrameProviderWrapper> configurables;
    private AnimationProject project = AnimationProject.createEmpty();

    private Animator() {
        configurables = new TreeMap<>(this::compareKeys);
    }

    public static Animator get() {
        return INSTANCE;
    }

    public void refreshUserAnimations() {
        List<String> collect = configurables.keySet().stream().filter(s -> !s.contains(":")).collect(Collectors.toList());
        collect.forEach(configurables::remove);
        loadUserDefinedProviders(exportDir(), AnimationEngine.get().loader().getResourceReader());
    }

    public void onAnimationsLoaded(Map<ResourceLocation, IKeyframeProvider> providerMap, AnimationLoader.ILoader loader) {
        configurables.clear();
        loadModProviders(providerMap.entrySet());
        loadUserDefinedProviders(exportDir(), loader);
    }

    public void setUsingProject(AnimationProject project) {
        this.project = project;
        if (project == null) this.project = AnimationProject.createEmpty();
    }

    public AnimationProject getProject() {
        return project;
    }

    public Collection<String> getPaths() {
        return configurables.keySet();
    }

    public FrameProviderWrapper getWrapper(String path) {
        FrameProviderWrapper wrapper = configurables.get(path);
        return wrapper.deepCopy();
    }

    private void loadModProviders(Set<Map.Entry<ResourceLocation, IKeyframeProvider>> set) {
        set.forEach(entry -> {
            String name = entry.getKey().toString();
            IKeyframeProvider provider = entry.getValue();
            configurables.put(name, FrameProviderWrapper.modded(name, provider));
        });
    }

    private void loadUserDefinedProviders(File expDir, AnimationLoader.ILoader loader) {
        if (!expDir.isDirectory())
            return;
        for (File file : expDir.listFiles(this::isValidProviderFile)) {
            loadUserDefinedProviderFile(file, loader);
        }
    }

    private void loadUserDefinedProviderFile(File file, AnimationLoader.ILoader loader) {
        Logger log = AnimationEngine.logger;
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            IKeyframeProvider provider = loader.loadResource(reader);
            if (provider == null)
                throw new JsonSyntaxException("Unable to parse");
            String name = file.getName().replaceFirst("[.][^.]+$", "");
            configurables.put(name, FrameProviderWrapper.userCreated(name, file.getParentFile(), provider));
        } catch (JsonParseException ex) {
            log.error(AnimationLoader.MARKER, "Exception parsing {} file: {}", file.getPath(), ex.getMessage());
        } catch (IOException ex) {
            log.fatal(AnimationLoader.MARKER, "Fatal exception ocurred while parsing {} file: {}", file.getPath(), ex.getMessage());
        }
    }

    private boolean isValidProviderFile(File pathname) {
        return getExtension(pathname.getName()).equals("json");
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return index == -1 ? "" : filename.substring(index + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File exportDir() {
        if (!exportDir.exists())
            exportDir.mkdirs();
        return exportDir;
    }

    private int compareKeys(String s1, String s2) {
        if (Objects.equals(s1, s2))
            return 0;
        boolean b1 = s1 != null && s1.contains(":");
        boolean b2 = s2 != null && s2.contains(":");
        return s1 != null && s2 != null && b1 == b2 ? s1.compareTo(s2) : b1 && !b2 ? 1 : -1;
    }

    private CustomizableAnimation getAnimation(PlayerEntity client) {
        return new CustomizableAnimation();
    }

    public static class CustomizableAnimation implements IAnimation {

        private final BooleanSupplier isPausedSupp = () -> Animator.get().getProject().getAnimationControl().isPaused();
        private final BooleanSupplier isOnRepeat = () -> Animator.get().getProject().getAnimationControl().isOnRepeat();
        private final AnimatorFrameProvider provider;
        private int totalTicks;
        private int remainingTicks;
        private float progress, progressO, progressI;
        private boolean removalMarker;

        public CustomizableAnimation() {
            Animator animator = Animator.get();
            AnimationProject project = animator.getProject();
            setTickLength(project.getAnimationControl().getAnimationTime());
            provider = project.getFrameControl().getProvider();
        }

        @Override
        public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
            if (provider.blocksStageAnimation(stage)) return;
            IKeyframe kf = provider.getActualFrame(stage, progressI);
            IKeyframe kfO = provider.getLastFrame(stage);
            float ep = kf.endpoint();
            float epO = kfO.endpoint();
            float min = ep - epO;
            float sProgress = min == 0.0F ? 1.0F : (progressI - epO) / (ep - epO);
            Keyframes.processFrame(kf, sProgress, matrixStack);
        }

        @Override
        public void renderTick(float deltaRenderTime) {
            float prev = progressI;
            if (!isPausedSupp.getAsBoolean()) {
                progressI = Math.min(progressO + (progress - progressO) * deltaRenderTime, provider.getAnimationEnd());
                advance(progressI, prev);
            }
        }

        @Override
        public void gameTick() {
            if (!isPausedSupp.getAsBoolean()) {
                int next = remainingTicks - 1;
                boolean canRepeat = isOnRepeat.getAsBoolean();
                if (next < 0) {
                    if (canRepeat) {
                        resetState();
                    }
                } else {
                    remainingTicks = next;
                }
            }
            progressO = progress;
            progress = getRawProgress();
        }

        @Override
        public boolean hasFinished() {
            return removalMarker;
        }

        public void setProgress(float value) {
            this.progress = value;
            this.progressO = value;
            this.progressI = value;
            this.remainingTicks = totalTicks - (int) (totalTicks * value);
            this.provider.forceProgress(value);
        }

        public void setTickLength(int length) {
            this.totalTicks = length;
            this.remainingTicks = length;
        }

        public float getProgress() {
            return progressI;
        }

        public void remove() {
            removalMarker = true;
        }

        private void advance(float actual, float old) {
            provider.onProgressed(actual, old, this);
        }

        private float getRawProgress() {
            return isPausedSupp.getAsBoolean() ? progress : 1.0F - ((float) remainingTicks / totalTicks);
        }

        private void resetState() {
            remainingTicks = totalTicks;
            progress = 0.0F;
            progressO = 0.0F;
            progressI = 0.0F;
            provider.forceProgress(0.0F);
        }
    }

    public static class BackgroundAnimation implements IAnimation {

        private final Map<AnimationStage, IKeyframe> map = new IdentityHashMap<>();

        public BackgroundAnimation(IKeyframeProvider provider) {
            if (!(provider instanceof AnimatorFrameProvider)) {
                provider = new AnimatorFrameProvider(provider);
            }
            AnimatorFrameProvider frameProvider = (AnimatorFrameProvider) provider;
            for (Map.Entry<AnimationStage, List<MutableKeyframe>> entry : frameProvider.getFrames().entrySet()) {
                map.put(entry.getKey(), this.mergeKeyframesIntoOne(entry.getValue()));
            }
        }

        @Override
        public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
            IKeyframe keyframe = map.get(stage);
            if (keyframe == null) return;
            Keyframes.processFrame(keyframe, 1.0F, matrixStack);
        }

        @Override
        public void gameTick() {}

        @Override
        public void renderTick(float deltaRenderTime) {}

        @Override
        public boolean hasFinished() {
            IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
            return !pipeline.has(Animator.ANIMATOR_TYPE);
        }

        private IKeyframe mergeKeyframesIntoOne(List<? extends IKeyframe> iKeyframes) {
            return MutableKeyframe.fullCopyOf(iKeyframes.get(iKeyframes.size() - 1));
        }
    }
}
