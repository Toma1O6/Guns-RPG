package lib.toma.animations.screen.animator;

import lib.toma.animations.AnimationEngine;
import lib.toma.animations.pipeline.AnimationType;
import lib.toma.animations.pipeline.IAnimationPipeline;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframeProvider;
import lib.toma.animations.pipeline.frame.KeyframeProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AnimationProject {

    private final AnimationController controller = new AnimationController();
    private final String name;
    private FrameProviderWrapper wrapper;
    private File projectDirectory;
    private boolean isSaved = true;

    public AnimationProject(FrameProviderWrapper wrapper) {
        this.name = wrapper.getName();
        this.projectDirectory = wrapper.getWorkingDirectory();
        this.wrapper = wrapper;
    }

    public AnimationProject(String projectName, int animationLength) {
        this.name = projectName;
        this.controller.setAnimationTime(animationLength);
        this.wrapper = FrameProviderWrapper.userCreated(projectName, new File("./export/providers"), new KeyframeProvider(new HashMap<>(), IAnimationEvent.NO_EVENTS));
    }

    public static AnimationProject createEmpty() {
        return new AnimationProject(null, 50);
    }

    public AnimationController getAnimationControl() {
        return controller;
    }

    public FrameProviderWrapper getFrameControl() {
        return wrapper;
    }

    public boolean isNamed() {
        return name != null && !name.isEmpty();
    }

    public boolean isSaved() {
        return isSaved;
    }

    public boolean hasEvents() {
        return wrapper.hasEventSupport();
    }

    public void saveProject() {
        if (name == null)
            return;
        try {
            File file = new File(projectDirectory, name + ".json");
            if (file.exists()) {
                saveToDisk(file);
            }
        } catch (IOException exc) {
            //
        }
        isSaved = true;
    }

    public void saveProjectAs(String name) {
        try {
            if (!projectDirectory.exists())
                projectDirectory.mkdirs();
            File file = new File(projectDirectory, name + ".json");
            if (!file.exists())
                file.createNewFile();
            saveToDisk(file);
        } catch (IOException exc) {
            //
        }
        isSaved = true;
    }

    private void saveToDisk(File file) throws IOException {
        IKeyframeProvider serializable = wrapper.getProvider().toSerializable();
        String out = AnimationEngine.get().loader().serializeToString(serializable);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(out);
        }
    }

    public static class AnimationController {

        private int animationTime = 50;
        private boolean isOnRepeat = false;
        private boolean paused = true;

        public void setAnimationTime(int animationTime) {
            this.animationTime = animationTime;
            IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
            AnimationType<Animator.CustomizableAnimation> type = Animator.ANIMATOR_TYPE;
            Animator.CustomizableAnimation animation = pipeline.get(type);
            if (animation != null) {
                animation.setTickLength(animationTime);
            }
        }

        public int getAnimationTime() {
            return animationTime;
        }

        public void setOnRepeat(boolean onRepeat) {
            isOnRepeat = onRepeat;
        }

        public boolean isOnRepeat() {
            return isOnRepeat;
        }

        public void setPaused(boolean paused) {
            this.paused = paused;
        }

        public boolean isPaused() {
            return paused;
        }
    }
}
