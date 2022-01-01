package lib.toma.animations.engine.screen.animator;

import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.engine.frame.MutableKeyframe;

import java.io.File;
import java.util.List;
import java.util.Map;

public final class FrameProviderWrapper {

    private final String name;
    private final File directory;
    private final AnimatorFrameProvider provider;

    private FrameProviderWrapper(String name, File directory, IKeyframeProvider provider) {
        this.name = name;
        this.directory = directory;
        this.provider = new AnimatorFrameProvider(provider);
    }

    private FrameProviderWrapper(String name, File directory, Map<AnimationStage, List<MutableKeyframe>> frames, IAnimationEvent... events) {
        this.name = name;
        this.directory = directory;
        this.provider = new AnimatorFrameProvider(frames, events);
    }

    public static FrameProviderWrapper modded(String name, IKeyframeProvider provider) {
        return new FrameProviderWrapper(name, new File("./export/providers"), provider);
    }

    public static FrameProviderWrapper userCreated(String name, File dir, IKeyframeProvider provider) {
        return new FrameProviderWrapper(name, dir, provider);
    }

    public FrameProviderWrapper deepCopy() {
        return new FrameProviderWrapper(name, directory, provider.getFrames(), provider.getEvents());
    }

    public String getName() {
        return name;
    }

    public File getWorkingDirectory() {
        return directory;
    }

    public AnimatorFrameProvider getProvider() {
        return provider;
    }

    public boolean hasEventSupport() {
        return provider.canAddEvents();
    }

    public void merge(FrameProviderWrapper wrapper, float mergeStart, float mergeEnd) {
        provider.merge(wrapper.provider, mergeStart, mergeEnd);
    }
}
