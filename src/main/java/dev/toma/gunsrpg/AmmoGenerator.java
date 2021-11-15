package dev.toma.gunsrpg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AmmoGenerator {

    static final String[] MATERIALS = {
            "wood", "stone", "iron", "lapis", "gold",
            "redstone", "diamond", "quartz",
            "emerald", "amethyst", "netherite"
    };

    static final String[] CALIBERS = {
            //"9mm", "45acp", "556mm", "762mm", "bolt"
            "12g"
    };

    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void main(String[] args) {
        File exportDir = new File("./export");
        try {
            exportDir.mkdirs();
            generateModelFiles(new File(exportDir, "ammoModels"));
            //generateTranslationMap(new File(exportDir, "laguageMap.txt"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static void generateModelFiles(File export) throws IOException {
        export.mkdirs();
        for (String material : MATERIALS) {
            for (String caliber : CALIBERS) {
                String regName = material + "_" + caliber;
                generateModelFile(regName, export);
            }
        }
    }

    private static void generateTranslationMap(File export) throws IOException {
        export.createNewFile();
        try (FileWriter writer = new FileWriter(export)) {
            for (String material : MATERIALS) {
                for (String caliber : CALIBERS) {
                    boolean isBolt = caliber.equals("bolt");
                    String descriptor = isBolt ? "Bolt" : "Ammo";
                    String translation = String.format("%s %s", upCase(material), descriptor);
                    if (!isBolt) {
                        translation = translation + " (" + caliber + ")";
                    }
                    writer.write("\"item.gunsrpg." + material + "_" + caliber + "\": \"" + translation + "\"\n");
                }
            }
        }
    }

    private static void generateModelFile(String regName, File dir) throws IOException {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "gunsrpg:item/" + regName);
        root.add("textures", textures);

        File file = new File(dir, regName + ".json");
        file.createNewFile();

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(root, writer);
        }
    }

    private static String upCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
