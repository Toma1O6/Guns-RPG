package toma.config.datatypes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.Config;
import toma.config.ui.ModConfigScreen;
import toma.config.ui.widget.ConfigWidget;

import java.util.function.Consumer;

public class ConfigEnum<T extends Enum<T>> extends TypeImpl<T> {

    private T[] values;

    public ConfigEnum(String name, String comment, T value, T[] values, Consumer<IConfigType<T>> assign) {
        super(name, comment, value, assign);
        this.values = values;
    }

    @Override
    public void save(JsonObject toObj) {
        JsonObject enumObj = new JsonObject();
        JsonArray validNames = new JsonArray();
        for(int i = 0; i < values.length; i++) {
            validNames.add(values[i].name());
        }
        enumObj.add("valid", validNames);
        enumObj.addProperty("value", value().name());
        toObj.add(this.displayName(), enumObj);
    }

    @Override
    public void load(JsonObject fromObj) {
        if(fromObj.has(displayName())) {
            JsonObject en = fromObj.getAsJsonObject(displayName());
            String name = en.has("value") ? en.getAsJsonPrimitive("value").getAsString() : values[0].name();
            try {
                set(Enum.valueOf(value().getDeclaringClass(), name));
            } catch (Exception e) {
                Config.log.fatal("Couldn't load enum {} for name {}, using default value", value().getDeclaringClass(), name);
                set(values[0]);
            }
        } else {
            set(values[0]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ConfigWidget<T> create(int x, int y, int w, int h, ModConfigScreen screen) {
        return new EnumWidget<>(x, y, w, h, this);
    }

    @SideOnly(Side.CLIENT)
    protected static class EnumWidget<T extends Enum<T>> extends ConfigWidget<T> {

        private T[] values;

        protected EnumWidget(int x, int y, int w, int h, ConfigEnum<T> type) {
            super(x, y, w, h, type);
            this.values = type.values;
        }

        protected ConfigEnum<T> asEnum() {
            return (ConfigEnum<T>) type;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(isMouseOver(mouseX, mouseY)) {
                Enum<T> tEnum = asEnum().value();
                int o = tEnum.ordinal();
                int n = o == values.length - 1 ? 0 : o + 1;
                type.set(values[n]);
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return isMouseIn(mouseX, mouseY, x + 100, y, width - 130, 20);
        }

        @Override
        public void doRender(double mouseX, double mouseY, float partial) {
            blit(x + 100, y, width - 130, 20, 0, isHovered ? 20 : 0, 200, isHovered ? 40 : 20);
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            renderer.drawString(type.displayName(), x, y + 6, 0xffffff);
            int tw = renderer.getStringWidth(type.value().toString()) / 2;
            int ww = (width - 130) / 2;
            renderer.drawStringWithShadow(type.value().toString(), x + 100 - tw + ww, y + 6, isHovered ? 0xffff00 : 0xffffff);
        }
    }
}
