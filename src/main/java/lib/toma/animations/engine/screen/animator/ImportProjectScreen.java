package lib.toma.animations.engine.screen.animator;

import lib.toma.animations.AnimationEngine;
import lib.toma.animations.engine.screen.animator.dialog.DialogScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ImportProjectScreen extends AbstractImportScreen {

    protected static final ITextComponent IMPORT = new StringTextComponent("Import");
    private final IBackgroundAnimationUnsetter unsetter;

    public ImportProjectScreen(AnimatorScreen screen, IBackgroundAnimationUnsetter unsetter) {
        super(screen);
        this.unsetter = unsetter;
    }

    @Override
    protected void placeControlButtons(int totalButtonWidth, int buttonWidth) {
        int y = height - 25;
        cancel = addButton(new Button(5, y, buttonWidth, 20, DialogScreen.CANCEL, this::cancelClicked));
        confirm = addButton(new Button(10 + buttonWidth, y, buttonWidth, 20, IMPORT, this::confirmClicked));
    }

    protected FrameProviderWrapper obtainWrapper(Animator animator, String path) {
        return animator.getWrapper(path);
    }

    @Override
    protected void confirmClicked(Button button) {
        Animator animator = Animator.get();
        unsetBackgroundAnimation();
        FrameProviderWrapper wrapper = obtainWrapper(animator, getSelectedPath());
        if (wrapper != null) {
            animator.setUsingProject(new AnimationProject(wrapper));
            AnimationEngine.get().pipeline().insert(Animator.ANIMATOR_TYPE);
        }
        showParent();
    }

    protected void unsetBackgroundAnimation() {
        unsetter.clearBackgroundAnimation();
    }

    @FunctionalInterface
    public interface IBackgroundAnimationUnsetter {
        void clearBackgroundAnimation();
    }
}
