package dev.toma.gunsrpg.client.screen.widgets;

import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.util.math.Vec2i;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;

import java.util.IdentityHashMap;
import java.util.Map;

public class PannableWidget extends ContainerWidget {

    private final IContentManager manager;
    private final Map<Widget, IVec2i> initialPositions = new IdentityHashMap<>();
    private int posX;
    private int posY;
    private int boundX;
    private int boundY;
    private IClickResponder<?> responder;

    public PannableWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.manager = this::addWidget;
        setEmptyClickResponder(obj -> {});
    }

    public void fill(IContentFillHandler fillHandler) {
        fill(fillHandler, true);
    }

    public void fill(IContentFillHandler fillHandler, boolean shouldClear) {
        if (shouldClear) {
            clear();
        }
        this.posX = 0;
        this.posY = 0;
        fillHandler.fillElements(manager, x, y);
        moveToInitialPositions();
        saveInitialPositions();
        updateChildren();
        computePannableBounds();
    }

    public <T> void setEmptyClickResponder(IClickResponder<T> clickResponder) {
        this.responder = clickResponder;
    }

    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.childrenAsStream().forEach(this::updateVisibility);
        computePannableBounds();
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        boolean clicked = super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        if (!clicked && isMouseOver(p_231044_1_, p_231044_3_)) {
            responder.onElementClicked(null);
            return true;
        }
        return clicked;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isValidClickButton(button)) {
            onDrag(mouseX, mouseY, dragX, dragY);
            return false;
        }
        return false;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double moveX, double moveY) {
        this.posX += moveValue(moveX);
        this.posY += moveValue(moveY);
        this.posX = MathHelper.clamp(posX, -boundX, 0);
        this.posY = MathHelper.clamp(posY, -boundY, 0);
        updateChildren();
    }

    private int moveValue(double value) {
        boolean negative = value < 0;
        int ceil = (int) (Math.ceil(Math.abs(value)));
        return negative ? -ceil : ceil;
    }

    @Override
    public void clear() {
        super.clear();
        initialPositions.clear();
    }

    private void updateChildren() {
        this.childrenAsStream().forEach(this::updateChildWidget);
    }

    private void updateChildWidget(Widget widget) {
        IVec2i initialPosition = initialPositions.computeIfAbsent(widget, obj -> new Vec2i(obj.x, obj.y));
        widget.x = initialPosition.x() + posX;
        widget.y = initialPosition.y() + posY;
        updateVisibility(widget);
    }

    private void moveToInitialPositions() {
        this.childrenAsStream().forEach(this::loadInitialPosition);
    }

    private void saveInitialPositions() {
        this.childrenAsStream().forEach(widget -> initialPositions.put(widget, new Vec2i(widget.x, widget.y)));
    }

    private void loadInitialPosition(Widget widget) {
        IVec2i oldPos = initialPositions.computeIfAbsent(widget, obj -> new Vec2i(obj.x, obj.y));
        widget.x = oldPos.x();
        widget.y = oldPos.y();
    }

    private void updateVisibility(Widget widget) {
        //widget.visible = checkInViewport(widget.x, widget.y) && checkInViewport(widget.x + widget.getWidth(), widget.y + widget.getHeight());
    }

    private boolean checkInViewport(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    private void computePannableBounds() {
        this.boundX = initialPositions.values().stream().mapToInt(IVec2i::x).max().orElse(0) - width / 2;
        this.boundY = initialPositions.values().stream().mapToInt(IVec2i::y).max().orElse(0) - height / 2;
    }

    @FunctionalInterface
    public interface IContentFillHandler {
        void fillElements(IContentManager filler, int x, int y);
    }

    @FunctionalInterface
    public interface IContentManager {
        <W extends Widget> W add(W w);
    }
}
