package lib.toma.animations.screen.animator;

import lib.toma.animations.pipeline.frame.IKeyframeProvider;

import java.io.File;

public final class FrameProviderWrapper {

    private final String name;
    private final File directory;
    private final AnimatorFrameProvider provider;

    private FrameProviderWrapper(String name, File directory, IKeyframeProvider provider) {
        this.name = name;
        this.directory = directory;
        this.provider = new AnimatorFrameProvider(provider);
    }

    public static FrameProviderWrapper modded(String name, IKeyframeProvider provider) {
        return new FrameProviderWrapper(name, new File("./export/providers"), provider);
    }

    public static FrameProviderWrapper userCreated(String name, File dir, IKeyframeProvider provider) {
        return new FrameProviderWrapper(name, dir, provider);
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
}
