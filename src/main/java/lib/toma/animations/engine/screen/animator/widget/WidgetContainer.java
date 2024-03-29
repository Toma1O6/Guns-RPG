package lib.toma.animations.engine.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class WidgetContainer extends Widget implements INestedGuiEventHandler {

    public int backgroundColor = 0;
    public int frameSize = 0;
    public int frameColor = 0xFFFFFFFF;
    private final List<IGuiEventListener> guiEventListeners = new ArrayList<>();
    private final List<Widget> widgets = new ArrayList<>();
    private IGuiEventListener focused;
    private boolean dragging;

    public WidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height, StringTextComponent.EMPTY);
    }

    public <T extends IGuiEventListener> T addGuiEventListener(T t) {
        guiEventListeners.add(t);
        return t;
    }

    public void removeEventListener(IGuiEventListener listener) {
        guiEventListeners.remove(listener);
    }

    public <T extends Widget> T addWidget(T t) {
        widgets.add(t);
        t.x += x;
        t.y += y;
        return addGuiEventListener(t);
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
        removeEventListener(widget);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (frameSize > 0)
            fill(stack, x, y, x + width, y + height, frameColor);
        fill(stack, x + frameSize, y + frameSize, x + width - frameSize, y + height - frameSize, backgroundColor);
        renderChildren(stack, mouseX, mouseY, partialTicks);
    }

    protected void renderChildren(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        for (Widget widget : widgets) {
            widget.render(stack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        return INestedGuiEventHandler.super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        return INestedGuiEventHandler.super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        return INestedGuiEventHandler.super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
    }

    @Override
    public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
        return INestedGuiEventHandler.super.mouseScrolled(p_231043_1_, p_231043_3_, p_231043_5_);
    }

    @Override
    public void mouseMoved(double p_212927_1_, double p_212927_3_) {
        INestedGuiEventHandler.super.mouseMoved(p_212927_1_, p_212927_3_);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        return INestedGuiEventHandler.super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    @Override
    public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
        return INestedGuiEventHandler.super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
    }

    @Override
    public List<? extends IGuiEventListener> children() {
        return guiEventListeners;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public IGuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(IGuiEventListener focused) {
        this.focused = focused;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        for (Widget widget : widgets) {
            widget.visible = visible;
        }
    }
}
