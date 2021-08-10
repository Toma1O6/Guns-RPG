package lib.toma.animations.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.AnimationType;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.IAnimationPipeline;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.MutableKeyframe;
import lib.toma.animations.screen.animator.AnimationProject;
import lib.toma.animations.screen.animator.Animator;
import lib.toma.animations.screen.animator.AnimatorFrameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Timeline extends WidgetContainer {

    private final Supplier<IKeyframeSelectContext> contextSupplier;
    private final AnimationProject project;
    private final int toolbarHeight = 20;
    private final int progressBarHeight = 10;
    private final int scrollbarWidth = 4;
    private final List<Display> displayList = new ArrayList<>();
    private int scrollIndex;
    private final int displaySize;

    private IProgressBarChange progressBarChange;
    private IKeyframeSelected keyframeSelected;
    private IEventClicked eventClicked;

    public Timeline(int x, int y, int width, int height, Supplier<IKeyframeSelectContext> contextSupplier) {
        super(x, y, width, height);
        this.project = Animator.get().getProject();
        this.displaySize = (height - toolbarHeight - progressBarHeight) / 10;
        this.contextSupplier = contextSupplier;
        init();
    }

    public void init() {
        for (Widget widget : displayList)
            removeWidget(widget);
        displayList.clear();
        int vertOffset = toolbarHeight + progressBarHeight;
        boolean addEventDisplay = project.hasEvents() && scrollIndex == 0;
        int defLimit = scrollIndex + displaySize;
        int limit = addEventDisplay ? defLimit : defLimit - 1;
        if (addEventDisplay) {
            Display display = new Display(0, vertOffset, width - scrollbarWidth, 10, new StringTextComponent("Events"), this, contextSupplier);
            IAnimationEvent[] events = project.getFrameControl().getProvider().getEvents();
            for (IAnimationEvent event : events) {
                display.addWidget(new Element<>(event, IAnimationEvent::invokeAt, display, ClickActionType.EVENT));
            }
            displayList.add(addWidget(display));
        }
        AnimatorFrameProvider provider = project.getFrameControl().getProvider();
        Map<AnimationStage, List<MutableKeyframe>> map = provider.getFrames();
        List<AnimationStage> stages = new ArrayList<>(map.keySet());
        for (int i = scrollIndex; i < limit; i++) {
            if (i >= stages.size()) break;
            AnimationStage stage = stages.get(i);
            List<MutableKeyframe> list = map.get(stage);
            if (list == null || list.isEmpty()) {
                provider.deleteStage(stage);
                continue;
            }
            int j = addEventDisplay ? i - scrollIndex + 1 : i - scrollIndex;
            Display display = new Display(0, vertOffset + j * 10, width - scrollbarWidth, 10, stage.getName(), this, stage, contextSupplier);
            for (MutableKeyframe frame : list) {
                display.addWidget(new Element<>(frame, IKeyframe::endpoint, display, ClickActionType.FRAME));
            }
            displayList.add(addWidget(display));
        }
    }

    public AnimationProject getProject() {
        return project;
    }

    public void setProgressBarClickHandler(IProgressBarChange handler) {
        progressBarChange = handler;
    }

    public void setKeyframeSelectHandler(IKeyframeSelected handler) {
        keyframeSelected = handler;
    }

    public void setEventClickHandler(IEventClicked handler) {
        eventClicked = handler;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, x, y, x + width, y + height, 0x67 << 24);
        fill(stack, x, y, x + width, y + toolbarHeight, 0x44 << 24);
        renderChildren(stack, mouseX, mouseY, partialTicks);
        renderProgressBar(stack);
        renderScrollbar(stack);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width - scrollbarWidth && mouseY >= y + toolbarHeight && mouseY <= y + toolbarHeight + progressBarHeight) {
            if (isValidClickButton(button)) {
                onClick(mouseX, mouseY);
                playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClick(double mx, double my) {
        if (progressBarChange != null) {
            float value = Math.max(0.0F, Math.min(1.0F, (float) (mx / ((float) x + width))));
            progressBarChange.onChanged(value);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!super.mouseScrolled(mouseX, mouseY, amount)) {
            if (amount != 0) {
                if (isMouseOver(mouseX, mouseY)) {
                    onScrolled((int) amount);
                    return true;
                }
            }
        }
        return false;
    }

    public void onScrolled(int amount) {
        int i = -amount;
        int j = scrollIndex + i;
        AnimatorFrameProvider provider = project.getFrameControl().getProvider();
        int elements = provider.getFrames().keySet().size();
        if (project.hasEvents()) {
            ++elements;
        }
        if (j >= 0 && j <= elements - displaySize + 1) {
            scrollIndex = j;
            init();
        }
    }

    public void recompile(AnimationStage stage) {
        project.getFrameControl().getProvider().recompile(stage);
        init();
    }

    public void finishFrames() {
        project.getFrameControl().getProvider().finish();
        init();
    }

    public void add(IAnimationEvent event) {
        project.getFrameControl().getProvider().addEvent(event);
        init();
    }

    public void replace(IAnimationEvent oldEvent, IAnimationEvent replacement) {
        AnimatorFrameProvider provider = project.getFrameControl().getProvider();
        provider.removeEvent(oldEvent);
        if (replacement != null) {
            provider.addEvent(replacement);
        }
        init();
    }

    public void add(AnimationStage stage, MutableKeyframe frame) {
        project.getFrameControl().getProvider().addFrame(stage, frame);
    }

    public void remove(AnimationStage stage, MutableKeyframe frame) {
        project.getFrameControl().getProvider().removeFrame(stage, frame);
        clearSelection();
    }

    public Animator.CustomizableAnimation getAnimation() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        Animator.CustomizableAnimation animation = pipeline.get(Animator.ANIMATOR_TYPE);
        if (animation == null) {
            animation = Animator.ANIMATOR_TYPE.create(Minecraft.getInstance().player);
            pipeline.insert(Animator.ANIMATOR_TYPE, animation);
        }
        return animation;
    }

    public void playAnimation() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        AnimationType<?> type = Animator.ANIMATOR_TYPE;
        if (pipeline.get(type) == null) {
            pipeline.insert(type);
        }
    }

    public float getAnimationProgress() {
        Animator.CustomizableAnimation animation = AnimationEngine.get().pipeline().get(Animator.ANIMATOR_TYPE);
        return animation != null ? animation.getProgress() : 0.0F;
    }

    @Override
    protected boolean isValidClickButton(int p_230987_1_) {
        return p_230987_1_ == 0;
    }

    private void renderProgressBar(MatrixStack stack) {
        float progress = this.getAnimationProgress();
        int pos = x + (int) (progress * (width - scrollbarWidth - 5)) + 5;
        vLine(stack, pos, y + toolbarHeight, y + height, 0xFFFFFFFF);
        fill(stack, pos - 2, y + toolbarHeight, pos + 3, y + toolbarHeight + progressBarHeight, 0xFFFFFFFF);
    }

    private void renderScrollbar(MatrixStack stack) {
        int left = x + width - 2;
        int right = left + 2;
        int top = y + toolbarHeight + progressBarHeight;
        int bottom = y + height;
        fill(stack, left, top, right, bottom, 0xFF << 24);
        AnimatorFrameProvider provider = project.getFrameControl().getProvider();
        int total = provider.getFrames().keySet().size();
        if (project.hasEvents()) {
            ++total;
        }
        double size = 1.0 / total * (height - toolbarHeight - progressBarHeight);
        int minY = (int) (scrollIndex * size);
        int bottomIndex = Math.min(displayList.size(), total);
        int maxY = minY + (int) size * bottomIndex;
        fill(stack, left, top + minY, right, top + maxY, 0xFFFFFFFF);
    }

    private void rightClickEvent(IAnimationEvent event) {
        if (eventClicked != null)
            eventClicked.clicked(event);
    }

    private void clearSelection() {
        if (keyframeSelected != null)
            keyframeSelected.onSelect(null);
    }

    private void rightClickFrame(AnimationStage stage, MutableKeyframe iKeyframe) {
        if (keyframeSelected != null) {
            keyframeSelected.onSelect(iKeyframe != null ? IKeyframeSelectContext.of(iKeyframe, stage) : null);
        }
    }

    @FunctionalInterface
    public interface IProgressBarChange {
        void onChanged(float newValue);
    }

    @FunctionalInterface
    public interface IKeyframeSelected {
        void onSelect(IKeyframeSelectContext context);
    }

    public interface IEventClicked {
        void clicked(IAnimationEvent event);
    }

    public interface IKeyframeSelectContext extends Comparable<IKeyframeSelectContext> {

        MutableKeyframe frame();
        AnimationStage owner();

        static IKeyframeSelectContext of(MutableKeyframe frame, AnimationStage owner) {
            return new IKeyframeSelectContext() {
                @Override
                public MutableKeyframe frame() {
                    return frame;
                }

                @Override
                public AnimationStage owner() {
                    return owner;
                }
            };
        }

        @Override
        default int compareTo(IKeyframeSelectContext o) {
            return Float.compare(frame().endpoint(), o.frame().endpoint());
        }
    }

    private static class Display extends WidgetContainer {

        private final Supplier<IKeyframeSelectContext> contextSupplier;
        private final AnimationStage stage;
        private final Timeline owner;

        Display(int x, int y, int width, int height, ITextComponent title, Timeline owner, Supplier<IKeyframeSelectContext> contextSupplier) {
            this(x, y, width, height, title, owner, null, contextSupplier);
        }

        Display(int x, int y, int width, int height, ITextComponent title, Timeline owner, AnimationStage stage, Supplier<IKeyframeSelectContext> contextSupplier) {
            super(x, y, width, height);
            setMessage(title);
            this.stage = stage;
            this.owner = owner;
            this.contextSupplier = contextSupplier;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            ITextComponent title = getMessage();
            hLine(stack, x, x + width, y + 5, 0xFFFF << 16);
            Minecraft.getInstance().font.drawShadow(stack, title, x + 2, y + 1, 0xFFFFFF);
            renderChildren(stack, mouseX, mouseY, partialTicks);
        }

        public Timeline getTimeline() {
            return owner;
        }

        private void selected(ClickActionType type, Object value) {
            switch (type) {
                case EVENT:
                    owner.rightClickEvent((IAnimationEvent) value);
                    break;
                case FRAME:
                    owner.rightClickFrame(stage, (MutableKeyframe) value);
                    break;
            }
        }
    }

    private static class Element<T> extends Widget {

        private final Supplier<IKeyframeSelectContext> contextSupplier;
        private final Display owner;
        private final ClickActionType clickActionType;
        private final T t;

        public Element(T t, Function<T, Float> progress, Display owner, ClickActionType clickActionType) {
            super(0, 0, 10, 10, StringTextComponent.EMPTY);
            this.t = t;
            this.owner = owner;
            this.clickActionType = clickActionType;
            this.contextSupplier = owner.contextSupplier;

            int dx = owner.x;
            int dy = owner.getTimeline().y;
            int dw = owner.getWidth() - 5;
            float posPct = progress.apply(t);
            this.x = dx + (int) (dw * posPct);
            this.y = dy;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            int color = isSelected() ? 0xCC00 : 0xCCCC;
            fill(stack, x, y, x + width, y + height, color | (0x89 << 24));
            fill(stack, x + 2, y + 2, x + width - 2, y + height - 2, color | (0xFF << 24));
        }

        @Override
        public void onClick(double p_230982_1_, double p_230982_3_) {
            if (isSelected()) {
                owner.selected(ClickActionType.FRAME, null);
            } else {
                owner.selected(clickActionType, t);
            }
        }

        @Override
        protected boolean isValidClickButton(int p_230987_1_) {
            return p_230987_1_ == 1;
        }

        private boolean isSelected() {
            IKeyframeSelectContext context = contextSupplier.get();
            return context != null && clickActionType == ClickActionType.FRAME && t == context.frame();
        }
    }

    enum ClickActionType {
        EVENT,
        FRAME
    }
}
