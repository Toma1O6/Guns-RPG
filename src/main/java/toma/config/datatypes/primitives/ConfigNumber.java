package toma.config.datatypes.primitives;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.datatypes.IConfigType;
import toma.config.datatypes.TypeImpl;
import toma.config.ui.ModConfigScreen;
import toma.config.ui.widget.ConfigWidget;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigNumber<T extends Number> extends TypeImpl<T> {

    private final Range<T> numberRange;
    private boolean isSlider;
    private Function<JsonPrimitive, T> loader;

    public ConfigNumber(String name, String comment, T def, T min, T max, boolean asSlider, Function<JsonPrimitive, T> loader, Consumer<IConfigType<T>> assign) {
        super(name, comment, def, assign);
        this.numberRange = new Range<>(min, max);
        this.isSlider = asSlider;
        this.loader = loader;
    }

    public Range<T> getNumberRange() {
        return numberRange;
    }

    public abstract float getSliderValue();

    public abstract T getDragValue(float f);

    public abstract boolean filter(char character, String string);

    public abstract T parse(String input);

    @Override
    public void save(JsonObject toObj) {
        toObj.addProperty(this.displayName(), this.value());
    }

    @Override
    public void load(JsonObject fromObj) {
        set(fromObj.has(this.displayName()) ? loader.apply(fromObj.getAsJsonPrimitive(this.displayName())) : value());
    }

    public boolean isSlider() {
        return isSlider;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ConfigWidget<T> create(int x, int y, int w, int h, @Nullable ModConfigScreen screen) {
        return new NumberWidget<>(x, y, w, h, this, this::getSliderValue, this::getDragValue, this::parse, this::filter);
    }

    @SideOnly(Side.CLIENT)
    public static class NumberWidget<T extends Number> extends ConfigWidget<T> {

        private boolean isSlider;
        private Supplier<Float> sliderValue;
        private Function<Float, T> dragFunc;
        private Function<String, T> valueTransformer;
        private BiPredicate<Character, String> textFilter;

        private String value;
        private boolean selected;
        private long time;
        private boolean tick;

        public NumberWidget(int x, int y, int w, int h, ConfigNumber<T> type, Supplier<Float> sliderValue, Function<Float, T> drag, Function<String, T> valueTransformer, BiPredicate<Character, String> textFilter) {
            super(x, y, w, h, type);
            this.isSlider = type.isSlider();
            this.sliderValue = sliderValue;
            this.dragFunc = drag;
            this.value = type.value().toString();
            this.valueTransformer = valueTransformer;
            this.textFilter = textFilter;
        }

        @Override
        public void onKeyPress(char character, int code) {
            if(selected) {
                if(character == '\b') {
                    if(value.length() > 0) value = value.substring(0, value.length() - 1);
                } else if(value.length() < 20 && this.textFilter.test(character, value)) {
                    value = value + character;
                }
            }
        }

        @Override
        public void pressedKey(int k1, int k2, int k3) {
            if(selected && k1 == 259) {
                if(value.length() > 0) value = value.substring(0, value.length() - 1);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            selected = false;
            if(isMouseOver(mouseX, mouseY)) {
                if(isSlider) {
                    float f0 = ((float)mouseX - x - 100) / (width - 130f);
                    type.set(dragFunc.apply(f0));
                    return false;
                } else {
                    selected = !selected;
                    return true;
                }
            } else if(!value.equals(type.value().toString())) {
                type.set(valueTransformer.apply(value));
                return false;
            }
            return false;
        }

        @Override
        public void mouseDragged(Minecraft mc, int p_onDrag_1_, int p_onDrag_3_) {
            if(isSlider) {
                if(isMouseOver(p_onDrag_1_, p_onDrag_3_)) {
                    float f0 = ((float)p_onDrag_1_ - x - 100) / (width - 130f);
                    type.set(dragFunc.apply(f0));
                }
            }
        }

        @Override
        public void doRender(double mouseX, double mouseY, float partial) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            renderer.drawString(type.displayName(), x, y + 6, 0xffffff);
            if(isSlider) {
                float f = sliderValue.get();
                int add = (int) (f * (width - 140));
                blit(x + 100, y, width - 130, 20, 0, 40, 200, 60);
                blit(x + 100 + add, y, 10, 20, 200, isHovered ? 20 : 0, 210, isHovered ? 40 : 20);
                int i = renderer.getStringWidth(type.value().toString()) / 2;
                int j = (width - 130) / 2;
                renderer.drawStringWithShadow(value = type.value().toString(), x + 100 + j - i, y + 6, isHovered ? 0xffff00 : 0xffffff);
            } else {
                long l = System.currentTimeMillis();
                if(l - time >= 750L) {
                    this.tick = !tick;
                    time = l;
                }
                blit(x + 100, y, width - 130, 20, 0, 40, 200, 60);
                renderer.drawStringWithShadow(value, x + 105, y + 6, isHovered || selected ? 0xffff00 : 0xffffff);
                int w = renderer.getStringWidth(value);
                if(selected && tick) drawColor(x + 105 + w, y + 5, 2, 10, 1, 1, 1, 1);
            }
        }
    }

    public static class Range<T extends Number> {

        private T min, max;

        public Range(T min, T max) {
            this.min = min;
            this.max = max;
        }

        public T getMin() {
            return min;
        }

        public T getMax() {
            return max;
        }
    }
}
