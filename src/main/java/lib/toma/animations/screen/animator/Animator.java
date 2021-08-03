package lib.toma.animations.screen.animator;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import lib.toma.animations.AnimationCompatLayer;
import lib.toma.animations.pipeline.frame.IKeyframeProvider;
import lib.toma.animations.serialization.AnimationLoader;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class Animator {

    private AnimationProject project = AnimationProject.createEmpty();
    private final File exportDir = new File("./export/providers");
    private final Map<String, FrameProviderWrapper> configurables;

    public void onAnimationsLoaded(Map<ResourceLocation, IKeyframeProvider> providerMap, AnimationLoader.ILoader loader) {
        configurables.clear();
        loadModProviders(providerMap.entrySet());
        loadUserDefinedProviders(exportDir(), loader);
    }

    public void setUsingProject(AnimationProject project) {
        this.project = project;
        if (project == null) this.project = AnimationProject.createEmpty();
    }

    public AnimationProject getLatestProject() {
        return project;
    }

    public Collection<String> getPaths() {
        return configurables.keySet();
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
        Logger log = AnimationCompatLayer.logger;
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            IKeyframeProvider provider = loader.load(reader);
            if (provider == null)
                throw new JsonSyntaxException("Unable to parse");
            String name = file.getName().replaceFirst("[.][^.]+$", "");
            configurables.put(name, FrameProviderWrapper.userCreated(name, file.getParentFile(), provider));
        } catch (JsonParseException ex) {
            log.error("Exception parsing {} file: {}", file.getPath(), ex.getMessage());
        } catch (IOException ex) {
            log.fatal("Fatal exception ocurred while parsing {} file: {}", file.getPath(), ex.getMessage());
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

    private static final Animator INSTANCE = new Animator();

    private Animator() {
        configurables = new TreeMap<>(this::compareKeys);
    }

    private int compareKeys(String s1, String s2) {
        if (Objects.equals(s1, s2))
            return 0;
        boolean b1 = s1 != null && s1.contains(":");
        boolean b2 = s2 != null && s2.contains(":");
        return s1 != null && s2 != null && b1 == b2 ? s1.compareTo(s2) : b1 && !b2 ? 1 : -1;
    }

    public static Animator get() {
        return INSTANCE;
    }
}
