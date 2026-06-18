package com.wf.firearms.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SkillNode {
    private final String id;
    private final String category;
    private final String parent;
    private final List<String> children;
    private final List<String> extensions;
    private final int level;
    private final int price;
    private final String validatorType;
    /** gunsrpg:weapon 时为枪 ID 字符串；少数节点可能为 JSON 对象 */
    private final String validatorData;

    public SkillNode(String id, JsonObject root) {
        this.id = id;
        JsonObject hierarchy = root.getAsJsonObject("hierarchy");
        JsonObject properties = root.has("properties") ? root.getAsJsonObject("properties") : new JsonObject();

        this.category = hierarchy.has("category") ? hierarchy.get("category").getAsString() : "";
        this.parent = stripId(hierarchy, "parent");
        this.children = readIdList(hierarchy, "children");
        this.extensions = readIdList(hierarchy, "extensions");

        this.level = properties.has("level") ? properties.get("level").getAsInt() : 0;
        this.price = properties.has("price") ? properties.get("price").getAsInt() : 0;

        if (properties.has("transactionValidator")) {
            JsonObject tv = properties.getAsJsonObject("transactionValidator");
            this.validatorType = tv.has("type") ? tv.get("type").getAsString() : "";
            this.validatorData = readValidatorData(tv);
        } else {
            this.validatorType = "";
            this.validatorData = "";
        }
    }

    private static String readValidatorData(JsonObject tv) {
        if (!tv.has("data") || tv.get("data").isJsonNull()) {
            return "";
        }
        JsonElement data = tv.get("data");
        if (data.isJsonPrimitive()) {
            return stripNamespace(data.getAsString());
        }
        if (data.isJsonObject() || data.isJsonArray()) {
            return data.toString();
        }
        return data.toString();
    }

    private static String stripId(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return "";
        }
        return stripNamespace(obj.get(key).getAsString());
    }

    private static List<String> readIdList(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return List.of();
        }
        JsonArray arr = obj.getAsJsonArray(key);
        List<String> out = new ArrayList<>(arr.size());
        for (JsonElement el : arr) {
            out.add(stripNamespace(el.getAsString()));
        }
        return Collections.unmodifiableList(out);
    }

    public static String stripNamespace(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        int colon = raw.indexOf(':');
        return colon >= 0 ? raw.substring(colon + 1) : raw;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getParent() {
        return parent;
    }

    public List<String> getChildren() {
        return children;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public String getValidatorType() {
        return validatorType;
    }

    public String getValidatorData() {
        return validatorData;
    }

    public boolean isAssembly() {
        return id.endsWith("_assembly");
    }
}
