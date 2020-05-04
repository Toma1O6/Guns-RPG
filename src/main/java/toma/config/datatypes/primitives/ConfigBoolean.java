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

public class ConfigBoolean extends TypeImpl<Boolean> {

    public ConfigBoolean(String name, String comment, boolean value, Consumer<IConfigType<Boolean>> load) {
        super(name, comment, value, load);
    }

    @Override
    public void save(JsonObject toObj) {
        toObj.addProperty(this.displayName(), this.value());
    }

    @Override
    public void load(JsonObject fromObj) {
        set(fromObj.has(this.displayName()) ? fromObj.getAsJsonPrimitive(this.displayName()).getAsBoolean() : value());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ConfigWidget<Boolean> create(int x, int y, int w, int h, ModConfigScreen screen) {
        return new BooleanWidget(x, y, w, h, this);
    }

    @SideOnly(Side.CLIENT)
    public static class BooleanWidget extends ConfigWidget<Boolean> {

        public BooleanWidget(int x, int y, int w, int h, ConfigBoolean cfg) {
            super(x, y, w, h, cfg);
        }

        @Override
        public void doRender(double mouseX, double mouseY, float partial) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            blit(x + 100, y, width - 130, 20, 0, isHovered ? 20 : 0, 200, isHovered ? 40 : 20);
            renderer.drawStringWithShadow(type.displayName(), x, y + 6, 0xffffff);
            int w = renderer.getStringWidth(type.value().toString()) / 2;
            int f = (width - 130) / 2;
            renderer.drawStringWithShadow(type.value().toString(), x + 100 - w + f, y + 6, isHovered ? 0xffff00 : type.value() ? 0x00cd00 : 0xcd0000);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(isMouseOver(mouseX, mouseY)) {
                this.type.set(!type.value());
                return true;
            }
            return false;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return isMouseIn(mouseX, mouseY, x + 100, y, width - 130, 20);
        }
    }
}
