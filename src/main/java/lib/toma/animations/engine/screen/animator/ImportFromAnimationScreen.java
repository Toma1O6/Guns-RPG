package lib.toma.animations.engine.screen.animator;

import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.engine.frame.KeyframeProvider;
import lib.toma.animations.Keyframes;
import lib.toma.animations.engine.frame.MutableKeyframe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportFromAnimationScreen extends ImportProjectScreen {

    public ImportFromAnimationScreen(AnimatorScreen screen) {
        super(screen);
    }

    @Override
    protected FrameProviderWrapper obtainWrapper(Animator animator, String path) {
        FrameProviderWrapper source = animator.getWrapper(path);
        if (source == null) {
            return null;
        }
        AnimatorFrameProvider srcProvider = source.getProvider();
        Map<AnimationStage, IKeyframe[]> frames = new HashMap<>();
        for (Map.Entry<AnimationStage, List<MutableKeyframe>> entry : srcProvider.getFrames().entrySet()) {
            List<MutableKeyframe> list = entry.getValue();
            if (list.isEmpty()) continue;
            MutableKeyframe last = list.get(list.size() - 1);
            MutableKeyframe next = new MutableKeyframe();
            next.setPosition(Keyframes.getInitialPosition(last));
            next.setRotation(Keyframes.getInitialRotation(last));
            next.setEndpoint(0.0F);
            frames.put(entry.getKey(), new IKeyframe[] {next});
        }
        return FrameProviderWrapper.modded(source.getName() + "_based", new KeyframeProvider(frames, IAnimationEvent.NO_EVENTS));
    }
}
