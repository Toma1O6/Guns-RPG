package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.datatypes.IConfigType;
import toma.config.datatypes.TypeImpl;
import toma.config.ui.ModConfigListScreen;
import toma.config.ui.ModConfigScreen;
import toma.config.ui.widget.ConfigWidget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigList<T> extends TypeImpl<List<T>> {

    private BiConsumer<JsonArray, T> save;
    private Function<JsonElement, T> load;
    private Supplier<T> objectFactory;
    private boolean fixedSize;
    private int definedSize;

    public ConfigList(String name, String comment, List<T> value, boolean fixedSize, Consumer<IConfigType<List<T>>> consumer, BiConsumer<JsonArray, T> save, Function<JsonElement, T> load, Supplier<T> objectFactory) {
        super(name, comment, value, consumer == null ? t -> {} : consumer);
        this.fixedSize = fixedSize;
        if(fixedSize) definedSize = value.size();
        this.save = save;
        this.load = load;
        this.objectFactory = objectFactory;
    }

    public boolean hasFixedSize() {
        return fixedSize;
    }

    public T get(int id) {
        return this.value().get(id);
    }

    public void remove(int id) {
        this.value().remove(id);
    }

    public void add() {
        this.value().add(objectFactory.get());
    }

    @Override
    public void save(JsonObject toObj) {
        JsonArray array = new JsonArray();
        Collection<T> collection = this.value();
        collection.forEach(t -> save.accept(array, t));
        toObj.add(this.displayName(), array);
    }

    @Override
    public void load(JsonObject fromObj) {
        if(!fromObj.has(this.displayName()) || !fromObj.get(this.displayName()).isJsonArray()) {
            this.set(value());
        } else {
            this.set(new ArrayList<>());
            JsonArray array = fromObj.getAsJsonArray(this.displayName());
            array.forEach(e -> this.value().add(load.apply(e)));
            if(fixedSize) {
                if(value().size() != definedSize) {
                    throw new UnsupportedOperationException("Invalid list size! You cannot add/remove elements in this config type!");
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ConfigWidget<List<T>> create(int x, int y, int w, int h, ModConfigScreen screen) {
        return new ListWidget<>(x, y, w, h, this, screen);
    }

    @SideOnly(Side.CLIENT)
    public static class ListWidget<T> extends ConfigWidget<List<T>> {

        private GuiScreen parent;

        public ListWidget(int x, int y, int w, int h, ConfigList<T> list, GuiScreen parent) {
            super(x, y, w, h, list);
            this.parent = parent;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(isMouseOver(mouseX, mouseY)) {
                Minecraft.getMinecraft().displayGuiScreen(new ModConfigListScreen<>((ConfigList<T>) this.type, parent));
                return true;
            }
            return false;
        }

        @Override
        public void doRender(double mouseX, double mouseY, float partial) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            blit(x, y, width - 30, height, 0, isHovered ? 20 : 0, 200, isHovered ? 40 : 20);
            int tw = renderer.getStringWidth(type.displayName()) / 2;
            int ww = (width - 30) / 2;
            renderer.drawString(type.displayName(), x - tw + ww, y + 6, isHovered ? 0xffff00 : 0xffffff);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return isMouseIn(mouseX, mouseY, x, y, width - 30, height);
        }
    }
}
