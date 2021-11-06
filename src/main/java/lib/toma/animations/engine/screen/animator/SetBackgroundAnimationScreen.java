package lib.toma.animations.engine.screen.animator;

import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.engine.screen.animator.dialog.DialogScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SetBackgroundAnimationScreen extends ImportProjectScreen {

    private static final ITextComponent SET = new StringTextComponent("Set");
    private static final ITextComponent DELETE = new StringTextComponent("Unset");
    private final IBackgroundAnimationSetter setter;

    public SetBackgroundAnimationScreen(AnimatorScreen screen, IBackgroundAnimationUnsetter unsetter, IBackgroundAnimationSetter setter) {
        super(screen, unsetter);
        this.setter = setter;
    }

    @Override
    protected void placeControlButtons(int totalButtonWidth, int buttonWidth) {
        int componentAreaWidth = width - 20;
        int componentWidth = componentAreaWidth / 3;
        int y = height - 25;

        this.confirm = addButton(new Button(5, y, componentWidth, 20, SET, this::confirmClicked));
        Button delete = addButton(new Button(11 + componentWidth, y, componentWidth, 20, DELETE, this::deleteClicked));
        this.cancel = addButton(new Button(17 + 2 * componentWidth, y, componentWidth, 20, DialogScreen.CANCEL, this::cancelClicked));

        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        delete.active = pipeline.has(Animator.BACKGROUND_TYPE);
    }

    @Override
    protected void confirmClicked(Button button) {
        Animator animator = Animator.get();
        String path = getSelectedPath();
        FrameProviderWrapper wrapper = animator.getWrapper(path);
        IKeyframeProvider provider = wrapper.getProvider();
        setter.setBackgroundAnimation(provider);
        showParent();
    }

    private void deleteClicked(Button button) {
        unsetBackgroundAnimation();
        showParent();
    }

    @FunctionalInterface
    public interface IBackgroundAnimationSetter {
        void setBackgroundAnimation(IKeyframeProvider provider);
    }
}
