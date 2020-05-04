package toma.config.datatypes.primitives;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.datatypes.IConfigType;
import toma.config.datatypes.TypeImpl;
import toma.config.ui.ModConfigScreen;
import toma.config.ui.widget.ConfigWidget;

import java.util.function.Consumer;

public class ConfigString extends TypeImpl<String> {

    public ConfigString(String name, String comment, String value, Consumer<IConfigType<String>> assign) {
        super(name, comment, value, assign);
    }

    @Override
    public void save(JsonObject toObj) {
        toObj.addProperty(this.displayName(), this.value());
    }

    @Override
    public void load(JsonObject fromObj) {
        set(fromObj.has(this.displayName()) ? fromObj.getAsJsonPrimitive(this.displayName()).getAsString() : value());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ConfigWidget<String> create(int x, int y, int w, int h, ModConfigScreen screen) {
        return new StringWidget(x, y, w, h, this);
    }

    @SideOnly(Side.CLIENT)
    public static class StringWidget extends ConfigWidget<String> {

        private boolean selected,
                tick;
        private long lastTime;

        public StringWidget(int x, int y, int w, int h, ConfigString type) {
            super(x, y, w, h, type);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            selected = false;
            if(isMouseOver(mouseX, mouseY)) {
                selected = true;
                return true;
            }
            return false;
        }

        @Override
        public void onKeyPress(char character, int code) {
            if(selected) {
                if(character == '\b') {
                    if(!type.value().isEmpty()) type.set(type.value().substring(0, type.value().length() - 1));
                } else if(type.value().length() < 20) {
                    type.set(type.value() + character);
                }
            }
        }

        @Override
        public void pressedKey(int k1, int k2, int k3) {
            if(selected && k1 == 259) {
                if(type.value().length() > 0) {
                    type.set(type.value().substring(0, type.value().length() - 1));
                }
            }
        }

        @Override
        public void doRender(double mouseX, double mouseY, float partial) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            renderer.drawString(type.displayName(), x, y + 6, 0xffffff);
            blit(x + 100, y, width - 130, 20, 0, 40, 200, 60);
            long time = System.currentTimeMillis();
            if(time - lastTime >= 750L) {
                tick = !tick;
                lastTime = time;
            }
            renderer.drawStringWithShadow(type.value(), x + 105, y + 6, isHovered || selected ? 0xffff00 : 0xffffff);
            if(tick && selected) drawColor(x + 105 + renderer.getStringWidth(type.value()), y + 5, 2, 10, 1, 1, 1, 1);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return isMouseIn(mouseX, mouseY, x + 100, y, width - 130, 20);
        }
    }
}
