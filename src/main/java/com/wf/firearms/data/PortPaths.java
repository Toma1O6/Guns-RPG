package com.wf.firearms.data;

import com.wf.firearms.GunsRpg;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;

public final class PortPaths {
    private PortPaths() {}

    public static Path configRoot() {
        return FMLPaths.CONFIGDIR.get().resolve("gunsrpg");
    }

    public static Path portRoot() {
        return configRoot().resolve("port_from_gunsrpg");
    }

    public static Path skillPropertiesDir() {
        return portRoot().resolve("skill_properties");
    }

    public static Path perksDir() {
        return portRoot().resolve("perks");
    }

    public static Path skillIndexFile() {
        return portRoot().resolve("skill_index.json");
    }

    public static Path levelingStrategyFile() {
        return portRoot().resolve("leveling_strategy.json");
    }

    public static boolean isPortPresent() {
        return Files.isDirectory(skillPropertiesDir()) && Files.isRegularFile(skillIndexFile());
    }
}
