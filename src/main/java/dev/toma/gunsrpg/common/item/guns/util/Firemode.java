package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.IFlags;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.LazyLoader;

public enum Firemode {

    SINGLE(
            "Single",
            new LazyLoader<>(IInputEventHandler.Single::new),
            InputEventListenerType.ON_INPUT
    ),

    BURST(
            "Burst",
            new LazyLoader<>(IInputEventHandler.Burst::new),
            InputEventListenerType.ON_INPUT,
            InputEventListenerType.ON_TICK
    ),

    FULL_AUTO(
            "Full Auto",
            new LazyLoader<>(IInputEventHandler.FullAuto::new),
            InputEventListenerType.ON_TICK
    ),

    SINGLE_BARREL(
            "Single Barrel",
            new LazyLoader<>(IInputEventHandler.Single::new),
            InputEventListenerType.ON_INPUT
    ),

    BOTH_BARRELS(
            "Both Barrels",
            new LazyLoader<>(IInputEventHandler.Barrage::new),
            InputEventListenerType.ON_INPUT
    ),

    BARRAGE(
            "Barrage",
            new LazyLoader<>(IInputEventHandler.Barrage::new),
            InputEventListenerType.ON_INPUT
    );

    private final String name;
    private final LazyLoader<IInputEventHandler> handler;
    private final int eventFlags;

    Firemode(String name, LazyLoader<IInputEventHandler> handler, InputEventListenerType... eventFlags) {
        this.name = name;
        this.handler = handler;
        this.eventFlags = IFlags.combine(InputEventListenerType::getFlag, eventFlags);
    }

    public boolean isSubscribedTo(InputEventListenerType event) {
        int id = event.getFlag();
        return (eventFlags & id) == id;
    }

    public IInputEventHandler getHandler() {
        return handler.get();
    }

    public static Firemode get(int id) {
        Firemode[] modes = values();
        int i = ModUtils.clamp(id, 0, modes.length - 1);
        return modes[i];
    }

    public String getName() {
        return name;
    }
}
