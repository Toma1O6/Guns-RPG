package toma.config.datatypes;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.ui.ModConfigScreen;
import toma.config.ui.widget.ConfigWidget;

import java.util.HashMap;
import java.util.Map;

public class ConfigObject extends TypeImpl<Map<String, IConfigType<?>>> {

    private JsonObject cache;

    public ConfigObject(String name, String comment) {
        super(name, comment, new HashMap<>(), p -> {});
    }

    public void add(String name, IConfigType<?> t) {
        value().put(name, t);
    }

    @Override
    public void save(JsonObject toObj) {
        JsonObject object = new JsonObject();
        this.value().forEach((k, v) -> v.save(object));
        toObj.add(this.displayName(), object);
    }

    @Override
    public void load(JsonObject fromObj) {
        if (fromObj.has(this.displayName())) {
            cache = fromObj.getAsJsonObject(displayName());
            value().forEach((k, v) -> {
                v.load(cache);
                v.onLoad();
            });
            cache = null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ConfigWidget<Map<String, IConfigType<?>>> create(int x, int y, int w, int h, ModConfigScreen screen) {
        return new ObjectWidget(x, y, w, h, this, screen);
    }

    public void put(IConfigType<?> configType) {
        this.value().put(configType.displayName(), configType);
    }

    @SideOnly(Side.CLIENT)
    private static class ObjectWidget extends ConfigWidget<Map<String, IConfigType<?>>> {

        private ModConfigScreen screen;

        public ObjectWidget(int x, int y, int w, int h, IConfigType<Map<String, IConfigType<?>>> type, ModConfigScreen parent) {
            super(x, y, w, h, type);
            this.screen = parent;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(isMouseOver(mouseX, mouseY)) {
                Minecraft.getMinecraft().displayGuiScreen(new ModConfigScreen(type.value(), screen));
                return true;
            }
            return false;
        }

        @Override
        public void doRender(double mouseX, double mouseY, float partial) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            blit(x, y, width - 30, 20, 0, isHovered ? 20 : 0, 200, isHovered ? 40 : 20);
            int tw = renderer.getStringWidth(type.displayName()) / 2;
            int ww = (width - 30) / 2;
            renderer.drawString(type.displayName(), x - tw + ww, y + 6, isHovered ? 0xffff00 : 0xffffff);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return isMouseIn(mouseX, mouseY, x, y, width - 30, 20);
        }
    }
}
