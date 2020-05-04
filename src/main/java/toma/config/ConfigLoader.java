package toma.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import toma.config.datatypes.ConfigObject;
import toma.config.util.ConfigUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ConfigLoader {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadFor(Side dist) {
        switch (dist) {
            case CLIENT: loadClient(); break;
            case SERVER: loadServer(); break;
            default: throw new IllegalArgumentException("Unknown side: " + dist);
        }
    }

    private static void loadClient() {
        Minecraft mc = Minecraft.getMinecraft();
        File configDir = new File(mc.mcDataDir, "config");
        if(!configDir.exists()) {
            return;
        }
        load(configDir);
    }

    private static void loadServer() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        File configDir = new File(server.getDataDirectory(), "config");
        if(!configDir.exists()) {
            return;
        }
        load(configDir);
    }

    private static void load(File configDir) {
        File[] configs = configDir.listFiles();
        try {
            for (Map.Entry<String, ConfigObject> entry : Config.CONFIGS.entrySet()) {
                Config.log.debug("Loading config for {} mod", entry.getKey());
                ConfigObject configObject = entry.getValue();
                String fileName = entry.getKey() + ".json";
                File modCfg = new File(configDir, fileName);
                boolean update = true;
                if(!ConfigUtils.findAndRun(fileName, configs, (mod, file) -> file.getName().equalsIgnoreCase(mod), file -> {})) {
                    try {
                        update = false;
                        modCfg.createNewFile();
                        JsonObject object = new JsonObject();
                        configObject.save(object);
                        FileWriter writer = new FileWriter(modCfg);
                        writer.write(GSON.toJson(object));
                        writer.close();
                    } catch (IOException e) {
                        Config.log.error("Couldn't create config file for {} mod!", entry.getKey());
                        continue;
                    }
                }
                if(update) {
                    String content = ConfigUtils.readFileToString(modCfg);
                    JsonObject object = new JsonParser().parse(content).getAsJsonObject();
                    try {
                        configObject.load(object);
                    } catch (Exception e) {
                        Config.log.fatal("Couldn't load config for {} mod, using defaults", entry.getKey());
                    }
                    JsonObject saved = new JsonObject();
                    configObject.save(saved);
                    FileWriter writer = new FileWriter(modCfg);
                    writer.write(GSON.toJson(saved));
                    writer.close();
                }
                Config.log.info("Loaded mod config [{}]", entry.getKey());
            }
        } catch (Exception e) {
            Config.log.fatal("Exception occurred during config load - {}", e.toString());
            Config.log.fatal("Shutting down...");
            e.printStackTrace();
            throw new IllegalStateException("Fatal error occurred during config load");
        }
    }
}
