package toma.config.util;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.ConfigLoader;
import toma.config.datatypes.ConfigObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class ConfigUtils {

    @SideOnly(Side.CLIENT)
    public static void c_saveConfig(String modid, ConfigObject object) {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "config" + File.separator + modid + ".json");
        if(!file.exists()) {
            throw new IllegalStateException("Couldn't locate config file!");
        }
        JsonObject json = new JsonObject();
        object.save(json);
        object.load(json);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(ConfigLoader.GSON.toJson(json));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileToString(File file) {
        StringBuilder builder = new StringBuilder();
        try(Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
            stream.forEach(string -> builder.append(string).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static <A, B> boolean isPresent(A a, B[] bs, BiPredicate<A, B> comparing) {
        for(B b : bs) {
            if(comparing.test(a, b)) {
                return true;
            }
        }
        return false;
    }

    public static <A, B> boolean findAndRun(A a, B[] bs, BiPredicate<A, B> func, Consumer<B> ac) {
        for(B b : bs) {
            if(func.test(a, b)) {
                ac.accept(b);
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> makeList(T... values) {
        List<T> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        return list;
    }

    public static <A, B> List<A> asList(List<B> list, Function<B, A> valueGetter) {
        List<A> aList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            B b = list.get(i);
            aList.add(valueGetter.apply(b));
        }
        return aList;
    }
}
