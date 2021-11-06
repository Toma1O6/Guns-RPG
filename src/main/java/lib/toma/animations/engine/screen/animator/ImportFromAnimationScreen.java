package lib.toma.animations.engine.screen.animator;

public class ImportFromAnimationScreen extends ImportProjectScreen {

    public ImportFromAnimationScreen(AnimatorScreen screen) {
        super(screen);
    }

    @Override
    protected FrameProviderWrapper obtainWrapper(Animator animator, String path) {
        FrameProviderWrapper wrapper = super.obtainWrapper(animator, path);
        getParent().setBackgroundAnimation(wrapper.getProvider());
        return wrapper;
    }
}
