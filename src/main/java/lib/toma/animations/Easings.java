package lib.toma.animations;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

/**
 * List of all easing functions
 *
 * @author Toma
 * @see <a href="https://easings.net/">Easing functions are implemented according to this website</a>
 */
public enum Easings implements IEasing {

    LINEAR(x -> x, "LIN"),

    EASE_IN_SINE(x -> 1.0F - MathHelper.cos(piMulX(x) / 2.0F), "IN SINE"),
    EASE_IN_CUBIC(x -> x * x * x, "IN CUBIC"),
    EASE_IN_QUINT(x -> x * x * x * x * x, "IN QUINT"),
    EASE_IN_QUAD(x -> x * x, "IN QUAD"),
    EASE_IN_QUART(x -> x * x * x * x, "IN QUART"),
    EASE_IN_EXP(x -> x == 0 ? 0 : (float) Math.pow(2.0F, 10.0F * x - 10.0F), "IN EXP"),

    EASE_OUT_SINE(x -> MathHelper.sin(piMulX(x) / 2.0F), "OUT SINE"),
    EASE_OUT_CUBIC(x -> 1.0F - (float) Math.pow(1.0F - x, 3), "OUT CUBIC"),
    EASE_OUT_QUINT(x -> 1.0F - (float) Math.pow(1.0F - x, 5), "OUT QUINT"),
    EASE_OUT_QUAD(x -> 1.0F - (1.0F - x) * (1.0F - x), "OUT QUAD"),
    EASE_OUT_QUART(x -> 1.0F - (float) Math.pow(1.0F - x, 4), "OUT QUART"),
    EASE_OUT_EXP(x -> x == 1.0F ? 1.0F : 1.0F - (float) Math.pow(2.0F, -10.0F * x), "OUT EXP"),

    EASE_IN_OUT_SINE(x -> -(MathHelper.cos(piMulX(x)) - 1) / 2.0F, "INOUT SINE"),
    EASE_IN_OUT_CUBIC(x -> x < 0.5F ? 4.0F * x * x * x : 1.0F - (float) Math.pow(-2.0F * x + 2.0F, 3) / 2.0F, "INOUT CUBIC"),
    EASE_IN_OUT_QUINT(x -> x < 0.5F ? 16.0F * x * x * x * x * x : 1.0F - (float) Math.pow(-2.0F * x + 2.0F, 5) / 2.0F, "INOUT QUINT"),
    EASE_IN_OUT_QUAD(x -> x < 0.5F ? 2.0F * x * x : 1.0F - (float) Math.pow(-2.0F * x + 2.0F, 2.0F) / 2.0F, "INOUT QUAD"),
    EASE_IN_OUT_QUART(x -> x < 0.5F ? 8.0F * x * x * x * x : 1.0F - (float) Math.pow(-2.0F * x + 2.0F, 4.0F) / 2.0F, "INOUT QUART"),
    EASE_IN_OUT_EXP(x -> x == 0.0F ? 0.0F : x == 1.0F ? 1.0F : x < 0.5F ? (float) Math.pow(2.0F, 20.0F * x - 10.0F) / 2.0F : (2.0F - (float) Math.pow(2.0F, -20.0F * x + 10.0F)) / 2.0F, "INOUT EXP"),

    JUMP_START(x -> x > 0.0F ? 1.0F : 0.0F, "JUMP START"),
    JUMP_MIDDLE(x -> x > 0.5F ? 1.0F : 0.0F, "JUMP MIDDLE"),
    JUMP_END(x -> x == 1.0F ? 1.0F : 0.0F, "JUMP END");

    private final IEasingFunction function;
    private final String shortName;
    private ITextComponent displayComponent;

    Easings(IEasingFunction function, String shortName) {
        this.function = function;
        this.shortName = shortName;

        EasingRegistry.getRegistry().register(this, (byte) ordinal());
    }

    @Override
    public float ease(float in) {
        return function.ease(in);
    }

    @Override
    public byte getEasingId() {
        return (byte) ordinal();
    }

    @Override
    public void setEasingId(byte id) {
        throw new UnsupportedOperationException("Cannot set easing ID on enum type!");
    }

    @Override
    public String getEasingName() {
        return shortName;
    }

    @Override
    public ITextComponent getDisplayText() {
        if (displayComponent == null) {
            TextFormatting color = getColor();
            String name = getFullDisplayName();
            displayComponent = new StringTextComponent(color + name);
        }
        return displayComponent;
    }

    private String getFullDisplayName() {
        String lowercase = name().toLowerCase(Locale.ROOT);
        String[] words = lowercase.split("_");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String formatted = word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1);
            words[i] = formatted;
        }
        return String.join(" ", words);
    }

    private static float piMulX(float x) {
        return x * (float) Math.PI;
    }

    private TextFormatting getColor() {
        int i = ordinal();
        if (i == 0)
            return TextFormatting.RED;
        int j = (i - 1) % 3;
        switch (j) {
            case 0:
                return TextFormatting.GREEN;
            case 1:
                return TextFormatting.YELLOW;
            case 2:
                return TextFormatting.RED;
            default:
                return TextFormatting.WHITE;
        }
    }

    @FunctionalInterface
    public interface IEasingFunction {

        float ease(float x);
    }
}
