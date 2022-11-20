package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.screen.widgets.ContainerWidget;
import dev.toma.gunsrpg.util.ITickable;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * View represents currently displayed page in UI
 */
public abstract class View extends Widget implements INestedGuiEventHandler {

    protected final Object lock = new Object();

    protected final Minecraft client;
    protected final FontRenderer font;
    protected final IViewManager manager;
    private final List<IGuiEventListener> eventListeners = new ArrayList<>();
    private final List<Widget> widgets = new ArrayList<>();
    private final List<ITickable> tickables = new ArrayList<>();
    private IGuiEventListener focused;
    private boolean dragging;

    public View(int windowWidth, int windowHeight, IViewManager manager) {
        super(0, 0, windowWidth, windowHeight, StringTextComponent.EMPTY);
        this.manager = manager;
        this.client = Minecraft.getInstance();
        this.font = client.font;
    }

    public void resizeFor(MainWindow window) {
        this.width = window.getGuiScaledWidth();
        this.height = window.getGuiScaledHeight();
    }

    protected void init() {
    }

    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

    }

    public void tick() {
        synchronized (lock) {
            tickables.forEach(ITickable::tick);
        }
    }

    public void sortRenderOrder() {
        synchronized (lock) {
            widgets.sort(ContainerWidget::compareWidgets);
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
        return eventListeners;
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

    @Override
    public final void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderView(stack, mouseX, mouseY, partialTicks);
        synchronized (lock) {
            widgets.forEach(widget -> widget.render(stack, mouseX, mouseY, partialTicks));
        }
    }

    public void addEventListener(IGuiEventListener listener) {
        synchronized (lock) {
            if (listener instanceof ITickable) {
                tickables.add((ITickable) listener);
            }
            eventListeners.add(listener);
        }
    }

    public <T extends Widget> T addWidget(T widget) {
        addEventListener(widget);
        synchronized (lock) {
            widgets.add(widget);
        }
        return widget;
    }

    protected void clear() {
        synchronized (lock) {
            this.widgets.clear();
            this.eventListeners.clear();
            this.tickables.clear();
        }
        this.focused = null;
        this.dragging = false;
    }
}
