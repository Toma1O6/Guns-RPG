package lib.toma.animations.screen.animator;

import lib.toma.animations.AnimationUtils;
import lib.toma.animations.QuickSort;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.*;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.function.Predicate;

public class AnimatorFrameProvider implements IKeyframeProvider {

    private final Map<AnimationStage, LinkedList<MutableKeyframe>> frameMap = new TreeMap<>(AnimationStage::compareTo);
    private final List<IAnimationEvent> eventList = new ArrayList<>();
    private final boolean events;
    private int maxStageIndex;

    // stuff which is normally handled in FrameProviderInstances
    private byte[] cache;
    private byte eventIndex;

    public AnimatorFrameProvider(IKeyframeProvider provider) {
        this.events = provider.getType().areEventsSupported();
        if (events) {
            eventList.addAll(Arrays.asList(provider.getEvents()));
            eventList.sort(Comparator.comparingDouble(IAnimationEvent::invokeAt));
        }
        Map<AnimationStage, IKeyframe[]> rawFrames = provider.getFrameMap();
        for (Map.Entry<AnimationStage, IKeyframe[]> entry : rawFrames.entrySet()) {
            AnimationStage stage = entry.getKey();
            IKeyframe[] array = entry.getValue();
            if(array.length == 0) continue;
            LinkedList<MutableKeyframe> linkedList = new LinkedList<>();
            Arrays.stream(array).map(MutableKeyframe::mutable).forEach(linkedList::add);
            frameMap.put(stage, linkedList);
        }
        computeMaxStageIndex();
    }

    @Override
    public boolean shouldAdvance(AnimationStage stage, float progress, byte frameIndex) {
        List<MutableKeyframe> list = frameMap.get(stage);
        if (list == null || frameIndex >= list.size() - 1) return false;
        MutableKeyframe frame = list.get(frameIndex);
        return frame.endpoint() <= progress;
    }

    @Override
    public IKeyframe getCurrentFrame(AnimationStage stage, float progress, byte frameIndex) {
        return frameMap.get(stage).get(frameIndex);
    }

    @Override
    public IKeyframe getOldFrame(AnimationStage stage, byte frameIndex) {
        return frameIndex == 0 ? Keyframes.none() : frameMap.get(stage).get(frameIndex - 1);
    }

    @Override
    public IAnimationEvent[] getEvents() {
        return eventList.toArray(IAnimationEvent.NO_EVENTS);
    }

    @Override
    public int getCacheSize() {
        return maxStageIndex;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrameMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FrameProviderType<?> getType() {
        throw new UnsupportedOperationException();
    }

    public void onProgressed(float progress, float progressOld, IAnimation source) {
        invokeEventsRecursive(source, progress, progressOld);
    }

    public void forceProgress(float progress) {
        Set<AnimationStage> set = frameMap.keySet();
        clrAndAdjustCache(progress, set);
    }

    public boolean blocksStageAnimation(AnimationStage stage) {
        return stage.getIndex() >= maxStageIndex;
    }

    public IKeyframe getActualFrame(AnimationStage stage, float progress) {
        byte index = cache[stage.getIndex()];
        if (shouldAdvance(stage, progress, index)) {
            index = ++cache[stage.getIndex()];
        }
        return getCurrentFrame(stage, progress, index);
    }

    public IKeyframe getLastFrame(AnimationStage stage) {
        return getOldFrame(stage, cache[stage.getIndex()]);
    }

    public IKeyframeProvider toSerializable() {
        if (frameMap.isEmpty())
            return NoFramesProvider.empty();
        boolean events = !eventList.isEmpty();
        boolean singleFrame = !events && checkSingleFrame();
        boolean tAndBack = !events && checkTargetAndBack();
        if (singleFrame) {
            return constructSingleFrameProvider();
        } else if (tAndBack) {
            return constructTargetAndBackProvider();
        } else {
            return constructFullProvider();
        }
    }

    public boolean canAddEvents() {
        return events;
    }

    private boolean checkSingleFrame() {
        return isWithinFrameCount(size -> size <= 1);
    }

    private boolean checkTargetAndBack() {
        return frameMap.size() == 1 && isWithinFrameCount(size -> size == 2);
    }

    private IKeyframeProvider constructSingleFrameProvider() {
        Map<AnimationStage, IKeyframe> map = new HashMap<>();
        for (Map.Entry<AnimationStage, LinkedList<MutableKeyframe>> entry : frameMap.entrySet()) {
            LinkedList<MutableKeyframe> list = entry.getValue();
            if (!list.isEmpty()) {
                map.put(entry.getKey(), list.getFirst());
            }
        }
        return SingleFrameProvider.fromExistingMap(map);
    }

    private IKeyframeProvider constructTargetAndBackProvider() {
        for (Map.Entry<AnimationStage, LinkedList<MutableKeyframe>> entry : frameMap.entrySet()) {
            AnimationStage target = entry.getKey();
            LinkedList<MutableKeyframe> list = entry.getValue();
            MutableKeyframe toTarget = list.getFirst();
            MutableKeyframe fromTarget = list.get(1);
            return new TargetAndBackFrameProvider(target, toTarget, fromTarget);
        }
        throw new IllegalStateException("Frame map was empty. How is this possible?");
    }

    private IKeyframeProvider constructFullProvider() {
        Map<AnimationStage, IKeyframe[]> map = new HashMap<>();
        for (Map.Entry<AnimationStage, LinkedList<MutableKeyframe>> entry : frameMap.entrySet()) {
            AnimationStage stage = entry.getKey();
            LinkedList<MutableKeyframe> frames = entry.getValue();
            IKeyframe[] array = frames.toArray(new IKeyframe[0]);
            QuickSort.sort(array, Comparator.comparingDouble(IKeyframe::endpoint));
            map.put(stage, array);
        }
        return new KeyframeProvider(map, eventList.isEmpty() ? IAnimationEvent.NO_EVENTS : eventList.toArray(IAnimationEvent.NO_EVENTS));
    }

    private boolean isWithinFrameCount(Predicate<Integer> sizePredicate) {
        for (Map.Entry<AnimationStage, LinkedList<MutableKeyframe>> entry : frameMap.entrySet()) {
            int size = entry.getValue().size();
            if (!sizePredicate.test(size)) {
                return false;
            }
        }
        return true;
    }

    private void onStageAdded(AnimationStage stage) {
        int index = stage.getIndex();
        if (index > maxStageIndex) {
            maxStageIndex = index;
            modifyCacheSize();
        }
    }

    private void computeMaxStageIndex() {
        int oldIndex = maxStageIndex;
        int biggest = AnimationUtils.getBiggestFromMap(frameMap, Map::keySet, AnimationStage::getIndex);
        if (oldIndex != biggest) {
            maxStageIndex = biggest;
            modifyCacheSize();
        }
    }

    private void modifyCacheSize() {
        byte[] newCache = new byte[maxStageIndex];
        System.arraycopy(cache, 0, newCache, 0, maxStageIndex);
        cache = newCache;
    }

    private void clrAndAdjustCache(float progress, Set<AnimationStage> set) {
        Arrays.fill(cache, (byte) 0);
        while (true) {
            if (!hasFrameAdvanced(progress, set)) {
                break;
            }
        }
    }

    private boolean hasFrameAdvanced(float progress, Set<AnimationStage> set) {
        boolean changed = false;
        for (AnimationStage stage : set) {
            byte index = cache[stage.getIndex()];
            if (shouldAdvance(stage, progress, index)) {
                cache[stage.getIndex()]++;
                changed = true;
            }
        }
        return changed;
    }

    private void invokeEventsRecursive(IAnimation source, float progress, float progressOld) {
        if (eventIndex >= eventList.size()) return;
        IAnimationEvent event = eventList.get(eventIndex);
        float target = event.invokeAt();
        if (progress >= target && progressOld < target) {
            ++eventIndex;
            event.dispatch(Minecraft.getInstance(), source);
            invokeEventsRecursive(source, progress, progressOld);
        }
    }
}
