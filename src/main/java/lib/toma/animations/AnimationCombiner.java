package lib.toma.animations;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.IKeyframeProvider;
import lib.toma.animations.pipeline.frame.KeyframeProvider;
import lib.toma.animations.pipeline.frame.Keyframes;

import java.util.*;

public final class AnimationCombiner {

    // just a utility class
    private AnimationCombiner() {}

    public static Builder create() {
        return new Builder();
    }

    public static final class Builder {

        private final List<Entry> entries = new ArrayList<>();

        private Builder() {}

        public void addSingle(IKeyframeProvider source, float point) {
            entries.add(new Entry(source, point));
        }

        public void addRepeated(IKeyframeProvider source, float point, int count) {
            entries.add(new Entry(source, point, count));
        }

        public IKeyframeProvider combine() {
            float min = 0.0F;
            entries.sort(Entry::compareTo);
            List<Entry> fullEntryList = splitRepeated(); // splits all entries which are repeated n times to n new entries
            Map<AnimationStage, List<IKeyframe>> frames = new HashMap<>();
            List<IAnimationEvent> eventList = new ArrayList<>();
            for (Entry entry : fullEntryList) {
                IKeyframeProvider provider = entry.insertionSource;
                float point = entry.insertionPoint;
                IAnimationEvent[] events = provider.getEvents();
                Map<AnimationStage, IKeyframe[]> entryFrames = provider.getFrameMap();
                for (Map.Entry<AnimationStage, IKeyframe[]> mapEntry : entryFrames.entrySet()) {
                    IKeyframe[] frameArray = mapEntry.getValue();
                    AnimationStage stage = mapEntry.getKey();
                    List<IKeyframe> keyframes = frames.computeIfAbsent(stage, k -> new ArrayList<>());
                    for (IKeyframe keyframe : frameArray) {
                        float framePoint = keyframe.endpoint();
                        float modifiedFramePoint = (point - min) * framePoint + min;
                        keyframes.add(Keyframes.keyframe(keyframe.positionTarget(), keyframe.rotationTarget(), modifiedFramePoint));
                    }
                }
                for (IAnimationEvent event : events) {
                    float eventPoint = event.invokeAt();
                    float modified = (point - min) * eventPoint + min;
                    eventList.add(event.copyAt(modified));
                }
                min = point;
            }
            Map<AnimationStage, IKeyframe[]> map = new HashMap<>(); // convert keyframe list to array, sort and compile
            for (Map.Entry<AnimationStage, List<IKeyframe>> entry : frames.entrySet()) {
                List<IKeyframe> list = entry.getValue();
                list.sort(Comparator.comparingDouble(IKeyframe::endpoint));
                if (list.size() > 1) {
                    for (int i = 1; i < list.size(); i++) {
                        IKeyframe parent = list.get(i - 1);
                        IKeyframe child = list.get(i);
                        child.baseOn(parent);
                    }
                }
                map.put(entry.getKey(), list.toArray(new IKeyframe[0]));
            }
            eventList.sort(Comparator.comparingDouble(IAnimationEvent::invokeAt));
            return new KeyframeProvider(map, eventList.isEmpty() ? IAnimationEvent.NO_EVENTS : eventList.toArray(IAnimationEvent.NO_EVENTS));
        }

        private List<Entry> splitRepeated() {
            List<Entry> list = new ArrayList<>();
            float lastPoint = 0.0F;
            for (Entry entry : entries) {
                if (entry.insertionCount == 1) {
                    list.add(entry);
                    lastPoint = entry.insertionPoint;
                    continue;
                }
                float pool = entry.insertionPoint - lastPoint;
                double modifier = 1.0 / entry.insertionCount;
                for (int i = 1; i <= entry.insertionCount; i++) {
                    double localModifier = modifier * i;
                    float point = lastPoint + (float) (pool * localModifier);
                    list.add(new Entry(entry.insertionSource, point));
                }
                lastPoint = entry.insertionPoint;
            }
            return list;
        }
    }

    private static class Entry implements Comparable<Entry> {
        private final int insertionCount;
        private final float insertionPoint;
        private final IKeyframeProvider insertionSource;

        Entry(IKeyframeProvider source, float point, int count) {
            insertionSource = source;
            insertionPoint = point;
            insertionCount = count;
        }

        Entry(IKeyframeProvider source, float point) {
            this(source, point, 1);
        }

        @Override
        public int compareTo(Entry o) {
            return Float.compare(insertionPoint, o.insertionPoint);
        }
    }
}
