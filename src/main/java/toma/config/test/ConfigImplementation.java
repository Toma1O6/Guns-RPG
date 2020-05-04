package toma.config.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import toma.config.IConfig;
import toma.config.datatypes.ConfigObject;
import toma.config.object.builder.ConfigBuilder;
import toma.config.util.ConfigUtils;

import java.util.List;

public class ConfigImplementation implements IConfig {

    public static boolean bool = true;
    public static byte aByte = 12;
    public static short aShort = 1234;
    public static int anInt = 12345;
    public static long aLong = 123456;
    public static float aFloat = 123456.7F;
    public static double aDouble = 1234567.8D;
    public static String aString = "Hello world";
    public static List<String> stringList = ConfigUtils.makeList("T", "o", "m", "a");
    /** Non-primitive types cannot be edited in GUI !! yet */
    public static List<Obj> objList = ConfigUtils.makeList(new Obj(13), new Obj(18));
    public static Obj obj = new Obj();
    public static TestEnum testEnum = TestEnum.B;

    @Override
    public ConfigObject getConfig(ConfigBuilder builder) {
        return builder
                .addEnum(testEnum).name("enum").add(t -> testEnum = t.value())
                .addBoolean(bool).name("bool").add(t -> bool = t.value())
                .addByte(aByte).range((byte) 10, (byte) 20).sliderRendering().name("byte").add(t -> aByte = t.value())
                .addShort(aShort).name("short").add(t -> aShort = t.value())
                .addInt(anInt).name("int").range(10, 100).sliderRendering().add(t -> anInt = t.value())
                .addLong(aLong).name("long").add(t -> aLong = t.value())
                .addFloat(aFloat).name("float").add(t -> aFloat = t.value())
                .addDouble(aDouble).name("double").range(-1.25, 1.25).sliderRendering().add(t -> aDouble = t.value())
                .addString(aString).name("string").add(t -> aString = t.value())
                .stringList(stringList).name("string list").add(t -> stringList = t.value())
                .list(objList).name("obj list").saveElement(Obj::save).loadElement(Obj::load).factory(Obj::new).add(t -> objList = t.value())
                .exec(obj::init)
                .build();
    }

    public enum TestEnum {
        A, B, C, D
    }

    public static class Obj {

        private int test;

        protected Obj() {
            this(0);
        }

        protected Obj(int test) {
            this.test = test;
        }

        private ConfigBuilder init(ConfigBuilder builder) {
            return builder.push().name("obj").init().addInt(test).assign(t -> test = t.value()).name("int").add().pop();
        }

        // for array saving
        static void save(JsonArray array, Obj obj) {
            JsonObject object = new JsonObject();
            object.addProperty("int", obj.test);
            array.add(object);
        }

        // for array loading
        static Obj load(JsonElement element) {
            Obj obj = new Obj();
            obj.test = element.getAsJsonObject().getAsJsonPrimitive("int").getAsInt();
            return obj;
        }

        @Override
        public String toString() {
            return test + "";
        }
    }
}
