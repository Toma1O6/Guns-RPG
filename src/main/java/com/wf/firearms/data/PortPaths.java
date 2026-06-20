package com.wf.firearms.data;

import com.wf.firearms.GunsRpg;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.locating.IModFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class PortPaths {
    private PortPaths() {}

    public static Path configRoot() {
        return FMLPaths.CONFIGDIR.get().resolve("gunsrpg");
    }

    public static Path portRoot() {
        return configRoot().resolve("port_from_gunsrpg");
    }

    /** Jar 内 {@code data/gunsrpg/}（默认技能/天赋数据）。 */
    public static Optional<Path> bundledDataRoot() {
        return ModList.get()
                .getModContainerById(GunsRpg.MOD_ID)
                .map(c -> c.getModInfo().getOwningFile().getFile())
                .flatMap(PortPaths::resolveBundledDataRoot);
    }

    private static Optional<Path> resolveBundledDataRoot(IModFile modFile) {
        Path index = modFile.findResource("data/gunsrpg/skill_index.json");
        if (index == null) {
            return Optional.empty();
        }
        Path parent = index.getParent();
        if (parent == null || !Files.isDirectory(parent)) {
            return Optional.empty();
        }
        return Optional.of(parent);
    }

    private static Path resolveDataRoot() {
        return bundledDataRoot().orElse(portRoot());
    }

    public static Path skillPropertiesDir() {
        return resolveDataRoot().resolve("skill_properties");
    }

    public static Path perksDir() {
        return resolveDataRoot().resolve("perks");
    }

    public static Path skillIndexFile() {
        return resolveDataRoot().resolve("skill_index.json");
    }

    public static Path levelingStrategyFile() {
        return resolveDataRoot().resolve("leveling_strategy.json");
    }

    public static boolean isPortPresent() {
        return Files.isRegularFile(skillIndexFile()) && Files.isDirectory(skillPropertiesDir());
    }
}
