package com.wf.firearms.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class SkillDatabase {
    private static final Map<String, SkillNode> NODES = new HashMap<>();
    private static final Map<String, PerkDef> PERKS = new HashMap<>();
    private static final Map<String, List<String>> ROOTS_BY_CATEGORY = new LinkedHashMap<>();
    private static final Map<String, List<String>> ASSEMBLIES = new HashMap<>();
    private static String loadError = "";
    private static boolean loaded;

    private SkillDatabase() {}

    public static void reload() {
        NODES.clear();
        PERKS.clear();
        ROOTS_BY_CATEGORY.clear();
        ASSEMBLIES.clear();
        loadError = "";
        loaded = false;

        if (!PortPaths.isPortPresent()) {
            loadError = "缺少 config/gunsrpg/port_from_gunsrpg/（请先运行数据导出）";
            GunsRpg.LOGGER.warn("[gunsrpg] {}", loadError);
            return;
        }

        try {
            loadIndex();
            loadSkillProperties();
            loadExtraSkills();
            indexAssembliesFromNodes();
            loadPerks();
            loaded = true;
            GunsRpg.LOGGER.info(
                    "[gunsrpg] 已加载技能节点 {}、天赋 {}、装配体 {}",
                    NODES.size(),
                    PERKS.size(),
                    ASSEMBLIES.size());
        } catch (Exception e) {
            loadError = e.getMessage() != null ? e.getMessage() : e.toString();
            GunsRpg.LOGGER.error("[gunsrpg] 加载失败", e);
        }
    }

    private static void loadIndex() throws IOException {
        try (Reader reader = Files.newBufferedReader(PortPaths.skillIndexFile(), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("roots_by_category")) {
                JsonObject roots = root.getAsJsonObject("roots_by_category");
                for (Map.Entry<String, JsonElement> e : roots.entrySet()) {
                    List<String> filteredRoots = new ArrayList<>();
                    for (JsonElement el : e.getValue().getAsJsonArray()) {
                        String id = el.getAsString();
                        if (SkillCatalog.isEnabled(id)) {
                            filteredRoots.add(id);
                        }
                    }
                    ROOTS_BY_CATEGORY.put(e.getKey(), Collections.unmodifiableList(filteredRoots));
                }
            }
            if (root.has("assemblies")) {
                JsonObject asm = root.getAsJsonObject("assemblies");
                for (Map.Entry<String, JsonElement> e : asm.entrySet()) {
                    ASSEMBLIES.put(e.getKey(), readStringArray(e.getValue().getAsJsonArray()));
                }
            }
        }
    }

    private static void loadSkillProperties() throws IOException {
        Path dir = PortPaths.skillPropertiesDir();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                String id = file.getFileName().toString().replace(".json", "");
                if (!SkillCatalog.isEnabled(id)) {
                    continue;
                }
                try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    NODES.put(id, new SkillNode(id, obj));
                }
            }
        }
    }

    /** 整合包追加节点（如 50 级加特林），放在 config/gunsrpg/extra_skills/*.json */
    private static void loadExtraSkills() throws IOException {
        Path dir = PortPaths.configRoot().resolve("extra_skills");
        if (!Files.isDirectory(dir)) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                String id = file.getFileName().toString().replace(".json", "");
                try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    NODES.put(id, new SkillNode(id, obj));
                }
            }
        }
    }

    /** extra_skills 中的装配体未写入 skill_index，需从节点 hierarchy.extensions 建索引。 */
    private static void indexAssembliesFromNodes() {
        for (SkillNode node : NODES.values()) {
            if (!node.isAssembly() || !SkillCatalog.isEnabled(node.getId())) {
                continue;
            }
            List<String> fromNode = node.getExtensions();
            if (!fromNode.isEmpty()) {
                ASSEMBLIES.put(node.getId(), fromNode);
            } else if (!ASSEMBLIES.containsKey(node.getId())) {
                ASSEMBLIES.put(node.getId(), List.of());
            }
        }
    }

    private static void loadPerks() throws IOException {
        Path dir = PortPaths.perksDir();
        if (!Files.isDirectory(dir)) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                String id = file.getFileName().toString().replace(".json", "");
                if (!PerkCatalog.isEnabled(id)) {
                    continue;
                }
                try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    PERKS.put(id, new PerkDef(id, obj));
                }
            }
        }
    }

    private static List<String> readStringArray(JsonArray arr) {
        List<String> out = new ArrayList<>(arr.size());
        for (JsonElement el : arr) {
            out.add(el.getAsString());
        }
        return Collections.unmodifiableList(out);
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static String getLoadError() {
        return loadError;
    }

    public static Optional<SkillNode> getNode(String id) {
        return Optional.ofNullable(NODES.get(id));
    }

    public static Map<String, SkillNode> getAllNodes() {
        return Collections.unmodifiableMap(NODES);
    }

    public static Map<String, PerkDef> getAllPerks() {
        return Collections.unmodifiableMap(PERKS);
    }

    public static Optional<PerkDef> getPerk(String id) {
        return Optional.ofNullable(PERKS.get(id));
    }

    public static Map<String, List<String>> getRootsByCategory() {
        return Collections.unmodifiableMap(ROOTS_BY_CATEGORY);
    }

    public static Map<String, List<String>> getAssemblies() {
        return Collections.unmodifiableMap(ASSEMBLIES);
    }

    public static List<String> getAssemblyIds() {
        List<String> ids = new ArrayList<>();
        for (String id : ASSEMBLIES.keySet()) {
            if (!id.endsWith("_assembly") || !SkillCatalog.isEnabled(id)) {
                continue;
            }
            if (!ActiveWeaponCatalog.isActiveAssembly(id)) {
                continue;
            }
            ids.add(id);
        }
        Collections.sort(ids);
        return ids;
    }
}
