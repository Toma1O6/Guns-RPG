package lib.toma.animations.screen.animator;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public abstract class DialogScreen extends Screen {

    private int left;
    private int top;
    private int xSize;
    private int ySize;
    private final AnimatorScreen parent;
    protected Button confirm;
    protected Button cancel;

    public DialogScreen(ITextComponent title, AnimatorScreen parent) {
        super(title);
        this.parent = parent;
        setDimensions(176, 166);
    }

    public static void handleDefaultKeys(int keycode, Button confirm, Button cancel) {
        if (keycode == GLFW.GLFW_KEY_ENTER || keycode == GLFW.GLFW_KEY_KP_ENTER) {
            if (confirm != null)
                confirm.onPress();
        } else if (keycode == GLFW.GLFW_KEY_ESCAPE) {
            if (cancel != null)
                cancel.onPress();
        }
    }

    public static int parse(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return fallback;
        }
    }

    @Override
    public void init(Minecraft mc, int width, int height) {
        super.init(mc, width, height);
        parent.init(mc, width, height);
    }

    @Override
    protected void init() {
        left = (width - xSize) / 2;
        top = (height - ySize) / 2;
    }

    public final void setDimensions(int x, int y) {
        xSize = x;
        ySize = y;
    }

    @Override
    public final void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        parent.render(stack, mouseX, mouseY, partialTicks);
        fill(stack, left, top, left + xSize, top + ySize, 0x9A << 24);
        font.drawShadow(stack, title, left + 5, top + 5, 0xFFFFFF);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int code, int scan, int modifiers) {
        handleDefaultKeys(code, confirm, cancel);
        return super.keyPressed(code, scan, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void showParent() {
        minecraft.setScreen(parent);
    }

    public int left() {
        return left;
    }

    public int top() {
        return top;
    }

    public int dWidth() {
        return xSize;
    }

    public int dHeight() {
        return ySize;
    }

    protected static class SuggestionResponder implements Consumer<String> {

        private final String suggestion;
        private final TextFieldWidget widget;
        private final Consumer<String> responder;

        protected SuggestionResponder(String suggestion, TextFieldWidget widget, Consumer<String> responder) {
            this.suggestion = suggestion;
            this.widget = widget;
            this.responder = responder;
        }

        @Override
        public void accept(String s) {
            if (s.isEmpty())
                widget.setSuggestion(suggestion);
            else
                widget.setSuggestion(null);
            responder.accept(s);
        }
    }
}
